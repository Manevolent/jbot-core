package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.entity.Entity;

import java.util.Collection;

public interface UserGroup extends Entity {

    /**
     * Gets the name of this group.
     * @return group name.
     */
    String getName();

    /**
     * Gets the users in this group.
     * @return users in the group.
     */
    Collection<User> getUsers();

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