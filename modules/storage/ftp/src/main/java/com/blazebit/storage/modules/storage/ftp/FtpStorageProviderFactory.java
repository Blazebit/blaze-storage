package com.blazebit.storage.modules.storage.ftp;

import java.net.URI;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderConfigurationElement;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.StorageProviderFactoryUriHelper;

@ApplicationScoped
public class FtpStorageProviderFactory implements StorageProviderFactory {

	private static final String SCHEME = "ftp";
	private static final StorageProviderFactoryUriHelper URI_HELPER = new StorageProviderFactoryUriHelper(SCHEME);
	
	private final StorageProviderMetamodel metamodel;
	
	public FtpStorageProviderFactory() {
		this.metamodel = new DefaultStorageProviderMetamodel(SCHEME, "FTP storage provider", "Loads from a remote FTP server", 
			new DefaultStorageProviderConfigurationElement(FtpStorage.URL_PROPERTY, "text", null, "FTP URL", "The URL including the credentials to the FTP server. Either use this property, or all the others."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.CREATE_DIRECTORY_PROPERTY, "boolean", null, "Create directory", "Whether the directory should be created if necessary."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.HOST_PROPERTY, "text", null, "Host", "The hostname of the FTP server."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.PORT_PROPERTY, "integer", null, "Port", "The port on which the FTP server listens."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.USER_PROPERTY, "text", null, "User", "The username to use for the FTP server."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.PASSWORD_PROPERTY, "password", null, "Password", "The password to use for the FTP server."),
			new DefaultStorageProviderConfigurationElement(FtpStorage.BASE_PATH_PROPERTY, "text", null, "Base path", "The path to the base directory which is used as storage.")
		);
	}

	@Override
	public StorageProviderMetamodel getMetamodel() {
		return metamodel;
	}

	@Override
	public Map<String, String> createConfiguration(URI uri) {
		return URI_HELPER.createConfiguration(uri);
	}

	@Override
	public URI createUri(Map<String, String> configuration) {
		return URI_HELPER.createUri(configuration);
	}

	@Override
	public StorageProvider createStorageProvider(Map<String, ? extends Object> properties) {
		String ftpUrl = normalize(properties);
		
		try {
			FileSystemManager fsManager = VFS.getManager();
			FileSystemOptions opts = new FileSystemOptions();
			FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
			FileObject fileObject = fsManager.resolveFile(ftpUrl, opts);
			
			boolean createDirectory = getBoolean(properties, FtpStorage.CREATE_DIRECTORY_PROPERTY, false);
			
			fsManager.getFilesCache().clear(fileObject.getFileSystem());
			if (!fileObject.exists()) {
				if (createDirectory) {
					fileObject.createFolder();
				} else {
					throw new StorageException("The given base path does not resolve to an existing directory: " + ftpUrl);
				}
			}
			
			if (fileObject.getType() != FileType.FOLDER) {
				throw new StorageException("The given base path does not resolve to an existing directory: " + ftpUrl);
			}
			
			// TODO: Maybe try to create a little test object so we can self check if writing is allowed
			boolean supportsWriting = true;
			
			return new FtpStorageProvider(fileObject, supportsWriting);
		} catch (FileSystemException ex) {
			throw new StorageException("Could not connect to the ftp storage at: " + ftpUrl, ex);
		}
	}
	
	private String normalize(Map<String, ? extends Object> properties) {
		Object urlValue = properties.get(FtpStorage.URL_PROPERTY);
		String url = null;
		
		if (urlValue == null) {
			url = null;
		} else if (!(urlValue instanceof String)) {
			throw new StorageException("Invalid url is set! String expected but got: " + urlValue);
		} else {
			url = (String) urlValue;
		}
		
		// If the properties were provided separately, we have to build the url
		if (url == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("ftp://");
			sb.append(getString(properties, FtpStorage.USER_PROPERTY, "anonymous"));
			sb.append(':');
			// use an actual email because some FTP servers expect the anonymous user to have a password of this format
			sb.append(getString(properties, FtpStorage.PASSWORD_PROPERTY, "anonymous@domain.com"));
			sb.append('@');
			sb.append(getString(properties, FtpStorage.HOST_PROPERTY));
			sb.append(':');
			sb.append(getInteger(properties, FtpStorage.PORT_PROPERTY));
			
			String basePath = getString(properties, FtpStorage.BASE_PATH_PROPERTY);
			basePath = basePath.trim();
			
			if (basePath.isEmpty()) {
				sb.append('/');
			} else {
				if (basePath.charAt(0) != '/') {
					sb.append('/');
				}
				
				sb.append(basePath);
			}
			
			url = sb.toString();
		}
		
		return url;
	}
	
	private boolean getBoolean(Map<String, ? extends Object> properties, String propertyName) {
		String s = getString(properties, propertyName, null, false);
		return Boolean.valueOf(s);
	}
	
	private boolean getBoolean(Map<String, ? extends Object> properties, String propertyName, boolean defaultValue) {
		String s = getString(properties, propertyName, null, true);
		
		if (s == null) {
			return defaultValue;
		}
		
		return Boolean.valueOf(s);
	}
	
	private int getInteger(Map<String, ? extends Object> properties, String propertyName) {
		String s = getString(properties, propertyName, null, false);
		return Integer.valueOf(s);
	}
	
	private String getString(Map<String, ? extends Object> properties, String propertyName) {
		return getString(properties, propertyName, null, false);
	}
	
	private String getString(Map<String, ? extends Object> properties, String propertyName, String defaultValue) {
		return getString(properties, propertyName, defaultValue, true);
	}
	
	private String getString(Map<String, ? extends Object> properties, String propertyName, String defaultValue, boolean useDefault) {
		Object value = properties.get(propertyName);
		
		if (value == null) {
			if (useDefault) {
				return defaultValue;
			} else {
				throw new StorageException("The property '" + propertyName + "' is not set!");
			}
		} else if (!(value instanceof String)) {
			throw new StorageException("Invalid value for property '" + propertyName + "' is set! String expected but got: " + value);
		}
		
		return (String) value;
	}

}
