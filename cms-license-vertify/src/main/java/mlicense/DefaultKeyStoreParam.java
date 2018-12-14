/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package mlicense;

/**
 * This is a convenience class implementing the {@link KeyStoreParam} interface.
 *
 * @author  Christian Schlichtherle
 * @version $Id$
 */
public class DefaultKeyStoreParam extends AbstractKeyStoreParam {

    private final String alias, storePwd, keyPwd;

    /**
     * Constructs a new instance.
     * 
     * @param clazz Used to retrieve the classloader required to load the
     *        keystore as a resource.
     * @param resource The resource identifier for the keystore
     *        to be returned by {@link #getStream()}.
     * @param alias The alias for the key entry in the key store
     *        to be returned by {@link #getAlias()}.
     * @param storePwd The key store password
     *        to be returned by {@link #getStorePwd()}.
     * @param keyPwd The password for the private key in the key store entry
     *        to be returned by {@link #getStorePwd()}.
     */
    public DefaultKeyStoreParam(
            final Class clazz,
            final String resource,
            final String alias,
            final String storePwd,
            final String keyPwd) {
        super(clazz, resource);
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    public String getAlias() {
        return alias;
    }

    public String getStorePwd() {
        return storePwd;
    }

    public String getKeyPwd() {
        return keyPwd;
    }
}
