package org.noktron.femboyFreebuild;

import me.monst.pluginutil.command.CommandRegisterService;
import org.bukkit.plugin.java.JavaPlugin;
import org.noktron.femboyFreebuild.command.ClaimCommand;
import org.noktron.femboyFreebuild.command.UnclaimCommand;
import org.noktron.femboyFreebuild.listener.ClaimInteractionListener;
import org.noktron.femboyFreebuild.persistence.FemboyFreebuildDatabase;

@SuppressWarnings("unused")
public final class FemboyFreebuildPlugin extends JavaPlugin {
    
    private FemboyFreebuildDatabase database;

    @Override
    public void onEnable() {
        this.database = new FemboyFreebuildDatabase();
        new CommandRegisterService(this).register(
                new ClaimCommand(this.database.getClaimService()),
                new UnclaimCommand(this.database.getClaimService())
        );
        getServer().getPluginManager().registerEvents(new ClaimInteractionListener(this.database.getClaimService()), this);
    }

    @Override
    public void onDisable() {
        this.database.shutdown();
    }

}
