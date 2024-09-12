package org.noktron.femboyFreebuild.domain;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.UUID;

/**
 * Represents a claim of a chunk of land by a player.
 * This is the domain model for a claim and reflects the structure of the claim table in the database.
 *
 * @param ownerUuid The UUID of the player who owns the claim
 * @param worldName The name of the world the chunk is in
 * @param x         The x-coordinate of the chunk
 * @param z         The z-coordinate of the chunk
 */
public record Claim(UUID ownerUuid, String worldName, int x, int z) {
    
    /**
     * Attempts to load the chunk this claim represents.
     * This will fail if the world with the name {@link #worldName} does not exist.
     * @return The chunk this claim represents
     */
    public Chunk chunk() {
        World world = Bukkit.getWorld(worldName);
        if (world == null)
            throw new IllegalStateException("World " + worldName + " does not exist.");
        return world.getChunkAt(x, z);
    }
    
}
