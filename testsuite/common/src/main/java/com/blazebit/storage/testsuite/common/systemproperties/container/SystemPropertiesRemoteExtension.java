package com.blazebit.storage.testsuite.common.systemproperties.container;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;

public class SystemPropertiesRemoteExtension implements RemoteLoadableExtension {
	@Override
	public void register(ExtensionBuilder builder) {
		builder.observer(SystemPropertiesLoader.class);
	}

}