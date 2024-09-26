package org.noktron.femboyFreebuild.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.lang.LanguageConfiguration;

public class Commands {

    public static Player playerOnly(CommandSender sender, LanguageConfiguration lang) throws TranslatableCommandFailedException {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            throw new TranslatableCommandFailedException(lang.playerCommandOnly.get());
        }
    }
    
    public static <T> T fail(Component message) throws TranslatableCommandFailedException {
        throw new TranslatableCommandFailedException(message);
    }

}
