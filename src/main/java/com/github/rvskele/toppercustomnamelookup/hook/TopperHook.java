package com.github.rvskele.toppercustomnamelookup.hook;

import com.github.rvskele.toppercustomnamelookup.lookup.UsernameCache;
import com.github.rvskele.toppercustomnamelookup.lookup.UsernameProvider;
import com.github.rvskele.toppercustomnamelookup.util.ReflectionUtil;
import me.hsgamer.topper.spigot.plugin.TopperPlugin;
import me.hsgamer.topper.spigot.plugin.template.SpigotTopTemplate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>Because Topper does not expose its internal components directly,
 * this class does a reflective lookup into the
 * {@code BasePlugin.components} list (inside {@link TopperPlugin}) to
 * locate the {@link SpigotTopTemplate} instance.</p>
 *
 * <p>Once found, this template's NameProviderManager is updated to use the
 * custom username lookup implementation provided by {@link UsernameCache}.</p>
 */
public class TopperHook {

    private final JavaPlugin plugin;
    private final UsernameProvider usernameProvider;

    public TopperHook(JavaPlugin plugin, UsernameProvider usernameProvider) {
        this.plugin = plugin;
        this.usernameProvider = usernameProvider;
    }

    /**
     * Attempts to detect and hook into the Topper plugin.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Checks if the Topper plugin is loaded and is an instance of {@link TopperPlugin}.</li>
     *     <li>Reflectively accesses {@code BasePlugin.components}, which contains plugin modules.</li>
     *     <li>Searches for the {@link SpigotTopTemplate}, which manages name providers.</li>
     *     <li>Adds a new provider with {@link UsernameCache}.</li>
     * </ol>
     *
     * <p>If successful, a log entry is produced. If not found or if reflection fails,
     * warnings or errors are logged accordingly.</p>
     */
    public void tryHookTopper() {
        try {
            SpigotTopTemplate template = findSpigotTopTemplate();
            addNameProviderHook(template);
            plugin.getLogger().info("Successfully hooked into Topper!");
        } catch (IllegalStateException e) {
            if (e.getCause() != null) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            } else {
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Topper hooking failed", t);
        }
    }

    private SpigotTopTemplate findSpigotTopTemplate() {
        Plugin topper = Bukkit.getPluginManager().getPlugin("Topper");
        if (!(topper instanceof TopperPlugin topperPlugin)) {
            throw new IllegalStateException("Topper not found or not loaded yet.");
        }

        try {
            Class<?> basePluginClass = topperPlugin.getClass().getSuperclass();
            Field componentsField = basePluginClass.getDeclaredField("components");

            List<?> components = ReflectionUtil.getFieldValue(componentsField, topperPlugin);

            for (Object component : components) {
                if (component instanceof SpigotTopTemplate template) {
                    return template;
                }
            }

            throw new IllegalStateException("SpigotTopTemplate not found inside Topper components.");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to access Topper internal components via reflection.", e);
        }
    }

    private void addNameProviderHook(SpigotTopTemplate template) {
        template.getNameProviderManager().addNameProvider(usernameProvider::lookupUsername);
    }
}
