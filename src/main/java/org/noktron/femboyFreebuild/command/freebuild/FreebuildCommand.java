package org.noktron.femboyFreebuild.command.freebuild;

import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.CommandDelegator;
import org.noktron.femboyFreebuild.config.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FreebuildCommand implements CommandDelegator {
    
    private final Map<String, Command> subCommands = new LinkedHashMap<>();
    
    public FreebuildCommand(Configuration configuration) {
        Stream.of(
                new FreebuildConfigCommand(configuration)
        ).forEach(subCommand -> subCommands.put(subCommand.getName(), subCommand));
    }
    
    @Override
    public String getName() {
        return "freebuild";
    }
    
    @Override
    public String getDescription() {
        return "Configure freebuild settings";
    }
    
    @Override
    public Map<String, Command> getSubCommands() {
        return subCommands;
    }
    
}
