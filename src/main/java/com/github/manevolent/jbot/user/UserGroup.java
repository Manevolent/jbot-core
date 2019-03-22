package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.entity.EntityType;

import java.util.Collection;

public interface UserGroup extends EntityType {

    /**
     * Gets the name of this group.
     * @return group name.
     */
    String getName();

    /**
     * Gets this group's owner.
     * @return owner.
     */
    User getOwner();

    /**
     * Gets the users in this group.
     * @return users in the group.
     */
    Collection<User> getUsers();

    /**
     * Finds if the specified user is a member of this group.
     * @param user User to check in the group.
     * @return true if the user is a member of the group, false otherwise.
     */
    default boolean isMember(User user) {
        return getUsers().contains(user);
    }

    /**
     * Adds a user to this group.
     * @param user user to add.
     * @throws SecurityException if there is a security violation adding the user.
     */
    void addUser(User user) throws SecurityException;

    /**
     * Removes a user from this group.
     * @param user user to remove.
     * @throws SecurityException if there is a security violation removing the user.
     */
    void removeUser(User user) throws SecurityException;

}
