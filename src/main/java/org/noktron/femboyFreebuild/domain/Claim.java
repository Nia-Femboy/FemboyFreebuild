package org.noktron.femboyFreebuild.domain;

import java.util.Set;
import java.util.UUID;

/**
 * Represents a claim of a chunk of land by a player. This is the domain model for a claim and reflects the structure of
 * the claim table in the database.
 *
 * @param claimUuid The UUID of the claim
 * @param ownerUuid The UUID of the player who owns the claim
 * @param chunks    The set of chunks that are claimed
 */
public record Claim(UUID claimUuid, UUID ownerUuid, Set<Chunk> chunks) {}
