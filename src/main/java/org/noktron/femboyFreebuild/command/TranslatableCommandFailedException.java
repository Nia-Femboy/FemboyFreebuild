package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class TranslatableCommandFailedException extends CommandExecutionException {
    
    private final Component message;
    
    public TranslatableCommandFailedException(Component message) {
        this.message = message;
    }
    
    @Override
    public void handle(CommandSender commandSender, Arguments arguments) {
        commandSender.sendMessage(message);
    }
    
}
