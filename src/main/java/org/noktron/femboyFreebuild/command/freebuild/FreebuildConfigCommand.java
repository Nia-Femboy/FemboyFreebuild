package org.noktron.femboyFreebuild.command.freebuild;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import me.monst.pluginutil.configuration.ConfigurationNode;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import org.bukkit.command.CommandSender;
import org.noktron.femboyFreebuild.command.Commands;
import org.noktron.femboyFreebuild.config.Configuration;
import org.noktron.femboyFreebuild.lang.LanguageConfiguration;

import java.util.ListIterator;

public class FreebuildConfigCommand implements Command {
    
    private final Configuration configuration;
    private final LanguageConfiguration lang;
    
    public FreebuildConfigCommand(Configuration configuration) {
        this.configuration = configuration;
        this.lang = configuration.languageFile.languageConfig;
    }
    
    @Override
    public String getName() {
        return "config";
    }
    
    @Override
    public String getDescription() {
        return "Configure freebuild settings";
    }
    
    @Override
    public String getUsage() {
        return "/freebuild config";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        configuration.load();
        ListIterator<String> iterator = args.asList().listIterator();
        ConfigurationNode targetNode = configuration.deepSearch(iterator);
        String path = args.between(0, iterator.nextIndex()).join(".");
        
        if (!(targetNode instanceof ConfigurationValue<?>))
            Commands.fail(lang.invalidConfigPath.get()); // TODO: Format message with path
        ConfigurationValue<?> configValue = (ConfigurationValue<?>) targetNode;
        
        String oldValue = configValue.get().toString();
        
        String input = String.join(" ", args.between(iterator.nextIndex(), args.size()));
        
        try {
            configValue.feed(input.isEmpty() ? null : input);
        } catch (ArgumentParseException e) {
            Command.fail(e.getMessage());
        }
        
        String newValue = configValue.get().toString();
        sender.sendMessage(lang.pluginConfigured.get()); // TODO: Format message with path, old value, and new value
        configuration.save();
    }
    
}
