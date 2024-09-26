package org.noktron.femboyFreebuild.config.database;

import me.monst.pluginutil.configuration.ConfigurationValue;

public class DatabaseName extends ConfigurationValue<String> {
    
    public DatabaseName() {
        super("name", "freebuild", s -> s);
    }
    
}
