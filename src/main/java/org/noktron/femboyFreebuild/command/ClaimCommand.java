package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.FemboyFreebuildPlugin;
import org.noktron.femboyFreebuild.Permissions;

public class ClaimCommand implements Command {
    
    private final FemboyFreebuildPlugin plugin;
    
    public ClaimCommand(FemboyFreebuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "claim";
    }
    
    @Override
    public String getDescription() {
        return "Claim a chunk of land";
    }
    
    @Override
    public String getUsage() {
        return "/claim";
    }
    
    @Override
    public void execute(CommandSender commandSender, Arguments arguments) throws CommandExecutionException {
        Player player = Command.playerOnly(commandSender);
        int currentClaims = 0;
        if (Permissions.CLAIM_LIMIT.isLimitReached(player, currentClaims))
            Command.fail("You have reached the maximum number of claims.");
        Command.fail("Not implemented yet");
    }
    
}
