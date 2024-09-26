package org.noktron.femboyFreebuild.config.database;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.OptionalTransformer;

import java.util.Optional;

public class DatabasePassword extends ConfigurationValue<Optional<String>> {
    
    public DatabasePassword() {
        super("password", Optional.empty(), new OptionalTransformer<>(s -> s));
    }
    
}
