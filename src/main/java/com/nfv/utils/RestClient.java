package com.nfv.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Tenant;
import com.nfv.entity.Vim;
import com.nfv.error.NFVException;
import com.nfv.model.Auth;
import com.nfv.model.Domain;
import com.nfv.model.Identity;
import com.nfv.model.Password;
import com.nfv.model.Project;
import com.nfv.model.RestLogin;
import com.nfv.model.RestOSAuthentication;
import com.nfv.model.Scope;
import com.nfv.model.User;

@SuppressWarnings("deprecation")
@Component
public class RestClient {
	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    
	@Value("${post.openstack.login.uri}")
	String loginURI;
	
	@Value("${content.type.key}")
	String contentTypeKey;
	
	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${subject.token.key}")
	String tokenKey;

	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	NFVUtility utility;
	
	public final String get(String uri, Map<String, String> map) throws Exception {
		logger.debug("RestClient.get.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(uri);
            if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpget.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.get.header:" + entry.getKey() + ":" + entry.getValue());
	            }
            }
            return httpclient.execute(httpget, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }

	public final String post(String uri, Map<String, String> map, String body) throws Exception {
		logger.debug("RestClient.post.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPost httpPost = new HttpPost(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPost.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.post.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body) {
            	logger.debug("RestClient.post.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPost.setEntity(entity);
            }
            return httpclient.execute(httpPost, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }
	
	public final String put(String uri, Map<String, String> map, String body) throws Exception {
		logger.debug("RestClient.put.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPut httpPut = new HttpPut(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPut.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.put.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body) {
            	logger.debug("RestClient.put.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPut.setEntity(entity);
            }
            return httpclient.execute(httpPut, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }
	
	public final String put(String uri, Map<String, String> map) throws Exception {
		logger.debug("RestClient.put.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPut httpPut = new HttpPut(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPut.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.put.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            return httpclient.execute(httpPut, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }
	
	public final String put(String uri, Map<String, String> map, byte[] bytes) throws Exception {
		logger.debug("RestClient.put.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPut httpPut = new HttpPut(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPut.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.put.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != bytes) {
            	HttpEntity entity = new ByteArrayEntity(bytes);
	            httpPut.setEntity(entity);
            }
            return httpclient.execute(httpPut, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }
	
	public final String postVUIC(String uri, Map<String, String> map, String body) throws Exception {
		logger.debug("RestClient.postVUIC.uri:" + uri);
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
        	HttpPost httpPost = new HttpPost(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPost.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.postVUIC.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body) {
            	logger.debug("RestClient.postVUIC.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPost.setEntity(entity);
            }
            return httpclient.execute(httpPost, new NFVResponseHandler());
        } finally {
            httpclient.close();
        }
    }
	
	public final Header[] postHeader(String uri, Map<String, String> map, String body) throws Exception {
		logger.debug("RestClient.postHeader.uri:" + uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPost httpPost = new HttpPost(uri);
        	if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpPost.setHeader(entry.getKey(), entry.getValue());
	            	logger.debug("RestClient.postHeader.header:" + entry.getKey() + ":" + entry.getValue());
	            }
        	}
            if (null != body) {
            	logger.debug("RestClient.postHeader.body:" + body);
	            StringEntity entity = new StringEntity(body);
	            httpPost.setEntity(entity);
            }
            ResponseHandler<Header[]> responseHandler = new ResponseHandler<Header[]>() {
                @Override
                public Header[] handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        return response.getAllHeaders();
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpPost, responseHandler);
        } finally {
            httpclient.close();
        }
    }
	
	public String getToken(Tenant tenant, Vim vim) throws NFVException {
		try {
			String projectId = null;
			if (tenant.getName().equals(tenantadminDbName))
				projectId = vim.getAdminprojectid();
			else
				projectId = tenant.getProjectid();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(contentTypeKey, contentTypeValue);
			Domain domain = new Domain();
			User user = new User();
			user.setDomain(domain);
			user.setName(tenant.getProjectusername());
			user.setPassword(tenant.getProjectpassword());
			Password password = new Password();
			password.setUser(user);
			Identity identity = new Identity();
			identity.setPassword(password);
			Project project = new Project();
			project.setId(projectId);
			Scope scope = new Scope();
			scope.setProject(project);
			Auth auth = new Auth();
			auth.setIdentity(identity);
			auth.setScope(scope);
			RestLogin restLogin = new RestLogin();
			restLogin.setAuth(auth);
			
			Header[] headers = postHeader(NFVConstants.HTTP + vim.getIpaddress() + loginURI, map, mapper.writeValueAsString(restLogin));
			for (Header header: headers) {
				String name = header.getName();
				if (tokenKey.equals(name)) {
					logger.debug("RestClient.getToken.value:" + header.getValue());
					return header.getValue();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("openstack.login.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		throw new NFVException(messageSource.getMessage("openstack.token.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public String getDomainId(Tenant tenant, Vim vim) throws NFVException {
		try {
			String projectId = null;
			if (tenant.getName().equals(tenantadminDbName))
				projectId = vim.getAdminprojectid();
			else
				projectId = tenant.getProjectid();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(contentTypeKey, contentTypeValue);
			Domain domain = new Domain();
			User user = new User();
			user.setDomain(domain);
			user.setName(tenant.getProjectusername());
			user.setPassword(tenant.getProjectpassword());
			Password password = new Password();
			password.setUser(user);
			Identity identity = new Identity();
			identity.setPassword(password);
			Project project = new Project();
			project.setId(projectId);
			Scope scope = new Scope();
			scope.setProject(project);
			Auth auth = new Auth();
			auth.setIdentity(identity);
			auth.setScope(scope);
			RestLogin restLogin = new RestLogin();
			restLogin.setAuth(auth);
			
			String result = post(NFVConstants.HTTP + vim.getIpaddress() + loginURI, map, mapper.writeValueAsString(restLogin));
			RestOSAuthentication object = mapper.readValue(result, RestOSAuthentication.class);
			return object.getToken().getProject().getDomain().getId();
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("openstack.login.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public final String delete(String uri, Map<String, String> map) throws Exception {
		logger.info("RestClient.delete.uri:" + uri);
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		try {
			HttpDelete httpDelete = new HttpDelete(uri);
			httpDelete.setHeader("Accept", "application/json");
			httpDelete.setHeader("Content-type", "application/json");
			if (httpDelete!=null) {
				for (Map.Entry<String, String> entry: map.entrySet()) {
					httpDelete.addHeader(entry.getKey(), entry.getValue());
				}
			}
			return httpclient.execute(httpDelete, new NFVResponseHandler());
		} finally {
			httpclient.close();
		}
	}
}

