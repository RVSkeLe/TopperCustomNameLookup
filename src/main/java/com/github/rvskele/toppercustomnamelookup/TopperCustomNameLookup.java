package com.github.rvskele.toppercustomnamelookup;

import com.github.rvskele.toppercustomnamelookup.hook.TopperHook;
import com.github.rvskele.toppercustomnamelookup.lookup.UsernameCache;
import org.bukkit.plugin.java.JavaPlugin;

public final class TopperCustomNameLookup extends JavaPlugin {

    private UsernameCache usernameCache;
    private TopperHook topperHook;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        UsernameCache.init(this);
        this.usernameCache = UsernameCache.get();

        this.topperHook = new TopperHook(this, usernameCache);
        topperHook.tryHookTopper();
    }

    @Override
    public void onDisable() {
        // Nothing needed
        // Might be nice to do something like "removeNameProvider" in the future
    }
}
