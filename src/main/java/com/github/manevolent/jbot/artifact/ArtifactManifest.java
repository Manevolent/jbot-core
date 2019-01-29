package com.github.manevolent.jbot.artifact;

import java.net.URI;
import java.util.Collection;

public interface ArtifactManifest {

    /**
     * Gets the artifact's ID.
     *
     * @return Artifact ID.
     */
    String getArtifactId();

    /**
     * Gets the artifact's package ID.
     *
     * @return Artifact package ID.
     */
    String getPackageId();

    /**
     * Gets the latest version of this plugin.
     *
     * @param branch Code branch to search on.
     * @return Version instance representing the latest version of this plugin.
     */
    Version getLatestVersion(String branch);

    /**
     * Finds if an update exists for a specific plugin's version.
     * @param version Version to check for an update to.
     * @return true if an update exists, false otherwise.
     */
    default boolean doesUpdateExist(Version version) {
        for (Version otherVersion : getVersions())
            if (version.compareTo(otherVersion) < 0)
                return true;

        return false;
    }

    /**
     * Gets a plugin for a specific version associated with this manifest.
     * @param version version to get.
     * @return Artifact instance corresponding to the version requested.
     */
    Artifact getPlugin(Version version) throws ArtifactNotFoundException;

    /**
     * Gets the versions this plugin has.
     * @return Version collection representing the versions for this plugin.
     */
    Collection<Version> getVersions();

    /**
     * Gets the repository this plugin was obtained from.
     * @return Artifact repository.
     */
    ArtifactRepository getRepository();

    /**
     * Gets this plugin's base URI.  This could be on the web as a GitHub repository, or on disk as a JARfile.
     * @return plugin resource URI.
     */
    URI getUri();

}