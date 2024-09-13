package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.Permissions;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import java.util.UUID;

public class ClaimCommand implements Command {
    
    private final ClaimService claimService;
    
    public ClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
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
        Chunk chunk = Chunk.convert(player.getLocation().getChunk());
        if (claimService.getOwnerOfClaimedChunk(chunk).isPresent())
            Command.fail("This chunk is already claimed.");
        int currentClaims = claimService.countClaimedChunksByOwner(player);
        if (Permissions.CHUNK_LIMIT.isLimitReached(player, currentClaims))
            Command.fail("You have reached the maximum number of claims.");
        claimService.claimChunk(player, chunk);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Chunk claimed!"));
    }
    
}
