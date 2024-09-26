package org.noktron.femboyFreebuild.config;

import me.monst.pluginutil.configuration.ConfigurationValue;
import org.noktron.femboyFreebuild.FemboyFreebuildPlugin;
import org.noktron.femboyFreebuild.config.parse.PathTransformer;
import org.noktron.femboyFreebuild.lang.LanguageConfiguration;

import java.nio.file.Path;

/**
 * This class represents the language file configuration value, e.g. "language-file: en.yml" in "config.yml".
 * It also holds the actual language configuration object, which is used to access the language strings.
 */
public class LanguageFile extends ConfigurationValue<Path> {
    
    public final LanguageConfiguration languageConfig;
    
    public LanguageFile(FemboyFreebuildPlugin plugin) {
        super("language-file", Path.of("en.yml"), new PathTransformer().ofType(".yml", ".yaml"));
        this.languageConfig = new LanguageConfiguration(plugin);
    }
    
    @Override
    protected void afterSet() {
        this.languageConfig.switchToFile(this.get());
    }
    
}
