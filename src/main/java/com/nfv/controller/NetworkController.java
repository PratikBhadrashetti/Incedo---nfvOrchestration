package com.nfv.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.PrivateNetwork;
import com.nfv.entity.Tenant;
import com.nfv.entity.TenantNetwork;
import com.nfv.entity.User;
import com.nfv.error.NFVException;
import com.nfv.model.AllocationPools;
import com.nfv.model.ExternalGatewayInfo;
import com.nfv.model.Network;
import com.nfv.model.Networks;
import com.nfv.model.RestNetwork;
import com.nfv.model.RestNetworkId;
import com.nfv.model.RestNetworks;
import com.nfv.model.RestRouter;
import com.nfv.model.RestRouters;
import com.nfv.model.RestSubnet;
import com.nfv.model.RestSubnetId;
import com.nfv.model.Router;
import com.nfv.model.Subnet;
import com.nfv.repository.PrivateNetworkRepository;
import com.nfv.repository.TenantNetworkRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.VimRepository;
import com.nfv.utils.NFVConstants;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@RestController
public class NetworkController {

	private static final Logger logger = LoggerFactory.getLogger(NetworkController.class);

	@Value("${get.vnf.uri}")
	String getVnfURI;

	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${auth.token.key}")
	String authTokenKey;

	@Value("${post.openstack.project.uri}")
	String postOSProjectURI;

	@Value("${post.openstack.users.uri}")
	String postOSUsersURI;

	@Value("${get.openstack.roles.uri}")
	String getOSRolesURI;

	@Value("${post.openstack.network}")
	String postNetwork;

	@Value("${post.openstack.subnet}")
	String postSubnet;

	@Value("${post.openstack.routers}")
	String postRouter;

	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Resource
	TenantRepository tenantRepository;

	@Resource
	PrivateNetworkRepository privateNetworkRepository;

	@Resource
	TenantNetworkRepository tenantNetworkRepository;

	@Resource
	VimRepository vimRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NFVUtility utility;

	@RequestMapping(value = "/privatenetwork", method = RequestMethod.POST)
	public ResponseEntity<PrivateNetwork> privatenetwork(@RequestBody PrivateNetwork privateNetwork) throws NFVException {
		try {
			logger.info("enter privatenetwork || privateNetwork = "+ privateNetwork);
			// utility.checkAdminPermissions();
			return new ResponseEntity<>(privateNetworkRepository.save(privateNetwork), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/privatenetwork", method = RequestMethod.GET)
	public ResponseEntity<Iterable<PrivateNetwork>> privatenetwork() throws NFVException {
		try {
			logger.info("enter privatenetwork : ");
			// utility.checkAdminPermissions();
			List<PrivateNetwork> privateNetworks = (List<PrivateNetwork>) privateNetworkRepository.findAll();
			logger.info("exit privatenetwork : ");
			return new ResponseEntity<>(privateNetworks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/vim/{id}/networktenant", method = RequestMethod.GET)
	public ResponseEntity<Iterable<TenantNetwork>> tenantnetwork(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		return new ResponseEntity<>(tenantNetworkRepository.findByTenantAndVim(user.getTenant(), vimRepository.findOne(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/networktenant", method = RequestMethod.POST)
	public ResponseEntity<Void> tenantnetwork(@RequestBody TenantNetwork t) throws NFVException {
		try {
			TenantNetwork tenantNetwork = tenantNetworkRepository.save(t);
			Tenant tenant = tenantRepository.findOne(tenantNetwork.getTenant().getId());
			PrivateNetwork privateNetwork = privateNetworkRepository.findOne(tenantNetwork.getPrivateNetwork().getId());
//			Tenant adminTenant = tenantRepository.findByName("Admin");
			String authTokenValue = restClient.getToken(utility.getUser().getTenant(), tenantNetwork.getVim());

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);

			String result = restClient.get(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postNetwork, map);
			RestNetworks networksObject = mapper.readValue(result, RestNetworks.class);
			List<Networks> networks = networksObject.getNetworks();
			String physicalNetworkId = null;
			for (Networks n: networks) {
				if (n.getName().equals("provider")) {
					physicalNetworkId = n.getId();
					break;
				}
			}
			if (physicalNetworkId == null) {
				throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String projectId = null;
			if (tenant.getName().equals(tenantadminDbName))
				projectId = tenantNetwork.getVim().getAdminprojectid();
			else
				projectId = tenant.getProjectid();
			Network network = new Network();
			network.setName(tenantNetwork.getNetwork_name() + "_" + projectId);
			network.setTenant_id(tenant.getProjectid());
			RestNetwork restNetwork = new RestNetwork();
			restNetwork.setNetwork(network);
			result = restClient.post(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postNetwork, map, mapper.writeValueAsString(restNetwork));
			RestNetworkId object = mapper.readValue(result, RestNetworkId.class);
			String networkId = object.getNetwork().getId();

			AllocationPools pool = new AllocationPools();
			pool.setStart(privateNetwork.getStart());
			pool.setEnd(privateNetwork.getEnd());
			List<AllocationPools> pools = new ArrayList<>();
			pools.add(pool);
			List<String> nameservers = new ArrayList<>();
			nameservers.add(privateNetwork.getDns());
			Subnet subnet = new Subnet();
			subnet.setNetwork_id(networkId);
			subnet.setCidr(privateNetwork.getCidr());
			subnet.setAllocation_pools(pools);
			subnet.setDns_nameservers(nameservers);
			subnet.setGateway_ip(privateNetwork.getGateway());
			subnet.setName(tenantNetwork.getSubnet_name() + "_" + tenant.getProjectid());
			RestSubnet restSubnet = new RestSubnet();
			restSubnet.setSubnet(subnet);
			result = restClient.post(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postSubnet, map, mapper.writeValueAsString(restSubnet));
			RestSubnetId subnetObject = mapper.readValue(result, RestSubnetId.class);
			String subnetId = subnetObject.getSubnet().getId();

			result = restClient.get(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postRouter, map);
			RestRouters routersObject = mapper.readValue(result, RestRouters.class);
			List<Router> routers = routersObject.getRouters();
			String routerId = null;
			for (Router r: routers) {
				if (r.getTenant_id().equals(utility.getUser().getTenant().getProjectid())) {
					routerId = r.getId();
					break;
				}
			}
			if (routerId == null) {
				ExternalGatewayInfo info = new ExternalGatewayInfo();
				info.setNetwork_id(physicalNetworkId);
				Router r = new Router();
				r.setName(utility.getUser().getTenant().getProject() + "_router");
				r.setExternal_gateway_info(info);
				RestRouter restRouter = new RestRouter();
				restRouter.setRouter(r);
				result = restClient.post(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postRouter, map, mapper.writeValueAsString(restRouter));
				restRouter = mapper.readValue(result, RestRouter.class);
				routerId = restRouter.getRouter().getId();
			}
			
			restClient.put(NFVConstants.HTTP + tenantNetwork.getVim().getIpaddress() + postRouter + "/" + routerId + "/add_router_interface", map, "{\"subnet_id\":\"" + subnetId + "\"}");
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
