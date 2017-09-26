package com.nfv.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NFVResponseHandler implements ResponseHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(NFVResponseHandler.class);

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity == null) {
        	logger.debug("RestClient.result:null");
        } else {
        	result = EntityUtils.toString(entity);
        	logger.debug("RestClient.result:" + result);
        }
        
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
        	return result;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
	}

}
