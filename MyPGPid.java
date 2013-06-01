/*
 * MyPGPid applet
 * (C) 2013 Diego 'NdK' Zuccato
 * Released under GNU Public Licence (GPL)
 * Package AID: 0xF9:0x4D:0x79:0x50:0x47:0x50:0x69:0x64:0x00:0x00 (F9 'MyPGPid' 00 00)
 * Applet AID:  0xF9:0x4D:0x79:0x50:0x47:0x50:0x69:0x64:0x30:0x31 (F9 'MyPGPid01')
 */

package MyPGPid;

/*
 * Imported packages
 */
// specific import for Javacard API access
import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.*;

public class MyPGPid extends javacard.framework.Applet
{
    // Odd high nibble in CLA means 'chaining'
    final static byte CLA_CARD_TEST               = (byte) 0xE0;
    // INS cannot be odd
    final static byte INS_CARD_READ_POLICY           = (byte) 0x70;
    final static byte INS_CARD_KEY_PUSH              = (byte) 0x72;
//    final static byte INS_CARD_KEY_SELECT           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;
//    final static byte INS_CARD_           = (byte) 0x7;

    // *** Card-specific configuration: decomment only one section
    // JCOP
//    final static byte  DEF_ALG = KeyPair.ALG_RSA_CRT;
//    final static short DEF_LEN = (short)2048;	// bits
//    final static short DEF_CAKEYTYPE = KeyBuilder.TYPE_RSA_PUBLIC;

    // G+D SmartCafé Expert
    final static byte  DEF_ALG = KeyPair.ALG_RSA;
    final static short DEF_LEN = (short)2048;	// bits
    final static byte  DEF_CAKEYTYPE = KeyBuilder.TYPE_RSA_PUBLIC;

    // *** Extended function support
    private KeyPair[]	m_keyPair = null;	// Keeps all key pairs
    private byte[]	m_transferBuffer = null;
    private byte	m_extFlag = (byte)0;// Using extensions
    private byte	m_currEncKey = 1;
    private byte	m_nPins = 2;
    private short	m_maxKeys = 3;	// SIG,DEC,AUT
    private short	m_bSize = 0;	// buffer size
    private boolean	m_separateAuth = false;// Use different AUT key/PIN for RFID
    private byte	m_oobAuth = 0;	// Require OOB auth for SIG key
    private boolean	m_CAKeyIsSet = false; // True when CA key is set
    private PublicKey	m_CAKey = null;

    /**
     * MyPGPid default constructor
     * Only this class's install method should create the applet object.
     */
    protected MyPGPid(byte[] buffer, short offset, byte length)
    {
	// G+D uses non-zero offset, JCOP uses offset=0
        if(length > 9) {
    	    // data offset is used for application specific parameter.
    	    // initialization with default offset (AID offset).
    	    short dataOffset = offset;

            // Install parameter detail. Compliant with OP 2.0.1.
            // | size | content
            // |------|---------------------------
            // |  1   | [AID_Length]
            // | 5-16 | [AID_Bytes]
            // |  1   | [Privilege_Length]
            // | 1-n  | [Privilege_Bytes] (normally 1Byte)
            // |  1   | [Application_Proprietary_Length]
            // | 0-m  | [Application_Proprietary_Bytes]

            // shift to privilege offset
            dataOffset += (short)( 1 + buffer[dataOffset]);
            // finally shift to Application specific offset
            dataOffset += (short)( 1 + buffer[dataOffset]);

	    // Here dataOffset points to policy (app. proprietary) len

	    if((short)(offset+length)>dataOffset) {	// Policy is present
		// Application_Proprietary_Length must be 5
		// Application_Proprietary_Bytes is:
		// | size | content
		// |------|-----------------------
		// |  1   | Number of expired ENC keys to keep
		// |  2   | Max size of transfer buffer (in bytes)
		// |  1   | Use different PIN and key for RFID
		// |  1   | OOB user consent needed
		short polLen=buffer[dataOffset];

		if(((short)(dataOffset+polLen-offset) > length) || (polLen != 5)) { // Check if policy is complete
		    ISOException.throwIt( ISO7816.SW_DATA_INVALID );
		}
		++dataOffset;

		// Limit of 255 old ENC keys
		m_maxKeys+=buffer[dataOffset];
		++dataOffset;

		m_bSize=(short)Util.makeShort(buffer[dataOffset], buffer[(short)(dataOffset+1)]);
		++dataOffset; ++dataOffset;

		if(0!=(byte)buffer[dataOffset]) {
		    m_separateAuth=true;
		    ++m_maxKeys;	// Requires an extra key...
		    ++m_nPins;		// ... and an extra PIN
		}
		++dataOffset;

		if(0!=buffer[dataOffset]) {
		    // Needs card-specific code!
                    ISOException.throwIt( ISO7816.SW_INS_NOT_SUPPORTED ) ;
		    // m_oobAuth=buffer[dataOffset];
		}

		++dataOffset;

		// *** These can fail: if so, then abort install

		// To RAM: prevent EEPROM wearout and clear buffer when card is removed
		m_transferBuffer = JCSystem.makeTransientByteArray(m_bSize, JCSystem.CLEAR_ON_DESELECT);

		// Pre-allocate space for all keys
		//@@@ Should allocate space for key IDs too!
		m_keyPair = new KeyPair[m_maxKeys];
		for(short c=0; c<m_maxKeys; ++c)
		    m_keyPair[c]=new KeyPair(DEF_ALG, DEF_LEN);
		m_CAKey= (PublicKey)KeyBuilder.buildKey( DEF_CAKEYTYPE, DEF_LEN, false);
	    }

	    // Register OP2 applet
	    register(buffer, (short)(offset + 1), (byte)buffer[offset]);
        } else {
	    // Register non-OP2 applet
	    register();
	}
    }

    /**
     * Method installing the applet.
     * @param bArray the array constaining installation parameters
     * @param bOffset the starting offset in bArray
     * @param bLength the length in bytes of the data parameter in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException
    {
        /* applet  instance creation */
        new MyPGPid(bArray, bOffset, (byte)bLength );
    }

    /**
     * Select method returns true if applet selection is supported.
     * @return boolean status of selection.
     */
    public boolean select()
    {
        // <PUT YOUR SELECTION ACTION HERE>
        // return status of selection
        return true;
    }

    /**
     * Deselect method called by the system in the deselection process.
     */
    public void deselect()
    {
        // <PUT YOUR DESELECTION ACTION HERE>
        return;
    }

    /**
     * Method processing an incoming APDU.
     * @see APDU
     * @param apdu the incoming APDU
     * @exception ISOException with the response bytes defined by ISO 7816-4
     */
    public void process(APDU apdu) throws ISOException
    {
        // ignore the applet select command dispached to the process
        if (selectingApplet())
            return;

        // get the APDU buffer
        byte[] apduBuffer = apdu.getBuffer();

        if (apduBuffer[ISO7816.OFFSET_CLA] == CLA_CARD_TEST) {
            switch ( apduBuffer[ISO7816.OFFSET_INS]) {
		case INS_CARD_READ_POLICY:
		    ReadPolicy(apdu); break;
		case INS_CARD_KEY_PUSH:
		    KeyPush(apdu); break;

                default : {
                    // The INS code is not supported by the dispatcher
                    ISOException.throwIt( ISO7816.SW_CLA_NOT_SUPPORTED ) ;
                    break;
                }
            }
        }
    }

    /**
     * Determine if APDU was sent via RFID interface
     * Returns true for contactless protocols
     */
    private boolean usingRF(APDU apdu)
    {
	short media=(short)(apdu.getProtocol() & (short)APDU.PROTOCOL_MEDIA_MASK);
	return (media==APDU.PROTOCOL_MEDIA_CONTACTLESS_TYPE_A) ||
		(media==APDU.PROTOCOL_MEDIA_CONTACTLESS_TYPE_B);
    }

    /**
     * Read back user policy
     * returns:
     *  short: total number of possible keys
     *  short: transfer buffer size
     *  byte:  use separate auth over RFID
     *  byte:  use OOB auth
     */
    private void ReadPolicy(APDU apdu)
    {
        byte[]	apdubuf = apdu.getBuffer();

        short	dataLen = apdu.setIncomingAndReceive();
        short   offset = (short)0;

        Util.arrayFillNonAtomic(apdubuf, offset, (short) 240, (byte) 0);

	Util.setShort(apdubuf, offset, m_maxKeys);
	offset = (short)(offset + 2);
	Util.setShort(apdubuf, offset, m_bSize);
	offset = (short)(offset + 2);
	apdubuf[offset] = m_separateAuth?(byte)1:(byte)0;
	++offset;
	apdubuf[offset] = m_oobAuth;
	++offset;

	apdu.setOutgoingAndSend((byte) 0, offset);
    }

    private void KeyPush(APDU apdu)
    {
	if(usingRF(apdu)) // Just for testing
	    ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        ISOException.throwIt( ISO7816.SW_INS_NOT_SUPPORTED ) ;
    }
}

