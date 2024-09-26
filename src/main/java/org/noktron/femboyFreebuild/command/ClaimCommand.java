package org.noktron.femboyFreebuild.command;

import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.Permissions;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.lang.LanguageConfiguration;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

public class ClaimCommand implements Command {
    
    private final LanguageConfiguration lang;
    private final ClaimService claimService;
    
    public ClaimCommand(LanguageConfiguration lang, ClaimService claimService) {
        this.lang = lang;
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
        Chunk chunk = Chunk.fromBukkit(player.getLocation().getChunk());
        if (claimService.getOwnerOfClaimedChunk(chunk).isPresent())
            Command.fail(lang.chunkAlreadyClaimed.get().toString());
        int currentClaims = claimService.countClaimedChunksByOwner(player);
        if (Permissions.CHUNK_LIMIT.isLimitReached(player, currentClaims))
            Command.fail(lang.maxClaimedChunksReached.get().toString());
        claimService.claimChunk(player, chunk);
        player.sendMessage(lang.chunkClaimed.get());
    }
    
}
