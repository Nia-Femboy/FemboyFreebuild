package org.noktron.femboyFreebuild.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.monst.pluginutil.persistence.Database;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class FemboyFreebuildDatabase implements Database {
    
    private HikariDataSource dataSource;
    private final ClaimService claimService;
    
    public FemboyFreebuildDatabase() {
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
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate("SHUTDOWN");
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
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find MariaDB JDBC Driver!", e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb:plugins/FemboyFreebuild/database/claims");
        config.setConnectionTestQuery("CALL NOW()");
        return new HikariDataSource(config);
    }
    
    public ClaimService getClaimService() {
        return claimService;
    }
    
}
