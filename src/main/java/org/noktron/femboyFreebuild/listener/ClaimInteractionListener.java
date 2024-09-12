package org.noktron.femboyFreebuild.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.noktron.femboyFreebuild.persistence.service.ClaimService;

import java.util.UUID;

public class ClaimInteractionListener implements Listener {
    
    private final ClaimService claimService;
    
    public ClaimInteractionListener(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    @EventHandler
    public void onInteraction(PlayerInteractEvent e) {
        UUID ownerUuid = claimService.getOwnerOfClaimedChunk(e.getClickedBlock().getChunk());
        if (ownerUuid == null)
            return;
        if (ownerUuid.equals(e.getPlayer().getUniqueId()))
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>This chunk is claimed by someone else!"));
    }
    
}
