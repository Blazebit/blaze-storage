package com.blazebit.storage.modules.storage.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.blazebit.storage.core.api.spi.StorageProviderConfigurationElement;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;

public class DefaultStorageProviderMetamodel implements StorageProviderMetamodel {
	
	private final String scheme;
	private final String name;
	private final String description;
	private final Set<StorageProviderConfigurationElement> configurationElements;
	private final Map<String, StorageProviderConfigurationElement> configurationElementMap;

	public DefaultStorageProviderMetamodel(String scheme, String name, String description, StorageProviderConfigurationElement... configurationElements) {
		this(scheme, name, description, new LinkedHashSet<>(Arrays.asList(configurationElements)));
	}

	public DefaultStorageProviderMetamodel(String scheme, String name, String description, Set<StorageProviderConfigurationElement> configurationElements) {
		this.scheme = scheme;
		this.name = name;
		this.description = description;
		Set<StorageProviderConfigurationElement> set = new LinkedHashSet<>();
		Map<String, StorageProviderConfigurationElement> map = new HashMap<>(configurationElements.size());
		
		for (StorageProviderConfigurationElement element : configurationElements) {
			set.add(element);
			map.put(element.getKey(), element);
		}

		this.configurationElements = Collections.unmodifiableSet(set);
		this.configurationElementMap = Collections.unmodifiableMap(map);
	}

	@Override
	public String getScheme() {
		return scheme;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public StorageProviderConfigurationElement getConfigurationElement(String key) {
		return configurationElementMap.get(key);
	}

	@Override
	public Set<StorageProviderConfigurationElement> getConfigurationElements() {
		return configurationElements;
	}

}
