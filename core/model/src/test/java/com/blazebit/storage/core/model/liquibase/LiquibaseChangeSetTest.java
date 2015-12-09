package com.blazebit.storage.core.model.liquibase;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import liquibase.changelog.ChangeSet;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;

public class LiquibaseChangeSetTest {
	  
    @Test
    public void testChangeSets() throws Throwable {
        String unitName = "StorageTestMasterOnly";
        String changeLogPath = "META-INF/changelog/changelog-master.xml";

        List<ChangeSet> changeSets = ChangeSetUtils.computeChangeSets(unitName, changeLogPath);
        
        if (changeSets.size() > 0) {
            ChangeLogSerializer changeLogSerializer = new XMLChangeLogSerializer();
            changeLogSerializer.write(changeSets, System.out);
            System.out.flush();
            Assert.fail("There are data model changes which were not versioned!");
        }
    }
}