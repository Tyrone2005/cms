/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package mlicense;

import de.schlichtherle.util.ObfuscatedString;
import de.schlichtherle.xml.GenericCertificate;

import java.io.*;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.security.auth.x500.X500Principal;
import javax.swing.filechooser.FileFilter;

/**
 * This is the top level class which manages all licensing aspects like for
 * instance the creation, installation and verification of license keys.
 * The license manager knows how to install, verify and uninstall full and
 * trial licenses for a given subject and ensures the privacy of the license
 * content in its persistent form (i.e. the <i>license key</i>).
 * For signing, verifying and validating licenses, this class cooperates with
 * a {@link LicenseNotary}.
 * <p>
 * This class is thread-safe.
 *
 * @author Christian Schlichtherle
 */
public class LicenseManager implements LicenseCreator, LicenseVerifier {

    /** The timeout for the license content cache. */
    private static final long TIMEOUT = 30 * 60 * 1000; // half an hour

    /**
     * The key in the preferences used to store the license key.
     *
     * => "license"
     */
    private static final String PREFERENCES_KEY
            = new ObfuscatedString(new long[] {
        0xD65FA96737AE2CB5L, 0xE804D1A38CF9A413L
    }).toString();

    /**
     * The suffix for files which hold license certificates.
     *
     * => ".lic" - must be lowercase!
     */
    public static final String LICENSE_SUFFIX
            = new ObfuscatedString(new long[] {
        0x97187B3A07E79CEEL, 0x469144B7E0D475E2L
    }).toString();

    static {
        assert LICENSE_SUFFIX.equals(LICENSE_SUFFIX.toLowerCase()); // paranoid
    }

    /** => "CN=" */
    protected static final String CN = new ObfuscatedString(new long[] {
        0x636F59E1FF007F64L, 0xAC9CE58690A43DD0L
    }).toString();

    /** => localized string for resource key "user" */
    private static final String CN_USER = CN + Resources.getString(
            new ObfuscatedString(new long[] {
        0xF3BE4EA2CCDD7EADL, 0x5B6A9F59A1183108L
    }).toString());

    /** => "User" */
    private static final String USER = new ObfuscatedString(new long[] {
        0x9F89522C9F6F4A13L, 0xFFDB7A316241AC79L
    }).toString();

    /** => "System" */
    private static final String SYSTEM = new ObfuscatedString(new long[] {
        0xEC006BE1C1F75BD6L, 0x54D650CDD244774BL
    }).toString();

    /** => "exc.invalidSubject" */
    private static final String EXC_INVALID_SUBJECT = new ObfuscatedString(new long[] {
        0x8029CDF4E32A76ECL, 0x56FA623D9AEE8C1L, 0x99E7882A708663ACL,
        0x5888C0D72E548FF4L
    }).toString();

    /** => "exc.holderIsNull" */
    private static final String EXC_HOLDER_IS_NULL = new ObfuscatedString(new long[] {
        0x6339FEFCDFD84427L, 0x57A2FA0735E47CBEL, 0xED1D06E6EED72950L
    }).toString();

    /** => "exc.issuerIsNull" */
    private static final String EXC_ISSUER_IS_NULL = new ObfuscatedString(new long[] {
        0xD5E29AC879334756L, 0xF1F7421CD6A06536L, 0x5E086D6468FECBF2L
    }).toString();

    /** => "exc.issuedIsNull" */
    private static final String EXC_ISSUED_IS_NULL = new ObfuscatedString(new long[] {
        0xAB8FF89F2DA6C32CL, 0x2A089A9CA80D970EL, 0xCF15F8842FCCD9D5L
    }).toString();

    /** => "exc.licenseIsNotYetValid" */
    private static final String EXC_LICENSE_IS_NOT_YET_VALID = new ObfuscatedString(new long[] {
        0x4B6BB2804EE7DDB1L, 0xD0BB0A33A41543C5L, 0x5FCEC6DF3725CEE4L,
        0xA165775BBD625344L
    }).toString();

    /** => "exc.licenseHasExpired" */
    private static final String EXC_LICENSE_HAS_EXPIRED = new ObfuscatedString(new long[] {
        0xDE2B2A7ACD6DA6DL, 0x9EE12DDECB3D4C0DL, 0xB3CF760B522E8688L,
        0x316BD3E92C17CC40L
    }).toString();

    /** => "exc.consumerTypeIsNull" */
    private static final String EXC_CONSUMER_TYPE_IS_NULL = new ObfuscatedString(new long[] {
        0xD29019F7B1D95C66L, 0xE859C44ACC3EB2FEL, 0xF041027C9003B031L,
        0x27E84AD8870D6063L
    }).toString();

    /** => "exc.consumerTypeIsNotUser" */
    private static final String EXC_CONSUMER_TYPE_IS_NOT_USER = new ObfuscatedString(new long[] {
        0xCE99D49CE98D1E47L, 0x7A3BA300A7DFCEABL, 0x2D2E4B624AD7C4E0L,
        0x2C86A28A075E71C6L, 0x79BCB920E5FB351DL
    }).toString();

    /** => "exc.consumerAmountIsNotOne" */
    private static final String EXC_CONSUMER_AMOUNT_IS_NOT_ONE = new ObfuscatedString(new long[] {
        0x5F20CBB98126BB0AL, 0xE8BB696B25D24011L, 0x435CC3AA7263BAE7L,
        0x9DA3066F501717E4L, 0x62FFA4899FBBA3F8L
    }).toString();

    /** => "exc.consumerAmountIsNotPositive" */
    private static final String EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE = new ObfuscatedString(new long[] {
        0xB14EB6259B4D7249L, 0xCD02F577511528D8L, 0x39B8CF1E258756DDL,
        0x67488F05891DF916L, 0x4256DE0CFFF62DCAL
    }).toString();

    /** => "fileFilter.description" */
    private static final String FILE_FILTER_DESCRIPTION = new ObfuscatedString(new long[] {
        0x2BDDE408C7B71604L, 0xDFCA7DA8912DE4C1L, 0xADA1FC1C1D5F1047L,
        0xD08EAA6CCDC342F3L
    }).toString();

    /** => " (*.lic)" */
    private static final String FILE_FILTER_SUFFIX = new ObfuscatedString(new long[] {
        0xA4BCC907D9FD1290L, 0x614A0A9015D3D8DDL
    }).toString();
    
    /** => " (*.lic)" */
    private static final String MAC_DDRESS_UNAUTHORIZED = "mac_ddress_is_unauthorized";
    /** Returns midnight local time today. */
    protected static Date midnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }

    private LicenseParam param; // initialized by setLicenseParam() - should be accessed via getLicenseParam() only!

    //
    // Data computed and cached from the license configuration parameters.
    //

    private LicenseNotary notary; // lazy initialized

    private PrivacyGuard guard; // lazy initialized

    /** The cached certificate of the current license key. */
    private GenericCertificate certificate; // lazy initialized

    /** The time when the certificate was last set. */
    private long certificateTimeout; // lazy initialized

    /** A suitable file filter for the subject of this license manager. */
    private FileFilter fileFilter; // lazy initialized

    /**
     * Constructs a License Manager.
     * <p>
     * <b>Warning:</b> The manager created by this constructor is <em>not</em>
     * valid and cannot be used unless {@link #setLicenseParam(LicenseParam)}
     * is called!
     */
    protected LicenseManager() {
    }

    /**
     * Constructs a License Manager.
     *
     * @param  param the license configuration parameters
     *         - may <em>not</em> be {@code null}.
     * @throws NullPointerException If the given parameter object does not
     *         obey the contract of its interface due to a {@code null}
     *         pointer.
     * @throws IllegalPasswordException If any password in the parameter object
     *         does not comply to the current policy.
     */
    public LicenseManager(LicenseParam param) {
        setLicenseParam0(param);
    }

    /** Returns the license configuration parameters. */
    public synchronized LicenseParam getLicenseParam() {
        return param;
    }

    /**
     * Sets the license configuration parameters.
     * Calling this method resets the manager as if it had been
     * newly created.
     * Some plausibility checks are applied to the given parameter object
     * to ensure that it adheres to the contract of the parameter interfaces.
     *
     * @param  param the license configuration parameters
     *         - may <em>not</em> be {@code null}.
     * @throws NullPointerException If the given parameter object does not
     *         obey the contract of its interface due to a {@code null}
     *         pointer.
     * @throws IllegalPasswordException If any password in the parameter object
     *         does not comply to the current policy.
     */
    public synchronized void setLicenseParam(LicenseParam param) {
        setLicenseParam0(param);
    }

    private void setLicenseParam0(LicenseParam param) {
        // Check parameters to implement fail-fast behaviour.
        final CipherParam cipherParam;
        if (null == param.getSubject()
                || null == param.getKeyStoreParam()
                || null == (cipherParam = param.getCipherParam()))
            throw new NullPointerException();
        // DONT DO THIS - ONLY REQUIRED FOR INSTALLATION OR VERIFICATION!
        //if (null == param.getPreferences())
        //    throw new NullPointerException();
        Policy.getCurrent().checkPwd(cipherParam.getKeyPwd());

        this.param = param;
        notary = null;
        setCertificate0(null);
        fileFilter = null;
    }

    //
    // Methods for license contents.
    //

    /**
     * Initializes and validates the license content, creates a new signed
     * license certificate for it and compresses, encrypts and stores it to
     * the given file as a license key.
     * <p>
     * As a side effect, the given license {@code content} is initialized
     * with some reasonable defaults unless the respective properties have
     * already been set.
     *
     * @param  content the license content
     *         - may <em>not</em> be {@code null}.
     * @param  keyFile the file to save the license key to
     *         - may <em>not</em> be {@code null}.
     *         This should have a {@code LICENSE_SUFFIX}.
     * @throws Exception for various reasons. Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     *
     * @see #store(LicenseContent, LicenseNotary, File)
     * @see #initialize(LicenseContent)
     * @see #validate(LicenseContent)
     */
    public final synchronized void store(LicenseContent content, File keyFile)
    throws Exception {
        store(content, getLicenseNotary(), keyFile);
    }

    /**
     * Initializes and validates the license content, creates a new signed
     * license certificate for it and compresses, encrypts and stores it to
     * the given file as a license key.
     * <p>
     * As a side effect, the given license {@code content} is initialized
     * with some reasonable defaults unless the respective properties have
     * already been set.
     *
     * @param  content the license content
     *         - may <em>not</em> be {@code null}.
     * @param  notary the license notary used to sign the license key
     *         - may <em>not</em> be {@code null}.
     * @param  keyFile the file to save the license key to
     *         - may <em>not</em> be {@code null}.
     *         This should have a {@code LICENSE_SUFFIX}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @see    #initialize(LicenseContent)
     * @see    #validate(LicenseContent)
     */
    protected synchronized void store(
            LicenseContent content,
            LicenseNotary notary,
            File keyFile)
    throws Exception {
        storeLicenseKey(create(content, notary), keyFile);
    }

    /**
     * Initializes and validates the license content, creates a new signed
     * license certificate for it and compresses, encrypts and returns it
     * as a license key.
     * <p>
     * As a side effect, the given license {@code content} is initialized
     * with some reasonable defaults unless the respective properties have
     * already been set.
     *
     * @param  content the license content
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return The license key
     *         - {@code null} is never returned.
     * @see    #create(LicenseContent, LicenseNotary)
     * @see    #initialize(LicenseContent)
     * @see    #validate(LicenseContent)
     */
    public final synchronized byte[] create(LicenseContent content)
    throws Exception {
        return create(content, getLicenseNotary());
    }

    /**
     * Initializes and validates the license content, creates a new signed
     * license certificate for it and compresses, encrypts and returns it
     * as a license key.
     * <p>
     * As a side effect, the given license {@code content} is initialized
     * with some reasonable defaults unless the respective properties have
     * already been set.
     *
     * @param  content the license content
     *         - may <em>not</em> be {@code null}.
     * @param  notary the license notary used to sign the license key
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return The license key
     *         - {@code null} is never returned.
     * @see    #initialize(LicenseContent)
     * @see    #validate(LicenseContent)
     */
    protected synchronized byte[] create(
            LicenseContent content,
            LicenseNotary notary)
    throws Exception {
        initialize(content);
        create_validate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * Loads, decrypts, decompresses, decodes and verifies the license key in
     * {@code keyFile}, validates its license content and installs it
     * as the current license key.
     *
     * @param  keyFile the file to load the license key from
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see #install(File, LicenseNotary)
     * @see #validate(LicenseContent)
     */
    public final synchronized LicenseContent install(File keyFile)
    throws Exception {
        return install(keyFile, getLicenseNotary());
    }

    /**
     * Loads, decrypts, decompresses, decodes and verifies the license key in
     * {@code keyFile}, validates its license content and installs it
     * as the current license key.
     *
     * @param  keyFile The file to load the license key from
     *         - may <em>not</em> be {@code null}.
     * @param  notary The license notary used to verify the license key
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see    #validate(LicenseContent)
     */
    protected synchronized LicenseContent install(
            File keyFile,
            LicenseNotary notary)
    throws Exception {
        return install(loadLicenseKey(keyFile), notary);
    }

    /**
     * Decrypts, decompresses, decodes and verifies the license key in
     * {@code key}, validates its license content and installs it
     * as the current license key.
     *
     * @param  key the license key
     *         - may <em>not</em> be {@code null}.
     * @param  notary the license notary used to verify the license key
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see #validate(LicenseContent)
     */
    protected synchronized LicenseContent install(
            final byte[] key,
            final LicenseNotary notary)
    throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) certificate.getContent();
        validate(content);
        setLicenseKey(key);
        setCertificate(certificate);

        return content;
    }

    /**
     * Decrypts, decompresses, decodes and verifies the current license key,
     * validates its license content and returns it.
     *
     * @throws NoLicenseInstalledException if no license key is installed.
     * @throws Exception for any other reason.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see    #validate(LicenseContent)
     */
    public final synchronized LicenseContent verify() throws Exception {
        return verify(getLicenseNotary());
    }

    /**
     * Decrypts, decompresses, decodes and verifies the current license key,
     * validates its license content and returns it.
     *
     * @param  notary the license notary used to verify the current license key
     *         - may <em>not</em> be {@code null}.
     * @throws NoLicenseInstalledException if no license key is installed.
     * @throws Exception for any other reason.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see    #validate(LicenseContent)
     */
    protected synchronized LicenseContent verify(final LicenseNotary notary)
    throws Exception {
        GenericCertificate certificate = getCertificate();
        if (null != certificate)
            return (LicenseContent) certificate.getContent();

        // Load license key from preferences, 
        final byte[] key = getLicenseKey();
        if (null == key)
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) certificate.getContent();
        validate(content);
        setCertificate(certificate);

        return content;
    }

    /**
     * Decrypts, decompresses, decodes and verifies the given license key,
     * validates its license content and returns it.
     *
     * @param key the license key
     *        - may <em>not</em> be {@code null}.
     * @throws Exception an instance of a subclass of this class for various
     *         reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see    #validate(LicenseContent)
     */
    public final synchronized LicenseContent verify(byte[] key)
    throws Exception {
        return verify(key, getLicenseNotary());
    }

    /**
     * Decrypts, decompresses, decodes and verifies the given license key,
     * validates its license content and returns it.
     *
     * @param  key the license key
     *         - may <em>not</em> be {@code null}.
     * @param  notary the license notary used to verify the license key
     *         - may <em>not</em> be {@code null}.
     * @throws Exception for various reasons.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @return A clone of the verified and validated content of the license key
     *         - {@code null} is never returned.
     * @see    #validate(LicenseContent)
     */
    protected synchronized LicenseContent verify(
            final byte[] key,
            final LicenseNotary notary)
    throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) certificate.getContent();
        validate(content);

        return content;
    }

    /**
     * Uninstalls the current license key.
     *
     * @throws Exception An instance of a subclass of this class for various
     *         reasons.
     */
    public synchronized void uninstall() throws Exception {
        setLicenseKey(null);
        setCertificate(null);
    }

    /**
     * Initializes the given license {@code content} with some reasonable
     * defaults unless the respective properties have already been set.
     *
     * @see #validate(LicenseContent)
     */
    protected synchronized void initialize(final LicenseContent content) {
        if (null == content.getHolder())
            content.setHolder(new X500Principal(CN_USER));
        if (null == content.getSubject())
            content.setSubject(getLicenseParam().getSubject());
        if (null == content.getConsumerType()) {
            final Preferences prefs = getLicenseParam().getPreferences();
            if (null != prefs) {
                if (prefs.isUserNode()) content.setConsumerType(USER);
                else content.setConsumerType(SYSTEM);
                content.setConsumerAmount(1);
            }
        }
        if (null == content.getIssuer())
            content.setIssuer(new X500Principal(
                    CN + getLicenseParam().getSubject()));
        if (null == content.getIssued())
            content.setIssued(new Date());
        if (null == content.getNotBefore())
            content.setNotBefore(midnight());
    }

    /**
     * Validates the license content.
     * This method is called whenever a license certificate is created,
     * installed or verified.
     * <p>
     * Validation consists of the following plausability checks for the
     * properties of this class:
     * <p>
     * <ul>
     * <li>'subject' must match the subject required by the application
     *     via the {@link LicenseParam} interface.
     * <li>'holder', 'issuer' and 'issued' must be provided (i.e. not
     *     {@code null}).
     * <li>If 'notBefore' or 'notAfter' are provided, the current date and
     *     time must match their restrictions.
     * <li>'consumerType' must be provided and 'consumerAmount' must be
     *     positive.
     *     If a user preference node is provided in the license parameters,
     *     'consumerType' must also match {@code "User"} (whereby case is
     *     ignored) and 'consumerAmount' must equal 1.
     * </ul>
     * <p>
     * If you need more or less rigid restrictions, you should override this
     * method in a subclass.
     * 
     * @param  content the license content
     *         - may <em>not</em> be {@code null}.
     * @throws NullPointerException if {@code content} is {@code null}.
     * @throws LicenseContentException if any validation test fails.
     *         Note that you should always use
     *         {@link Throwable#getLocalizedMessage()} to get a (possibly
     *         localized) meaningful detail message.
     * @see    #initialize(LicenseContent)
     */
//    protected synchronized void validate(final LicenseContent content)
//    throws LicenseContentException {
//        final LicenseParam param = getLicenseParam();
//        if (!param.getSubject().equals(content.getSubject()))
//            throw new LicenseContentException(EXC_INVALID_SUBJECT);
//        if (null == content.getHolder())
//            throw new LicenseContentException(EXC_HOLDER_IS_NULL);
//        if (null == content.getIssuer())
//            throw new LicenseContentException(EXC_ISSUER_IS_NULL);
//        if (null == content.getIssued())
//            throw new LicenseContentException(EXC_ISSUED_IS_NULL);
//        final Date now = new Date();
//        final Date notBefore = content.getNotBefore();
//        if (null != notBefore && now.before(notBefore))
//            throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
//        final Date notAfter = content.getNotAfter();
//        if (null != notAfter && now.after(notAfter))
//            throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
//        final String consumerType = content.getConsumerType();
//        if (null == consumerType)
//            throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
//        final Preferences prefs = param.getPreferences();
//        if (null != prefs && prefs.isUserNode()) {
//            if (!USER.equalsIgnoreCase(consumerType))
//                throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
//            if (1 != content.getConsumerAmount())
//                throw new LicenseContentException(EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
//        } else {
//            if (0 >= content.getConsumerAmount())
//                throw new LicenseContentException(EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
//        }
//    }

    protected synchronized void validate(LicenseContent paramLicenseContent)
			throws LicenseContentException {
		LicenseParam localLicenseParam = getLicenseParam();
		if (!localLicenseParam.getSubject().equals(
				paramLicenseContent.getSubject())) {
			throw new LicenseContentException(EXC_INVALID_SUBJECT);
		}
		if (paramLicenseContent.getHolder() == null) {
			throw new LicenseContentException(EXC_HOLDER_IS_NULL);
		}
		if (paramLicenseContent.getIssuer() == null) {
			throw new LicenseContentException(EXC_ISSUER_IS_NULL);
		}
		if (paramLicenseContent.getIssued() == null) {
			throw new LicenseContentException(EXC_ISSUED_IS_NULL);
		}
		Date localDate1 = new Date();
		Date localDate2 = paramLicenseContent.getNotBefore();
		if ((localDate2 != null) && (localDate1.before(localDate2))) {
			throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
		}
		Date localDate3 = paramLicenseContent.getNotAfter();
		if ((localDate3 != null) && (localDate1.after(localDate3))) {
			throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
		}
		
		LicenseCheckModel licenseCheckModel = (LicenseCheckModel)paramLicenseContent.getExtra();
		String macAddress = licenseCheckModel.getIpMacAddress();
		
		try {
			if (!ListNets.validateMacAddress(macAddress)) {
				throw new LicenseContentException(MAC_DDRESS_UNAUTHORIZED);
			}
		} catch (SocketException e) {
			throw new LicenseContentException(e.getMessage());
		}
		
		
		String str = paramLicenseContent.getConsumerType();
		if (str == null) {
			throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
		}
		Preferences localPreferences = localLicenseParam.getPreferences();
		if ((localPreferences != null) && (localPreferences.isUserNode())) {
			if (!USER.equalsIgnoreCase(str)) {
				throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
			}
			if (paramLicenseContent.getConsumerAmount() != 1) {
				throw new LicenseContentException(
						EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
			}
		} else if (paramLicenseContent.getConsumerAmount() <= 0) {
			throw new LicenseContentException(
					EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
		}
	}

    //
    // Methods for license certificates.
    //

    /**
     * Returns the license certificate cached from the
     * last installation/verification of a license key
     * or {@code null} if there wasn't an installation/verification
     * or a timeout has occured.
     */
    protected synchronized GenericCertificate getCertificate() {
        return System.currentTimeMillis() < certificateTimeout
                ? certificate
                : null;
    }

    /**
     * Sets the given license certificate as installed or verified.
     *
     * @param certificate the license certificate
     *        - may be {@code null} to clear.
     */
    protected synchronized void setCertificate(GenericCertificate certificate) {
        setCertificate0(certificate);
    }

    private void setCertificate0(GenericCertificate certificate) {
        certificateTimeout = null != (this.certificate = certificate)
                ? System.currentTimeMillis() + TIMEOUT
                : 0;
    }

    //
    // Methods for license keys.
    // Note that in contrast to the methods of the privacy guard,
    // the following methods may have side effects (preferences, file system).
    //

    /**
     * Returns the current license key.
     */
    protected synchronized byte[] getLicenseKey() {
        return getLicenseParam().getPreferences().getByteArray(PREFERENCES_KEY, null);
    }

    /**
     * Installs the given license key as the current license key.
     * If {@code key} is {@code null}, the current license key gets
     * uninstalled (but the cached license certificate is not cleared).
     */
    protected synchronized void setLicenseKey(final byte[] key) {
        final Preferences prefs = getLicenseParam().getPreferences();
        if (null != key)
            prefs.putByteArray(PREFERENCES_KEY, key);
        else
            prefs.remove(PREFERENCES_KEY);
    }

    /**
     * Stores the given license key to the given file.
     * 
     * @param key the license key
     *        - may <em>not</em> be {@code null}.
     * @param keyFile the file to save the license key to
     *        - may <em>not</em> be {@code null}.
     *        This should have a {@code LICENSE_SUFFIX}.
     */
    protected static void storeLicenseKey(
            final byte[] key,
            final File keyFile)
    throws IOException {
        final OutputStream out = new FileOutputStream(keyFile);
        try {
            out.write(key);
        } finally {
            try { out.close(); }
            catch (IOException weDontCare) { }
        }
    }

    /**
     * Loads and returns the first megabyte of content from {@code keyFile}
     * as license key in a newly created byte array.
     * 
     * @param keyFile the file holding the license key
     *        - may <em>not</em> be {@code null}.
     */
    protected static byte[] loadLicenseKey(final File keyFile)
    throws IOException {
        // Allow max 1MB size files and let the verifier detect a partial read
        final int size = Math.min((int) keyFile.length(), 1024 * 1024);
        final byte[] b = new byte[size];
        final InputStream in = new FileInputStream(keyFile);
        try {
            // Let the verifier detect a partial read as an error
            in.read(b);
        } finally {
            in.close();
        }
        return b;
    }

    //
    // Various stuff.
    //

    /**
     * Returns a license notary configured to use the keystore parameters
     * contained in the current license parameters
     * - {@code null} is never returned.
     */
    protected synchronized LicenseNotary getLicenseNotary() {
        if (null == notary)
            notary = new LicenseNotary(getLicenseParam().getKeyStoreParam());
        return notary;
    }

    /**
     * Returns a privacy guard configured to use the cipher parameters
     * contained in the current license parameters
     * - {@code null} is never returned.
     */
    protected synchronized PrivacyGuard getPrivacyGuard() {
        if (null == guard)
            guard = new PrivacyGuard(getLicenseParam().getCipherParam());
        return guard;
    }

    /**
     * Returns a suitable file filter for the subject of this license manager.
     * On Windows systems, the case of the suffix is ignored when browsing
     * directories.
     *
     * @return A valid {@code FileFilter}.
     */
    public synchronized FileFilter getFileFilter() {
        if (fileFilter != null)
            return fileFilter;
        final String description
                = Resources.getString(FILE_FILTER_DESCRIPTION,
                    getLicenseParam().getSubject());
        if (File.separatorChar == '\\') {
            fileFilter = new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory()
                        || f.getPath().toLowerCase().endsWith(LICENSE_SUFFIX);
                }

                public String getDescription() {
                    return description + FILE_FILTER_SUFFIX;
                }
            };
        } else {
            fileFilter = new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory()
                        || f.getPath().endsWith(LICENSE_SUFFIX);
                }

                public String getDescription() {
                    return description + FILE_FILTER_SUFFIX;
                }
            };
        }
        return fileFilter;
    }
    
	protected synchronized void create_validate(LicenseContent paramLicenseContent)
			throws LicenseContentException {
		LicenseParam localLicenseParam = getLicenseParam();
		if (!localLicenseParam.getSubject().equals(
				paramLicenseContent.getSubject())) {
			throw new LicenseContentException(EXC_INVALID_SUBJECT);
		}
		if (paramLicenseContent.getHolder() == null) {
			throw new LicenseContentException(EXC_HOLDER_IS_NULL);
		}
		if (paramLicenseContent.getIssuer() == null) {
			throw new LicenseContentException(EXC_ISSUER_IS_NULL);
		}
		if (paramLicenseContent.getIssued() == null) {
			throw new LicenseContentException(EXC_ISSUED_IS_NULL);
		}
		Date localDate1 = new Date();
		Date localDate2 = paramLicenseContent.getNotBefore();
		if ((localDate2 != null) && (localDate1.before(localDate2))) {
			throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
		}
		Date localDate3 = paramLicenseContent.getNotAfter();
		if ((localDate3 != null) && (localDate1.after(localDate3))) {
			throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
		}	
		
		String str = paramLicenseContent.getConsumerType();
		if (str == null) {
			throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NULL);
		}
		Preferences localPreferences = localLicenseParam.getPreferences();
		if ((localPreferences != null) && (localPreferences.isUserNode())) {
			if (!USER.equalsIgnoreCase(str)) {
				throw new LicenseContentException(EXC_CONSUMER_TYPE_IS_NOT_USER);
			}
			if (paramLicenseContent.getConsumerAmount() != 1) {
				throw new LicenseContentException(
						EXC_CONSUMER_AMOUNT_IS_NOT_ONE);
			}
		} else if (paramLicenseContent.getConsumerAmount() <= 0) {
			throw new LicenseContentException(
					EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
		}
	}

}
