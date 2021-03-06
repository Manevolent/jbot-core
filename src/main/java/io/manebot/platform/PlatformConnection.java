package io.manebot.platform;

import io.manebot.chat.Chat;
import io.manebot.chat.Community;
import io.manebot.plugin.PluginException;
import io.manebot.user.User;
import io.manebot.user.UserRegistration;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface PlatformConnection {

    /**
     * Calls to connect to the platform.  This process is called automatically upon registration.
     */
    void connect() throws PluginException;

    /**
     * Calls to disconnect from the platform.
     */
    default void disconnect() throws UnsupportedOperationException, PluginException {
        throw new UnsupportedOperationException();
    }

    /**
     * Finds if the platform connection holds a connection.
     * @return true if a connection is held, false otherwise.
     */
    default boolean isConnected() {
        return true;
    }

    /**
     * Gets the status of the current user on the platform.
     * @return current status.
     */
    default PlatformUser.Status getStatus() {
        return PlatformUser.Status.UNKNOWN;
    }

    /**
     * Gets the default user registration system for this platform.
     * @return platform user registration.  If <i>null</i>, this defaults to Bot.getDefaultUserRegistration().
     */
    default UserRegistration getUserRegistration() {
        return null;
    }

    /**
     * Sets the status of the current user on the platform.
     * @param status status to set.
     */
    default void setStatus(PlatformUser.Status status) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the self user for the platform.
     * @return self user.
     */
    PlatformUser getSelf();

    /**
     * Gets a user on this platform by their platform-specific ID.
     * @param id platform-specific Id.
     * @return PlatformUser instance if found, null otherwise.
     */
    PlatformUser getPlatformUser(String id);

    /**
     * Gets a collection of known platform users to this platform.
     * @return immutable list of platform users.
     */
    Collection<PlatformUser> getPlatformUsers();

    /**
     * Gets a collection of known user IDs on this platform.
     * @return platform-specific user Id list.
     */
    Collection<String> getPlatformUserIds();

    /**
     * Gets a list of chats associated with this platform.
     * @return immutable collection of Chat instances associated with this platform.
     */
    Collection<Chat> getChats();

    /**
     * Gets a list of chat IDs associated with this platform.
     * @return immutable collection of Chat identifiers associated with this platform.
     */
    Collection<String> getChatIds();

    /**
     * Gets a collection of community IDs.
     * @return collection of community IDs.
     */
    Collection<String> getCommunityIds();

    /**
     * Gets a collection of communities in this platform.
     * @return communities.
     */
    Collection<Community> getCommunities();

    /**
     * Gets a community by its ID.
     * @param id community ID.
     * @return Community instance.
     */
    Community getCommunity(String id);

    /**
     * Creates a new chat with the specified participants.
     * @param users users to create a chat with.
     * @return Chat instance created.
     */
    default Chat createChatFromUsers(Collection<PlatformUser> users) throws UnsupportedOperationException {
        return createChatFromIds(users.stream().map(PlatformUser::getId).collect(Collectors.toList()));
    }


    /**
     * Creates a new chat with the specified participants.
     * @param userIds platform-specific user IDs to create a chat with.
     * @return Chat instance created.
     */
    default Chat createChatFromIds(Collection<String> userIds) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a chat by its user-specific ID.
     * @param id Chat ID to search for.
     * @return Chat instance if found, null otherwise.
     */
    default Chat getChat(String id) {
        return getChats().stream()
                .filter(chat -> chat.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets an immutable collection of chats where the specified user is a member.
     *
     * @param member User member to search for.
     * @return Chat instances whose members contain the specified User.
     */
    default Collection<Chat> getChatsByUser(User member) {
        return Collections.unmodifiableCollection(
                getChats().stream()
                    .filter(chat -> chat.isParticipant(member))
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets an immutable collection of chats where the specified user is a member.
     *
     * @param member User member to search for.
     * @return Chat instances whose members contain the specified User.
     */
    default Collection<Chat> getChatsByUser(PlatformUser member) {
        return Collections.unmodifiableCollection(
                getChats().stream()
                        .filter(chat -> chat.isParticipant(member))
                        .collect(Collectors.toList())
        );
    }

}
