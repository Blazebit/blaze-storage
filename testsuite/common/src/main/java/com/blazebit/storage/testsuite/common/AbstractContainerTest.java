package com.blazebit.storage.testsuite.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.storage.core.config.api.persistence.MasterOnly;
import com.blazebit.storage.testsuite.common.context.TestUserContext;
import com.blazebit.storage.testsuite.common.storages.temporary.TestStorageProviderFactory;

@RunWith(DatabaseAwareArquillianRunner.class)
public class AbstractContainerTest {
	
	@Inject
    @MasterOnly
    protected EntityManager em;
    @Inject
    protected DataService dataService;
    @Inject
    @MasterOnly
    protected CriteriaBuilderFactory cbf; 
    
    @Inject
    protected TestUserContext userContext;
    @Inject
    protected TestStorageProviderFactory testStorage;
    
    @Before
    public void resetUserContext() {
    	userContext.setAccountId(null);
    	userContext.setAccountKey(null);
    	userContext.setAccountRoles(new HashSet<String>(0));
    	userContext.setLocale(Locale.ENGLISH);
    	userContext.setLocales(Arrays.asList(Locale.ENGLISH));
    	
    	testStorage.setBasePath(null);
    }

    protected static WebArchive createBaseDeployment() {
    	boolean offline = true;
    	PomEquippedResolveStage resolver = Maven.configureResolver().workOffline(offline).loadPomFromFile("pom.xml");
    	
    	return ShrinkWrap.create(WebArchive.class)
			.addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
			.addAsWebInfResource("META-INF/beans.xml", "beans.xml")
			
			/* persistence */
			.addAsWebInfResource("test-ds.xml")
			.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
			.addAsLibraries(resolveEjbs(resolver.importDependencies(ScopeType.TEST, ScopeType.COMPILE, ScopeType.RUNTIME)))
			;
    }
    
    private static Archive<?>[] resolveEjbs(PomEquippedResolveStage dependencies) {
    	List<Archive<?>> archives = new ArrayList<Archive<?>>();

    	MavenResolvedArtifact[] artifacts = dependencies.resolve().withTransitivity().asResolvedArtifact();

		for (MavenResolvedArtifact artifact : artifacts) {
			if (PackagingType.EJB.equals(artifact.getCoordinate().getType())) {
				String originalName = artifact.asFile().getName();
				String name = originalName.replace(".ejb", ".jar");
				JavaArchive ejbArchive = ShrinkWrap.createFromZipFile(JavaArchive.class, artifact.asFile());
				archives.add(ShrinkWrap.create(JavaArchive.class, name).merge(ejbArchive));
			} else {
				archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, artifact.asFile()));
			}
		}
    	
    	return archives.toArray(new Archive<?>[archives.size()]);
	}
}