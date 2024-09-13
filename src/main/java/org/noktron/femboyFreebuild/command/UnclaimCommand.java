package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import java.util.Optional;
import java.util.UUID;

public class UnclaimCommand implements Command {
    
    private final ClaimService claimService;
    
    public UnclaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }
    
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
        Player player = Command.playerOnly(commandSender);
        Chunk chunk = Chunk.convert(player.getLocation().getChunk());
        Optional<UUID> ownerUuid = claimService.getOwnerOfClaimedChunk(chunk);
        if (ownerUuid.isEmpty())
            Command.fail("You have not claimed this chunk.");
        if (!ownerUuid.get().equals(player.getUniqueId()))
            Command.fail("You do not own this chunk.");
        claimService.unclaimChunk(chunk);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Chunk unclaimed!"));
    }
    
}
