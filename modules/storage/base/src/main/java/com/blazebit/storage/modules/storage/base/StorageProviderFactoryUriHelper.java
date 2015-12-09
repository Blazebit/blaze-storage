package com.blazebit.storage.modules.storage.base;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class StorageProviderFactoryUriHelper {

	protected final String scheme;
	protected final String defaultPath;

	public StorageProviderFactoryUriHelper(String scheme) {
		this(scheme, "/");
	}
	
	public StorageProviderFactoryUriHelper(String scheme, String defaultPath) {
		this.scheme = scheme;
		this.defaultPath = defaultPath;
	}

	public URI createUri(Map<String, String> configuration) {
		StringBuilder sb = new StringBuilder();
		sb.append(scheme);
		sb.append(':');
		sb.append(getPath(configuration));
		
		boolean first = true;

		try {
			for (Iterator<Map.Entry<String, String>> iter = configuration.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, String> entry = iter.next();
				
				if (shouldAppendParameter(entry.getKey())) {
					if (first) {
						sb.append('?');
						first = false;
					} else {
						sb.append('&');
					}
					
					sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
					sb.append('=');
					sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// yeah... as if utf-8 will ever not be available on a system...
		}
		
		return URI.create(sb.toString());
	}
	
	public Map<String, String> createConfiguration(URI uri) {
		Map<String, String> config = new LinkedHashMap<String, String>();
		addPath(config, uri.getPath());
		
		if (uri.getQuery() != null) {
			String[] pairs = uri.getQuery().split("&");
	
			try {
				for (String pair : pairs) {
					int idx = pair.indexOf("=");
					String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
					
					if (shouldAppendParameter(key)) {
					    String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
					    
					    if (config.put(key, value) != null) {
					    	throw new IllegalArgumentException("Duplicate configuration parameter '" + key + "'!");
					    }
					}
				}
			} catch (UnsupportedEncodingException e) {
				// yeah... as if utf-8 will ever not be available on a system...
			}
		}
		
		return config;
	}
	
	protected String getPath(Map<String, String> configuration) {
		return defaultPath;
	}
	
	protected void addPath(Map<String, String> configuration, String path) {
	}
	
	protected boolean shouldAppendParameter(String name) {
		return true;
	}
}
