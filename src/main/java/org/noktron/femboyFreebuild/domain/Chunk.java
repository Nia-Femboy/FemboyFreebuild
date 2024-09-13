package org.noktron.femboyFreebuild.domain;

import org.bukkit.World;

import java.util.Optional;

/**
 * Represents a chunk of land in a world. This is the domain model for a chunk and reflects the structure of the chunk
 * table in the database. A chunk is identified by the world it is in and its x and z coordinates.
 * @param worldName The name of the world the chunk is in
 * @param x The x coordinate of the chunk
 * @param z The z coordinate of the chunk
 */
public record Chunk(String worldName, int x, int z) {
    
    /**
     * Converts a Bukkit chunk to a domain chunk
     * @param chunk The Bukkit chunk to convert
     * @return The domain chunk
     */
    public static Chunk convert(org.bukkit.Chunk chunk) {
        return new Chunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }
    
    /**
     * Returns the world that this chunk is in if it can be found
     * @return The optional world
     */
    public Optional<World> world() {
        return Optional.ofNullable(org.bukkit.Bukkit.getWorld(worldName));
    }
    
    /**
     * Converts this domain chunk to a Bukkit chunk if the world can be found
     * @return The Bukkit chunk optional
     */
    public Optional<org.bukkit.Chunk> convert() {
        return world().map(world -> world.getChunkAt(x, z));
    }
    
}
