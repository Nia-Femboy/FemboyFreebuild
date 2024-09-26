package org.noktron.femboyFreebuild.persistence.repository;

import me.monst.pluginutil.persistence.Query;
import org.noktron.femboyFreebuild.domain.Chunk;
import org.noktron.femboyFreebuild.domain.Claim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClaimRepository {
    
    public void createTable(Connection con) throws SQLException {
        Query.of("CREATE TABLE IF NOT EXISTS Claim ("
                        + "claimUuid UUID PRIMARY KEY NOT NULL, "
                        + "ownerUuid UUID NOT NULL)")
                .executeUpdate(con);
        Query.of("CREATE TABLE IF NOT EXISTS Chunk ("
                        + "claimUuid UUID NOT NULL REFERENCES Claim(claimUuid) ON DELETE CASCADE, "
                        + "world VARCHAR(32) NOT NULL, "
                        + "x INTEGER NOT NULL, "
                        + "z INTEGER NOT NULL, "
                        + "PRIMARY KEY (world, x, z))")
                .executeUpdate(con);
    }
    
    public List<Claim> findAll(Connection con) throws SQLException {
        return Query.of("SELECT * FROM Claim").asList(con, this::reconstructClaim);
    }
    
    public void save(Connection con, Claim claim) throws SQLException {
        Query.of("INSERT INTO Claim VALUES (?, ?)")
                .with(claim.claimUuid(), claim.ownerUuid())
                .executeUpdate(con);
        Query.of("INSERT INTO Chunk VALUES (?, ?, ?, ?)")
                .batch(claim.chunks())
                .with(chunk -> List.of(claim.claimUuid(), chunk.worldName(), chunk.x(), chunk.z()))
                .executeUpdate(con);
    }
    
    /**
     * Removes claims that do not have any chunks. This can happen if all chunks of a claim are deleted
     * or transferred to another claim.
     * @param con The connection to the database
     * @throws SQLException If an error occurs while querying the database
     */
    private void removeChunklessClaims(Connection con) throws SQLException {
        Query.of("DELETE FROM Claim WHERE claimUuid NOT IN (SELECT DISTINCT claimUuid FROM Chunk)")
                .executeUpdate(con);
    }
    
    public void updateOwnershipOfChunks(Connection con, Collection<Chunk> chunks, UUID claimUuid) throws SQLException {
        Query.of("UPDATE Chunk SET claimUuid = ? WHERE world = ? AND x = ? AND z = ?")
                .batch(chunks)
                .with(chunk -> List.of(claimUuid, chunk.worldName(), chunk.x(), chunk.z()))
                .executeUpdate(con);
        removeChunklessClaims(con);
    }
    
    /**
     * Deletes the chunk from the database. If the chunk is the last chunk in the claim, the claim is also deleted.
     *
     * @param con   The connection to the database
     * @param chunk The chunk to delete
     * @throws SQLException If an error occurs while querying the database
     */
    public void deleteChunk(Connection con, Chunk chunk) throws SQLException {
        Query.of("DELETE FROM Chunk WHERE world = ? AND x = ? AND z = ?")
                .with(chunk.worldName(), chunk.x(), chunk.z())
                .executeUpdate(con);
        removeChunklessClaims(con);
    }
    
    /**
     * Returns all claims that are adjacent to the given chunk and owned by the player with the given UUID.
     * Adjacent claims are claims that share a border with the given chunk.
     * @param con The connection to the database
     * @param chunk The chunk to find adjacent claims for
     * @param ownerUuid The UUID of the player who owns the claims
     * @return All claims that are adjacent to the given chunk and owned by the player with the given UUID
     * @throws SQLException If an error occurs while querying the database
     */
    public List<Claim> findAdjacentClaimsOwnedBy(Connection con, Chunk chunk, UUID ownerUuid) throws SQLException {
        return Query.of("SELECT * FROM Claim WHERE ownerUuid = ? AND claimUuid IN "
                        + "(SELECT DISTINCT claimUuid FROM Chunk "
                        + "WHERE world = ? AND (ABS(x - ?) <= 1 AND z = ?) OR (x = ? AND ABS(z - ?) <= 1))")
                .with(ownerUuid, chunk.worldName(), chunk.x(), chunk.z(), chunk.x(), chunk.z())
                .asList(con, this::reconstructClaim);
    }
    
    /**
     * Returns the UUID of the player who owns the claim that contains the given chunk.
     *
     * @param con   The connection to the database
     * @param chunk The chunk to find the owner of
     * @return The UUID of the player who owns the claim that contains the given chunk
     * @throws SQLException If an error occurs while querying the database
     */
    public Optional<UUID> getOwnerOfClaimedChunk(Connection con, Chunk chunk) throws SQLException {
        return Query.of("SELECT ownerUuid FROM Claim WHERE claimUuid = "
                        + "(SELECT claimUuid FROM Chunk WHERE world = ? AND x = ? AND z = ?)")
                .with(chunk.worldName(), chunk.x(), chunk.z())
                .asOptional(con, UUID.class);
    }
    
    /**
     * Returns the claim that contains the given chunk. Exactly the same as the method above but the entire claim is
     * returned, including all chunks.
     *
     * @param con   The connection to the database
     * @param chunk The chunk to find the claim for
     * @return The claim that contains the given chunk
     * @throws SQLException If an error occurs while querying the database
     */
    public Optional<Claim> findClaimByChunk(Connection con, Chunk chunk) throws SQLException {
        return Query.of("SELECT * FROM Claim WHERE claimUuid = "
                        + "(SELECT claimUuid FROM Chunk WHERE world = ? AND x = ? AND z = ?)")
                .with(chunk.worldName(), chunk.x(), chunk.z())
                .asOptional(con, this::reconstructClaim);
    }
    
    /**
     * Returns the number of chunks claimed by the player with the given UUID.
     *
     * @param con       The connection to the database
     * @param ownerUuid The UUID of the player to count the claimed chunks for
     * @return The number of chunks claimed by the player with the given UUID
     * @throws SQLException If an error occurs while querying the database
     */
    public int countClaimedChunksByOwner(Connection con, UUID ownerUuid) throws SQLException {
        return Query.of("SELECT COUNT(*) FROM Chunk WHERE claimUuid IN (SELECT claimUuid FROM Claim WHERE ownerUuid = ?)")
                .with(ownerUuid)
                .asOptional(con, Integer.class)
                .orElse(0);
    }
    
    private Claim reconstructClaim(ResultSet rs, Connection con) throws SQLException {
        UUID claimUuid = UUID.fromString(rs.getString("claimUuid"));
        UUID ownerUuid = UUID.fromString(rs.getString("ownerUuid"));
        Set<Chunk> chunks = Query.of("SELECT * FROM Chunk WHERE claimUuid = ?")
                .with(claimUuid)
                .asSet(con, this::reconstructChunk);
        return new Claim(claimUuid, ownerUuid, chunks);
    }
    
    private Chunk reconstructChunk(ResultSet rs, Connection con) throws SQLException {
        return new Chunk(
                rs.getString("world"),
                rs.getInt("x"),
                rs.getInt("z")
        );
    }
    
}
