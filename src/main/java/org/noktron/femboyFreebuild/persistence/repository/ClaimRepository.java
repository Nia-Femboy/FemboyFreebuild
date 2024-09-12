package org.noktron.femboyFreebuild.persistence.repository;

import me.monst.pluginutil.persistence.Query;
import org.noktron.femboyFreebuild.domain.Claim;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ClaimRepository {
    
    public void createTable(Connection con) throws SQLException {
        Query.of("CREATE TABLE IF NOT EXISTS CLAIM ("
                        + "ownerUuid UUID NOT NULL, "
                        + "world VARCHAR(32) NOT NULL, "
                        + "x INTEGER NOT NULL, "
                        + "z INTEGER NOT NULL, "
                        + "PRIMARY KEY (world, x, z))")
                .executeUpdate(con);
    }
    
    public List<Claim> findAll(Connection con) throws SQLException {
        return Query.of("SELECT * FROM CLAIM").asList(con, (rs, c) -> new Claim(
                UUID.fromString(rs.getString("ownerUuid")),
                rs.getString("world"),
                rs.getInt("x"),
                rs.getInt("z")
        ));
    }
    
    public void save(Connection con, Claim claim) throws SQLException {
        Query.of("INSERT INTO CLAIM VALUES (?, ?, ?, ?)")
                .with(claim.ownerUuid(), claim.worldName(), claim.x(), claim.z())
                .executeUpdate(con);
    }
    
    public void deleteByChunk(Connection con, String worldName, int x, int z) throws SQLException {
        Query.of("DELETE FROM CLAIM WHERE world = ? AND x = ? AND z = ?")
                .with(worldName, x, z)
                .executeUpdate(con);
    }
    
    public UUID getOwnerOfClaimedChunk(Connection con, String worldName, int x, int z) throws SQLException {
        return Query.of("SELECT ownerUUID FROM CLAIM WHERE world = ? AND x = ? AND z = ?")
                .with(worldName, x, z)
                .asOne(con, UUID.class);
    }
    
    public int countClaimedChunksByOwner(Connection con, UUID ownerUUID) throws SQLException {
        return Query.of("SELECT COUNT(*) FROM CLAIM WHERE ownerUUID = ?")
                .with(ownerUUID)
                .asOne(con, Integer.class);
    }
    
}
