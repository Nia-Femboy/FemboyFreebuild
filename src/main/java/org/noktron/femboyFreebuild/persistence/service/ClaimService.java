package org.noktron.femboyFreebuild.persistence.service;

import me.monst.pluginutil.persistence.ConnectionProvider;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.domain.Claim;
import org.noktron.femboyFreebuild.persistence.repository.ClaimRepository;

import java.util.*;

public class ClaimService extends PersistenceService {
    
    private final ClaimRepository repository;
    private final HashMap<Chunk, UUID> claimOwnerCache; // In-memory cache for faster permission checks
    
    public ClaimService(ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.repository = new ClaimRepository();
        this.claimOwnerCache = new HashMap<>();
    }
    
    @Override
    public void createTables() {
        execute(con -> {
            repository.createTable(con);
            repository.findAll(con).forEach(
                    claim -> claim.chunks().forEach(
                            chunk -> this.claimOwnerCache.put(chunk, claim.ownerUuid())
                    )
            );
        });
    }
    
    public Optional<Claim> getClaimByChunk(Chunk chunk) {
        return fetch(con -> repository.findClaimByChunk(con, chunk));
    }
    
    /**
     * Claims the chunk for the player. If the chunk is adjacent to an existing claim owned by the player, the chunks
     * are merged into a single claim. If the chunk is not adjacent to any existing claims, a new claim is created.
     *
     * @param player The player claiming the chunk
     * @param chunk  The chunk to claim
     */
    public void claimChunk(Player player, Chunk chunk) {
        UUID ownerUuid = player.getUniqueId();
        transact(con -> {
            Iterator<Claim> adjacentClaims = repository.findAdjacentClaimsOwnedBy(con, chunk, ownerUuid).iterator();
            Claim mergedClaim; // Select an adjacent claim to merge into, or create a new one if no adjacent claims exist
            if (adjacentClaims.hasNext()) {
                mergedClaim = adjacentClaims.next(); // The chunks of this claim do not need to be updated
            } else {
                mergedClaim = new Claim(UUID.randomUUID(), ownerUuid, new HashSet<>(List.of(chunk)));
                repository.save(con, mergedClaim);
            }
            while (adjacentClaims.hasNext()) {
                mergedClaim.chunks().addAll(adjacentClaims.next().chunks());
            }
            repository.updateOwnershipOfChunks(con, mergedClaim.chunks(), mergedClaim.claimUuid());
            claimOwnerCache.put(chunk, ownerUuid);
        });
    }
    
    public void unclaimChunk(Chunk chunk) {
        execute(con -> repository.deleteChunk(con, chunk));
        claimOwnerCache.remove(chunk);
    }
    
    /**
     * Checks if the player has access to the chunk. The player has access if the chunk is not claimed or if the player
     * owns the claim that the chunk is part of.
     * @param player The player to check access for
     * @param chunk The chunk to check access for
     * @return True if the player has access to the chunk, false otherwise
     */
    public boolean hasAccessToChunk(Player player, Chunk chunk) {
        return getOwnerOfClaimedChunk(chunk).map(uuid -> uuid.equals(player.getUniqueId())).orElse(true);
    }
    
    public Optional<UUID> getOwnerOfClaimedChunk(Chunk chunk) {
        return Optional.ofNullable(claimOwnerCache.get(chunk));
    }
    
    public int countClaimedChunksByOwner(Player player) {
        UUID ownerUuid = player.getUniqueId();
        return fetch(con -> repository.countClaimedChunksByOwner(con, ownerUuid));
    }
    
}
