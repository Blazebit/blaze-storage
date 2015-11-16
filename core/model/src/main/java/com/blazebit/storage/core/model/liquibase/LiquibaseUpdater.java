package com.blazebit.storage.core.model.liquibase;

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.logging.LogFactory;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.servicelocator.ServiceLocator;

public class LiquibaseUpdater {
	public static final String FIRST_VERSION = "1.0.0-1";
    public static final String LAST_VERSION = "1.0.1.Final";
    
    private static final Logger logger = Logger.getLogger(LiquibaseUpdater.class.getName());
    private static final String CHANGELOG = "META-INF/changelog/changelog-master.xml";

    public String getCurrentVersionSql() {
        return "SELECT ID from DATABASECHANGELOG ORDER BY DATEEXECUTED DESC LIMIT 1";
    }

    public void update(Connection connection) {
        logger.fine("Starting database update");
        try {
            Liquibase liquibase = getLiquibase(connection);
            List<ChangeSet> changeSets = liquibase.listUnrunChangeSets((Contexts) null);
            if (!changeSets.isEmpty()) {
                if (changeSets.get(0).getId().equals(FIRST_VERSION)) {
                    logger.info("Initializing database schema");
                } else {
                    if (logger.isLoggable(Level.FINE)) {
                        List<RanChangeSet> ranChangeSets = liquibase.getDatabase().getRanChangeSetList();
                        final String msg = MessageFormat.format("Updating database from {0} to {1}", ranChangeSets.get(ranChangeSets.size() - 1).getId(), changeSets.get(changeSets.size() - 1).getId());
                        logger.fine(msg);
                    } else {
                        logger.info("Updating database");
                    }
                }
                liquibase.update((Contexts) null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update database", e);
        }
        logger.fine("Completed database update");
    }

    public void validate(Connection connection) {
        try {
            Liquibase liquibase = getLiquibase(connection);
            liquibase.validate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate database", e);
        }
    }

    private Liquibase getLiquibase(Connection connection) throws Exception {
        ServiceLocator sl = ServiceLocator.getInstance();
        if (!System.getProperties().containsKey("liquibase.scan.packages")) {
            if (sl.getPackages().remove("liquibase.core")) {
                sl.addPackageToScan("liquibase.core.xml");
            }
            if (sl.getPackages().remove("liquibase.parser")) {
                sl.addPackageToScan("liquibase.parser.core.xml");
            }
            if (sl.getPackages().remove("liquibase.serializer")) {
                sl.addPackageToScan("liquibase.serializer.core.xml");
            }
            sl.getPackages().remove("liquibase.ext");
            sl.getPackages().remove("liquibase.sdk");
        }
        LogFactory.setInstance(new LogWrapper());
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        return new Liquibase(CHANGELOG, new ClassLoaderResourceAccessor(getClass().getClassLoader()), database);
    }

    private static class LogWrapper extends LogFactory {
        private liquibase.logging.Logger logger = new liquibase.logging.Logger() {
            @Override
            public void setName(String name) {
            }

            @Override
            public void setLogLevel(String level) {
            }

            @Override
            public void setLogLevel(LogLevel level) {
            }

            @Override
            public void setLogLevel(String logLevel, String logFile) {
            }

            @Override
            public void severe(String message) {
                LiquibaseUpdater.logger.severe(message);
            }

            @Override
            public void severe(String message, Throwable e) {
                LiquibaseUpdater.logger.log(Level.SEVERE, message, e);
            }

            @Override
            public void warning(String message) {
                LiquibaseUpdater.logger.warning(message);
            }

            @Override
            public void warning(String message, Throwable e) {
                LiquibaseUpdater.logger.log(Level.WARNING, message, e);
            }

            @Override
            public void info(String message) {
                LiquibaseUpdater.logger.fine(message);
            }

            @Override
            public void info(String message, Throwable e) {
                LiquibaseUpdater.logger.log(Level.FINE, message, e);
            }

            @Override
            public void debug(String message) {
                LiquibaseUpdater.logger.finest(message);
            }

            @Override
            public LogLevel getLogLevel() {
                if (LiquibaseUpdater.logger.isLoggable(Level.FINEST)) {
                    return LogLevel.DEBUG;
                } else if (LiquibaseUpdater.logger.isLoggable(Level.FINE)) {
                    return LogLevel.INFO;
                } else {
                    return LogLevel.WARNING;
                }
            }

            @Override
            public void debug(String message, Throwable e) {
                LiquibaseUpdater.logger.log(Level.FINEST, message, e);
            }

            @Override
            public void setChangeLog(DatabaseChangeLog databaseChangeLog) {
            }

            @Override
            public void setChangeSet(ChangeSet changeSet) {
            }

            @Override
            public int getPriority() {
                return 0;
            }
        };

        @Override
        public liquibase.logging.Logger getLog(String name) {
            return logger;
        }

        @Override
        public liquibase.logging.Logger getLog() {
            return logger;
        }
    }
}
