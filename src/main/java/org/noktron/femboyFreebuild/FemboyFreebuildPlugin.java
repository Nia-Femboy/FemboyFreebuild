package org.noktron.femboyFreebuild;

import me.monst.pluginutil.command.CommandRegisterService;
import org.bukkit.plugin.java.JavaPlugin;
import org.noktron.femboyFreebuild.command.ClaimCommand;
import org.noktron.femboyFreebuild.command.UnclaimCommand;
import org.noktron.femboyFreebuild.persistence.FemboyFreebuildDatabase;

public final class FemboyFreebuildPlugin extends JavaPlugin {
    
    private FemboyFreebuildDatabase database;

    @Override
    public void onEnable() {
        this.database = new FemboyFreebuildDatabase();
        new CommandRegisterService(this).register(
                new ClaimCommand(this),
                new UnclaimCommand(this)
        );
    }

    @Override
    public void onDisable() {
        this.database.shutdown();
    }

}
