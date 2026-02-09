package com.github.rvskele.toppercustomnamelookup.lookup;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.github.rvskele.toppercustomnamelookup.TopperCustomNameLookup;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This cache stores UUID → Optional(username) mappings to reduce expensive
 * lookups through {@link org.bukkit.Bukkit#getOfflinePlayer(UUID)}.
 * </p>
 */
public class UsernameCache implements UsernameProvider {

    private static UsernameCache INSTANCE;
    private final Cache<UUID, Optional<String>> usernameCache;

    private UsernameCache(TopperCustomNameLookup plugin) {

        long maxSize = plugin.getConfig().getLong("username-cache.max-size", 10000);
        long expireMinutes = plugin.getConfig().getLong("username-cache.expire-minutes", 120);

        this.usernameCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expireMinutes, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Initializes the singleton instance of this cache.
     * @param plugin The plugin instance to get config values from
     */
    public static void init(TopperCustomNameLookup plugin) {
        if (INSTANCE != null) return;
        INSTANCE = new UsernameCache(plugin);
    }

    /**
     * Get the singleton
     *
     * @return the active {@link UsernameCache} instance
     * @throws IllegalStateException if called before {@link #init(TopperCustomNameLookup)}
     */
    public static UsernameCache get() {
        if (INSTANCE == null)
            throw new IllegalStateException("UsernameCache must be initialized first");
        return INSTANCE;
    }

    /**
     * Look up a username from {@link UsernameCache#usernameCache} or load it using Bukkit's offline player system.
     * <p>
     * The first lookup for a UUID triggers a load via
     * {@link Bukkit#getOfflinePlayer(UUID)}. The username result (including null)
     * is cached afterward to avoid repeated player file access.
     * </p>
     *
     * @param uuid the UUID of the player whose username is requested
     * @return the player's last known username, or {@code null} if unknown
     */
    @Override
    public String lookupUsername(UUID uuid) {
        try {
            return usernameCache.get(uuid,
                    () -> Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).getName())
            ).orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
