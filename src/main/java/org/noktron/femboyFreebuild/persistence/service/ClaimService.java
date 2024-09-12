package org.noktron.femboyFreebuild.persistence.service;

import me.monst.pluginutil.persistence.ConnectionProvider;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.noktron.femboyFreebuild.domain.Claim;
import org.noktron.femboyFreebuild.persistence.repository.ClaimRepository;

import java.util.HashMap;
import java.util.UUID;

public class ClaimService extends PersistenceService {
    
    private final ClaimRepository repository;
    private final HashMap<Chunk, UUID> claims;
    
    public ClaimService(ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.repository = new ClaimRepository();
        this.claims = new HashMap<>();
    }
    
    @Override
    public void createTables() {
        execute(repository::createTable);
        executeOpt(repository::findAll).ifPresent(claims -> claims.forEach(claim -> this.claims.put(claim.chunk(), claim.ownerUuid())));
    }
    
    public void claimChunk(Player player, Chunk chunk) {
        UUID ownerUuid = player.getUniqueId();
        String worldName = chunk.getWorld().getName();
        int x = chunk.getX();
        int z = chunk.getZ();
        execute(con -> repository.save(con, new Claim(ownerUuid, worldName, x, z)));
        claims.put(chunk, ownerUuid);
    }
    
    public void unclaimChunk(Chunk chunk) {
        execute(con -> repository.deleteByChunk(con, chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
        claims.remove(chunk);
    }
    
    public UUID getOwnerOfClaimedChunk(Chunk chunk) {
        return claims.get(chunk);
    }
    
    public int countClaimedChunksByOwner(Player player) {
        UUID ownerUuid = player.getUniqueId();
        return executeOpt(con -> repository.countClaimedChunksByOwner(con, ownerUuid)).orElse(0);
    }
    
}
