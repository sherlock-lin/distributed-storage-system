package com.sherlock.ha;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface RegistrationManager extends AutoCloseable {

    /**
     * Registration Listener on listening the registration state.
     */
    @FunctionalInterface
    interface RegistrationListener {

        /**
         * Signal when registration is expired.
         */
        void onRegistrationExpired();

    }

    @Override
    void close();

    /**
     * Prepare ledgers root node, availableNode, readonly node..
     *
     * @return Returns true if old data exists, false if not.
     */
    boolean prepareFormat() throws Exception;

    /**
     * Initializes new cluster by creating required znodes for the cluster. If
     * ledgersrootpath is already existing then it will error out.
     *
     * @return returns true if new cluster is successfully created or false if it failed to initialize.
     * @throws Exception
     */
    boolean initNewCluster() throws Exception;

    /**
     * Do format boolean.
     *
     * @return Returns true if success do format, false if not.
     */
    boolean format() throws Exception;

    /**
     * Nukes existing cluster metadata.
     *
     * @return returns true if cluster metadata is successfully nuked
     *          or false if it failed to nuke the cluster metadata.
     * @throws Exception
     */
    boolean nukeExistingCluster() throws Exception;

    /**
     * Add a listener to be triggered when an registration event occurs.
     *
     * @param listener the listener to be added
     */
    void addRegistrationListener(RegistrationListener listener);

    void registerBookie(String bookieId, boolean readOnly, String serviceInfo);
    void unregisterBookie(String bookieId, boolean readOnly) ;

}
