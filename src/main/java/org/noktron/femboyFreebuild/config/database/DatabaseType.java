package org.noktron.femboyFreebuild.config.database;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.transform.Transformer;

import java.util.Arrays;

public class DatabaseType extends ConfigurationValue<DatabaseType.Type> {
    
    public enum Type {
        MARIADB("org.mariadb.jdbc.Driver"),
        SQLITE("org.sqlite.JDBC");
        
        final String driver;
        
        Type(String driver) {
            this.driver = driver;
        }
    }
    
    public DatabaseType() {
        super("type", Type.MARIADB, new DatabaseTypeTransformer());
    }
    
    private static class DatabaseTypeTransformer implements Transformer<Type> {
        @Override
        public Type parse(String s) throws ArgumentParseException {
            try {
                return Type.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ArgumentParseException("Invalid database type. Valid types: " +
                        Arrays.stream(Type.values())
                                .map(t -> t.name().toLowerCase())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse(""));
            }
        }
        
        @Override
        public Object toYaml(Type value) {
            return value.name().toLowerCase();
        }
    }
    
}
