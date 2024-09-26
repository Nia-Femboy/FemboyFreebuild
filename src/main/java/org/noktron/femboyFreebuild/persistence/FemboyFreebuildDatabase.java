package org.noktron.femboyFreebuild.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.monst.pluginutil.persistence.Database;
import org.noktron.femboyFreebuild.config.database.DatabaseConfiguration;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class FemboyFreebuildDatabase implements Database {
    
    private final DatabaseConfiguration config;
    private HikariDataSource dataSource;
    public final ClaimService claimService;
    
    public FemboyFreebuildDatabase(DatabaseConfiguration config) {
        this.config = config;
        this.dataSource = createDataSource();
        this.claimService = new ClaimService(this);
        createTables();
    }
    
    @Override
    public void reload() {
        shutdown();
        dataSource = createDataSource();
        createTables();
    }
    
    public void createTables() {
        claimService.createTables();
    }
    
    @Override
    public void shutdown() {
        if (dataSource == null)
            return;
        try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
            stmt.executeUpdate(config.getShutdownQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataSource.close();
        dataSource = null;
    }
    
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
    
    private HikariDataSource createDataSource() {
        try {
            Class.forName(config.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find MariaDB JDBC Driver!", e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        config.getUsername().ifPresent(hikariConfig::setUsername);
        config.getPassword().ifPresent(hikariConfig::setPassword);
        hikariConfig.setConnectionTestQuery(config.getConnectionTestQuery());
        return new HikariDataSource(hikariConfig);
    }
    
}
