package com.github.rvskele.toppercustomnamelookup.lookup;

import java.util.UUID;

@FunctionalInterface
public interface UsernameProvider {

    /**
     * Performs a lookup for the username associated with a given UUID.
     *
     * @param uuid the UUID of the player to look up
     * @return the most recently known username, or {@code null} if unknown
     */
    String lookupUsername(UUID uuid);
}
