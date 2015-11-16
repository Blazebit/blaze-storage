package com.blazebit.storage.core.model.liquibase;

import java.sql.DriverManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import liquibase.CatalogAndSchema;
import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.change.core.DropIndexChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;

public class ChangeSetUtils {

    /**
     * Little utility method that can be used in a LiquibaseTest of a persistence project.
     * 
     * @param unitName
     * @param changeLogPath
     * @return
     * @throws Exception
     */
    public static List<ChangeSet> computeChangeSets(String unitName, String changeLogPath) throws Exception {
    	String driverClass = "org.h2.Driver";
        String oldJdbcUrl = "jdbc:h2:mem:old";
        String newJdbcUrl = "jdbc:h2:mem:new";
        String user = "test";
        String password = "test";
        
        Database newDatabase = null;
        Database oldDatabase = null;
        
        try {
            newDatabase = new H2Database();
            newDatabase.setConnection(new JdbcConnection(DriverManager.getConnection(newJdbcUrl, user, password)));
            newDatabase.dropDatabaseObjects(new CatalogAndSchema(newDatabase.getDefaultCatalogName(), newDatabase.getDefaultSchemaName()));
    
            oldDatabase = new H2Database();
            oldDatabase.setConnection(new JdbcConnection(DriverManager.getConnection(oldJdbcUrl, user, password)));
            oldDatabase.dropDatabaseObjects(new CatalogAndSchema(oldDatabase.getDefaultCatalogName(), oldDatabase.getDefaultSchemaName()));
            
            return computeChangeSets(oldDatabase, newDatabase, driverClass, user, password, unitName, changeLogPath, "");
	    } finally {	        
	        if (oldDatabase != null) {
	            oldDatabase.close();
	        }
	        
	        if (newDatabase != null) {
	            newDatabase.close();
	        }
	    }
    	
    }
    
    public static List<ChangeSet> computeChangeSets(Database oldDatabase, Database newDatabase, String newDriverClass, String newUser, String newPassword, String unitName, String changeLogPath, String contexts) throws Exception {
        EntityManagerFactory emf = null;
        
        try {
            Liquibase oldLiquibase = new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), oldDatabase);
            oldLiquibase.update(contexts);
    
            Liquibase newLiquibase = new Liquibase((String) null, null, newDatabase);
    
            // Schema export to new database
            Map<String, Object> properties = new HashMap<String, Object>();
            
            // Hibernate specific
            properties.put("hibernate.hbm2ddl.auto", "create");
            properties.put("hibernate.cache.region.factory_class", null);
            properties.put("hibernate.cache.use_query_cache", false);
            properties.put("hibernate.cache.use_second_level_cache", false);
            
            properties.put("javax.persistence.jtaDataSource", null);
            properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
            properties.put("javax.persistence.jdbc.driver", newDriverClass);
            properties.put("javax.persistence.jdbc.user", newUser);
            properties.put("javax.persistence.jdbc.password", newPassword);
            properties.put("javax.persistence.jdbc.url", newDatabase.getConnection().getURL());
            emf = Persistence.createEntityManagerFactory(unitName, properties);
    
            // Diff
            DatabaseSnapshot oldSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(oldDatabase.getDefaultSchema(), oldDatabase, new SnapshotControl(oldDatabase));
            DatabaseSnapshot newSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(newDatabase.getDefaultSchema(), newDatabase, new SnapshotControl(newDatabase));
    
            CompareControl compareControl = new CompareControl(
                new CompareControl.SchemaComparison[] { 
                    new CompareControl.SchemaComparison(
                            new CatalogAndSchema(newDatabase.getDefaultCatalogName(), newDatabase.getDefaultSchemaName()), 
                            new CatalogAndSchema(oldDatabase.getDefaultCatalogName(), oldDatabase.getDefaultSchemaName())) 
                    },
                oldSnapshot.getSnapshotControl().getTypesToInclude());
    
            DiffResult diffResult = oldLiquibase.diff(newDatabase, oldDatabase, compareControl);
            DiffToChangeLog changeLog = new DiffToChangeLog(diffResult, new DiffOutputControl(false, false, true))
            // Unfortunately there is no easy way to append conditions to change sets yet
            {
    
                @Override
                public List<ChangeSet> generateChangeSets() {
                    List<ChangeSet> oldChangeSets = super.generateChangeSets();
                    
                    if (oldChangeSets.isEmpty()) {
                    	return oldChangeSets;
                    }
                    
                    ChangeSet firstChangeSet = oldChangeSets.get(0);
                    ChangeSet newChangeSet = new ChangeSet(
                    		firstChangeSet.getId(), 
                    		firstChangeSet.getAuthor(), 
                    		firstChangeSet.isAlwaysRun(), 
                    		firstChangeSet.isRunOnChange(), 
                    		firstChangeSet.getFilePath(), 
                    		join(",", firstChangeSet.getContexts().getContexts()), 
                    		join(",", firstChangeSet.getDbmsSet()), 
                    		firstChangeSet.isRunInTransaction(), 
                    		firstChangeSet.getObjectQuotingStrategy(), 
                    		firstChangeSet.getChangeLog()
                    );
    
                    for(ChangeSet oldChangeSet : oldChangeSets) {
                        for (Change oldChange : oldChangeSet.getChanges()) {
                            if(oldChange instanceof DropIndexChange) {
                                // We ignore drop index changes because we can't reflect the creation of various indexes in hibernate
                            } else {
                            	newChangeSet.addChange(oldChange);
                            }
                        }
                    }

					if (newChangeSet.getChanges().isEmpty()) {
						return Collections.emptyList();
					}
					
                    return Collections.singletonList(newChangeSet);
                }
                
            }
            ;
    
            return changeLog.generateChangeSets();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

	protected static String join(String string, Set<String> set) {
		if (set == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		
		for (String o : set) {
			if (first) {
				first = false;
			} else {
				sb.append(string);
			}
			
			sb.append(o);
		}
		
		return sb.toString();
	}
}