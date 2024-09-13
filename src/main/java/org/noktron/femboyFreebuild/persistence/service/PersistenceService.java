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
    
    <T> T fetch(ConnectionFunction<T> query) {
        try (Connection con = db.getConnection()) {
            return query.apply(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    void transact(ConnectionConsumer writeAction) {
        try (Connection con = db.getConnection()) {
            try {
                con.setAutoCommit(false);
                writeAction.accept(con);
                con.commit();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException suppressed) {
                    e.addSuppressed(suppressed);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
