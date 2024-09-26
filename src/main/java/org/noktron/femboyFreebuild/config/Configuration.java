package org.noktron.femboyFreebuild.config;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.YamlFile;
import org.noktron.femboyFreebuild.FemboyFreebuildPlugin;
import org.noktron.femboyFreebuild.config.database.DatabaseConfiguration;

public class Configuration extends ConfigurationBranch {
    
    public final LanguageFile languageFile;
    public final DatabaseConfiguration databaseConfig;
    
    private final YamlFile file;
    
    public Configuration(FemboyFreebuildPlugin plugin) {
        super(null);
        this.file = new YamlFile(plugin, "config.yml"); // Create config.yml file
        this.languageFile = addChild(new LanguageFile(plugin));
        this.databaseConfig = addChild(new DatabaseConfiguration());
        reload();
    }
    
    public void reload() {
        load();
        save();
    }
    
    public void load() {
        feed(file.load());
    }
    
    public void save() {
        file.save(getAsYaml());
    }
    
}
