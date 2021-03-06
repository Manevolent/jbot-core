package io.manebot.security;

import io.manebot.user.User;
import io.manebot.virtual.Virtual;
import io.manebot.virtual.VirtualProcess;
import com.google.common.collect.MapMaker;

import java.util.Map;

public final class Permission {
    private static final Map<String, Permission> referenceMap = new MapMaker().weakValues().makeMap();
    private final String node;

    private Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object b) {
        return b != null && b instanceof Permission && node.equals(((Permission) b).node);
    }

    /**
     * Gets or creates a permission in the permission node tree.
     * @param node node to create.
     * @return Permission instance.
     */
    public static Permission get(String node) {
        synchronized (referenceMap) {
            return referenceMap.computeIfAbsent(node.toLowerCase(), Permission::new);
        }
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param node node to check.
     * @throws IllegalStateException if the current thread is not a member of the virtual process system, or if that thread is not logged in.
     * @throws SecurityException if the user associated with the current thread does not have the permission node.
     */
    public static void checkPermission(String node) throws IllegalStateException, SecurityException {
        checkPermission(get(node));
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param node node to check.
     */
    public static boolean hasPermission(String node) throws IllegalStateException, SecurityException {
        return hasPermission(get(node));
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param node node to check.
     * @param defaultGrant default grant behavior.  When DENY, users with no matching permission node are denied.  When ALLOW, these users are allowed.
     * @throws SecurityException if the user associated with the current thread does not have the permission node.
     */
    public static void checkPermission(String node, Grant defaultGrant) throws IllegalStateException, SecurityException {
        checkPermission(get(node), defaultGrant);
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param node node to check.
     * @param defaultGrant default grant behavior.  When DENY, users with no matching permission node are denied.  When ALLOW, these users are allowed.
     * @throws IllegalStateException if the current thread is not a member of the virtual process system, or if that thread is not logged in.
     */
    public static boolean hasPermission(String node, Grant defaultGrant) throws IllegalStateException, SecurityException {
        return hasPermission(get(node), defaultGrant);
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param permission Permission instance to check.
     * @throws IllegalStateException if the current thread is not a member of the virtual process system, or if that thread is not logged in.
     * @throws SecurityException if the user associated with the current thread does not have the permission node.
     */
    public static void checkPermission(Permission permission) throws IllegalStateException, SecurityException {
        VirtualProcess currentProcess = Virtual.getInstance().currentProcess();
        if (currentProcess == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                    " is not member of virtual subsystem"
            );

        User user = currentProcess.getUser();
        if (user == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                    " is member of virtual subsystem, but is not logged in"
            );

        user.checkPermission(permission);
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param permission Permission instance to check.
     */
    public static boolean hasPermission(Permission permission) throws IllegalStateException, SecurityException {
        VirtualProcess currentProcess = Virtual.getInstance().currentProcess();
        if (currentProcess == null)
            return false;

        User user = currentProcess.getUser();
        if (user == null)
            return false;

        return user.hasPermission(permission);
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param permission Permission instance to check.
     * @param defaultGrant default grant behavior.  When DENY, users with no matching permission node are denied.  When ALLOW, these users are allowed.
     * @throws IllegalStateException if the current thread is not a member of the virtual process system, or if that thread is not logged in.
     * @throws SecurityException if the user associated with the current thread does not have the permission node.
     */
    public static void checkPermission(Permission permission, Grant defaultGrant) throws IllegalStateException, SecurityException {
        VirtualProcess currentProcess = Virtual.getInstance().currentProcess();
        if (currentProcess == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                            " is not member of virtual subsystem"
            );

        if (currentProcess.getUser() == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                            " is member of virtual subsystem, but is not logged in"
            );

        currentProcess.getUser().checkPermission(permission, defaultGrant);
    }

    /**
     * Checks that the current process has a specific permission, defaulting to deny if no permission is defined.
     * @param permission Permission instance to check.
     * @param defaultGrant default grant behavior.  When DENY, users with no matching permission node are denied.  When ALLOW, these users are allowed.
     */
    public static boolean hasPermission(Permission permission, Grant defaultGrant) throws IllegalStateException, SecurityException {
        VirtualProcess currentProcess = Virtual.getInstance().currentProcess();
        if (currentProcess == null)
            return false;

        if (currentProcess.getUser() == null)
            return false;

        return currentProcess.getUser().hasPermission(permission, defaultGrant);
    }
}
