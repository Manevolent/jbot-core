package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.loader.PluginLoader;
import com.github.manevolent.jbot.plugin.loader.PluginLoaderRegistry;

import java.io.FileNotFoundException;
import java.util.Collection;

public interface PluginManager extends PluginLoader {

    /**
     * Gets this PluginManager's PluginLoaderRegistry instance, used to load plugins from LocalArtifacts.
     * @return PluginLoaderRegistry instance.
     */
    PluginLoaderRegistry getLoaderRegistry();

    /**
     * Gets an immutable collection of loaded plugins.
     * @return Plugin collection.
     */
    Collection<Plugin> getLoadedPlugins();

    /**
     * Unloads the specified plugin.
     * @param plugin Plugin to unload.
     */
    void unload(Plugin plugin);

    /**
     * Helper method.  Loads a plugin based on the specified associated artifact.
     * @param artifact Local artifact/file to load.
     * @return Plugin instance.
     * @throws IllegalArgumentException if the artifact's file extension is not recognized.
     * @throws PluginLoadException if there is a problem loading the plugin file associated with the artifcat.
     * @throws FileNotFoundException if the plugin artifact or one of its artifact dependencies does not exist.
     */
    @Override
    default Plugin load(LocalArtifact artifact)
            throws IllegalArgumentException, PluginLoadException, FileNotFoundException {
        return getLoaderRegistry().getLoader(artifact.getFile()).load(artifact);
    }

}