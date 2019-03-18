package com.github.manevolent.jbot.database;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.Collection;

public interface Database extends AutoCloseable {

    /**
     * Gets the database's name.
     * @return Database name.
     */
    String getName();

    /**
     * Gets a list of this database's own registered entities.
     * @return Collection of Class objects.
     */
    Collection<Class<?>> getEntities();

    /**
     * Gets an immutable collection of this database's dependent databases.
     * @return Database collection.
     */
    Collection<Database> getDependentDatabases();

    /**
     * Finds if this database is closed.  A closed database does not hold open connections to a server.
     * @return true if the database is closed, false otherwise.
     */
    boolean isClosed();

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @param function Function to execute.
     * @param <T> Return type of the function.
     *
     * @throws E user-defined exception
     *
     * @return User-defined result.
     */
    default <T, E extends Throwable> T execute(Execution<T, E> function) throws E {
        EntityManager session = null;

        try {
            session = openSession();

            return function.execute(session);
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @throws E user-defined exception
     *
     * @param function Function to execute.
     */
    default <E extends Throwable> void execute(VoidExecution<E> function) throws E {
        EntityManager session = null;

        try {
            session = openSession();

            function.execute(session);
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Executes a function on the database, using a transactional
     * session and returning the session to the pool fairly, automatically rolling back on failure.
     *
     * @param function Function to execute.
     * @throws SQLException failure to execute <b>function</b> or transactional behavior.
     */
    default <E extends Throwable> void executeTransaction(VoidExecution<E> function) throws SQLException {
        EntityManager session = null;

        try {
            session = openSession();

            session.getTransaction().begin();

            function.execute(session);

            session.getTransaction().commit();
        } catch (Throwable e) {
            if (session != null && session.getTransaction().isActive())
                session.getTransaction().rollback();

            throw new SQLException("Problem executing transaction", e);
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Opens a session to the database.
     *
     * @return Session instance.
     */
    EntityManager openSession();

    /**
     * Database model constructor.
     */
    interface ModelConstructor {

        /**
         * Gets the database name.
         *
         * @return Database name.
         */
        String getDatabaseName();

        /**
         * Depends on another database, associating those models with this model.
         *
         * @param database Database to depend on.
         * @return ModelConstructor instance.
         */
        ModelConstructor depend(Database database);

        /**
         * Registers an entityi n the persistence system for this database.
         *
         * @param entityClass Entity class.
         * @return ModelConstructor instance.
         */
        ModelConstructor registerEntity(Class<?> entityClass);

        /**
         * Defines the database in the persistence system.
         *
         * @return Database instance.
         */
        Database define();

    }

    interface VoidExecution<E extends Throwable> {
        void execute(EntityManager entityManager) throws E;
    }

    interface Execution<T, E extends Throwable> {
        T execute(EntityManager entityManager) throws E;
    }

}