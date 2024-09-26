package org.noktron.femboyFreebuild;

import me.monst.pluginutil.command.CommandRegisterService;
import org.bukkit.plugin.java.JavaPlugin;
import org.noktron.femboyFreebuild.command.ClaimCommand;
import org.noktron.femboyFreebuild.command.UnclaimCommand;
import org.noktron.femboyFreebuild.command.freebuild.FreebuildCommand;
import org.noktron.femboyFreebuild.config.Configuration;
import org.noktron.femboyFreebuild.listener.ClaimInteractionListener;
import org.noktron.femboyFreebuild.persistence.FemboyFreebuildDatabase;

@SuppressWarnings("unused")
public final class FemboyFreebuildPlugin extends JavaPlugin {
    
    private Configuration config;
    private FemboyFreebuildDatabase database;

    @Override
    public void onEnable() {
        this.config = new Configuration(this);
        this.database = new FemboyFreebuildDatabase(config.databaseConfig);
        new CommandRegisterService(this).register(
                new FreebuildCommand(this.config),
                new ClaimCommand(this.config.languageFile.languageConfig, this.database.claimService),
                new UnclaimCommand(this.config.languageFile.languageConfig, this.database.claimService)
        );
        getServer().getPluginManager().registerEvents(new ClaimInteractionListener(this.database.claimService), this);
    }
    
    public Configuration config() {
        return this.config;
    }

    @Override
    public void onDisable() {
        this.database.shutdown();
    }

}
