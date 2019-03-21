package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.artifact.ManifestIdentifier;
import com.github.manevolent.jbot.command.CommandManager;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.event.EventListener;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.platform.PlatformConnection;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public interface Plugin {

    /**
     * Gets an instance provided by the specified Plugin.
     * @param instanceClass Instance class to look for.
     * @param <T> Type to return
     * @return Instance
     */
    <T> T getInstance(Class<? extends T> instanceClass);

    /**
     * Gets the registration used to map this Plugin object to the system.
     * @return PluginRegistration instance.
     */
    PluginRegistration getRegistration();

    /**
     * Gets the artifact associated with this plugin.
     * @return associated Artifact instance.
     */
    Artifact getArtifact();

    /**
     * Gets a collection of dependencies associated with this plugin.
     * @return Plugin dependencies.
     */
    Collection<Plugin> getDependencies();

    /**
     * Gets a list of platforms registered by this plugin.
     * @return associated Platform instances.
     */
    Collection<Platform> getPlatforms();

    /**
     * Gets the registered command labels for this plugin.
     * @return registered commands.
     */
    Collection<String> getCommands();

    /**
     * Gets this plugin's name.  This is typically the lowercase <b>artifactId</b> of the plugin.
     * @return Plugin's name.
     */
    String getName();

    /**
     * Sets this artifact's enabled state.
     * @param enabled enabled state
     * @return true if the artifact's state was changed, false otherwise.
     */
    boolean setEnabled(boolean enabled) throws PluginException;

    /**
     * Finds if this plugin is enabled.
     * @return true if the plugin is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Plugin instance Builder
     */
    interface Builder {

        /**
         * Gets the plugin manager associated with this Plugin builder.
         * @return PluginManager instance.
         */
        PluginManager getPluginManager();

        /**
         * Gets the artifact associated with this Plugin builder.
         * @return Artifact instance.
         */
        Artifact getArtifact();

        /**
         * Signals dependency on another Plugin.  Note that, this does not load a Plugin instance.  This only obtains
         * an already-recognized dependent Plugin object.
         * @param identifier Manifest identifier.
         * @return Plugin instance.
         */
        Plugin getDependency(ManifestIdentifier identifier);

        /**
         * Registers an event listener to this Plugin.
         * @param listener event listener to bind.
         * @return Builder instance.
         */
        Builder listen(EventListener listener);

        /**
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder command(String label, CommandExecutor executor) {
            return command(Collections.singleton(label), executor);
        }

        /**
         * Registers a command to this Plugin.
         * @param labels labels to assign this command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder command(Collection<String> labels, CommandExecutor executor) {
            for (String label : labels)
                command(label, executor);

            return this;
        }

        /**
         * Builds a platform for this Plugin instance.
         * @param function Platform building function.
         * @return Builder instance.
         */
        Builder platform(Function<Platform.Builder, PlatformConnection> function);

        /**
         * Binds the specified class to an <i>instance</i>, which is a simple method of communicating functionality to
         * dependent plugins from another plugin.
         *
         * Instances are defined when a plugin is successfully enabled and has configured other features,
         * such as Platforms.
         *
         * @param instantiator instantiator function. Called during Plugin enable, cleared at disable.
         * @param <T> User-chosen type to bind the instance to.
         * @return Builder instance.
         */
        <T> Builder instance(Function<PluginRegistration, T> instantiator);

        /**
         * Calls the specified function when the Plugin is enabled.
         *
         * @param function Function to call.
         * @return Builder instance.
         */
        Builder onEnable(PluginFunction function);

        /**
         * Calls the specified function when the Plugin is enabled.
         *
         * @param function Function to call.
         * @return Builder instance.
         */
        Builder onDisable(PluginFunction function);

        /**
         * Builds a Plugin instance.
         * @return Plugin instance.
         */
        Plugin build();

    }

    interface PluginFunction {
        void call() throws PluginException;
    }

}
