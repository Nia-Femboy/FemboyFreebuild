package org.noktron.femboyFreebuild.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Block;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import java.util.*;
import java.util.stream.Collectors;

public class ClaimInteractionListener implements Listener {
    
    private final ClaimService claimService;
    
    public ClaimInteractionListener(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        if (!(e.getEntity() instanceof Mob mob))
            return;
        if (!(mob.getTarget() instanceof Player target))
            return;
        UUID targetUuid = target.getUniqueId();
        Map<Chunk, List<Block>> chunkBlocks = e.blockList().stream().collect(Collectors.groupingBy(block -> Chunk.convert(block.getChunk())));
        for (Map.Entry<Chunk, List<Block>> entry : chunkBlocks.entrySet()) {
            Chunk chunk = entry.getKey();
            Optional<UUID> ownerUuid = claimService.getOwnerOfClaimedChunk(chunk);
            if (ownerUuid.isEmpty())
                continue;
            // Protect the blocks if the chunk is claimed by someone other than the target
            if (!ownerUuid.get().equals(targetUuid))
                e.blockList().removeAll(entry.getValue());
        }
    }
    
    @EventHandler
    public void onInteraction(PlayerInteractEvent e) {
        if (e.getInteractionPoint() == null)
            return;
        Chunk chunk = Chunk.convert(e.getInteractionPoint().getChunk());
        if (claimService.hasAccessToChunk(e.getPlayer(), chunk))
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>This chunk is claimed by someone else!"));
    }
    
}
