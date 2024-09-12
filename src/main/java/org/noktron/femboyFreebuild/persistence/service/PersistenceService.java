package org.noktron.femboyFreebuild.persistence.service;

import me.monst.pluginutil.persistence.ConnectionConsumer;
import me.monst.pluginutil.persistence.ConnectionFunction;
import me.monst.pluginutil.persistence.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public abstract class PersistenceService {
    
    private final ConnectionProvider db;
    
    public PersistenceService(ConnectionProvider db) {
        this.db = db;
    }
    
    public abstract void createTables();
    
    void execute(ConnectionConsumer writeAction) {
        try (Connection con = db.getConnection()) {
            writeAction.accept(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    <T> Optional<T> executeOpt(ConnectionFunction<T> query) {
        try (Connection con = db.getConnection()) {
            return Optional.ofNullable(query.apply(con));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
}
