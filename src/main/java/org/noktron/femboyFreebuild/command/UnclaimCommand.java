package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.command.CommandSender;

public class UnclaimCommand implements Command {
    
    @Override
    public String getName() {
        return "unclaim";
    }
    
    @Override
    public String getDescription() {
        return "Unclaim a claimed chunk of land";
    }
    
    @Override
    public String getUsage() {
        return "/unclaim";
    }
    
    @Override
    public void execute(CommandSender commandSender, Arguments arguments) throws CommandExecutionException {
        Command.fail("Not implemented yet");
    }
    
}
