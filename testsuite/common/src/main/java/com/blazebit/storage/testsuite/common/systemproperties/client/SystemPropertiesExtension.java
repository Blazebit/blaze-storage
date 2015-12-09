package com.blazebit.storage.testsuite.common.systemproperties.client;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.core.spi.LoadableExtension.ExtensionBuilder;

public class SystemPropertiesExtension implements LoadableExtension {
	@Override
	public void register(ExtensionBuilder builder) {
		builder.service(ApplicationArchiveProcessor.class,
				ArchiveProcessor.class).service(AuxiliaryArchiveAppender.class,
				ArchiveAppender.class);
	}

}