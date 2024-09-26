package org.noktron.femboyFreebuild.config.database;

import me.monst.pluginutil.configuration.ConfigurationBranch;

import java.util.Optional;

public class DatabaseConfiguration extends ConfigurationBranch {
    
    public final DatabaseType type = addChild(new DatabaseType());
    public final DatabaseName name = addChild(new DatabaseName());
    
    public DatabaseConfiguration() {
        super("database");
    }
    
    public String getJdbcDriver() {
        return switch (type.get()) {
            case MARIADB -> "org.mariadb.jdbc.Driver";
            case SQLITE -> "org.sqlite.JDBC";
        };
    }
    
    public String getJdbcUrl() {
        return switch (type.get()) {
            case MARIADB -> "jdbc:mariadb://localhost:3306/" + name.get();
            case SQLITE -> "jdbc:sqlite:plugins/FemboyFreebuild/" + name.get() + ".db";
        };
    }
    
    public Optional<String> getUsername() {
        return switch (type.get()) {
            case MARIADB -> Optional.of("root");
            case SQLITE -> Optional.empty();
        };
    }
    
    public Optional<String> getPassword() {
        return switch (type.get()) {
            case MARIADB -> Optional.of("password");
            case SQLITE -> Optional.empty();
        };
    }
    
    public String getConnectionTestQuery() {
        return switch (type.get()) {
            case MARIADB, SQLITE -> "SELECT 1";
        };
    }
    
    public String getShutdownQuery() {
        return switch (type.get()) {
            case MARIADB, SQLITE -> "SHUTDOWN";
        };
    }
    
}
