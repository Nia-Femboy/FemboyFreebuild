package org.noktron.femboyFreebuild.persistence.repository;

import me.monst.pluginutil.persistence.Query;

import java.sql.Connection;
import java.sql.SQLException;

public class TrustRepository {

    public void createTable(Connection con) throws SQLException {
        Query.of("CREATE TABLE IF NOT EXISTS TRUST ("
                        + "claimUuid UUID NOT NULL, "
                        + "trustedUuid UUID NOT NULL, "
                        + "PRIMARY KEY (claimUuid, trustedUuid))")
                .executeUpdate(con);
    }

}
