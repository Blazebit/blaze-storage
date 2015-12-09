package com.blazebit.storage.testsuite.common.systemproperties.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.blazebit.storage.testsuite.common.systemproperties.SystemProperties;
import com.blazebit.storage.testsuite.common.systemproperties.container.SystemPropertiesRemoteExtension;

public class ArchiveAppender implements AuxiliaryArchiveAppender {
	@Override
	public Archive<?> createAuxiliaryArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "arquillian-systemproperties.jar")
				.addPackage(SystemPropertiesRemoteExtension.class.getPackage())
				.addClass(SystemProperties.class)
				.addAsServiceProvider(RemoteLoadableExtension.class,
						SystemPropertiesRemoteExtension.class);
	}

}