package org.noktron.femboyFreebuild.config.database;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.OptionalTransformer;

import java.util.Optional;

public class DatabaseUsername extends ConfigurationValue<Optional<String>> {
    
    public DatabaseUsername() {
        super("username", Optional.empty(), new OptionalTransformer<>(s -> s));
    }
    
}
