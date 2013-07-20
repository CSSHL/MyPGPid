/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;

/**
 *
 * @author petr
 */
public class test_gpg extends TestCase {
    final static String CMD_LOG_FILE_GPG_CARDSTATUS = "cmd_log_gpg_cardstatus.txt";

    
    public test_gpg(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        // TODO: connect to card
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        // TODO: disconnect from card
    }
    public void test_gpg_card_status() {
        System.out.println("* test_gpg_card_status()");
        try {
            // Run bat file for on-card installation
            //String cmdCommand = "cmd /c start test\\gpg_cardstatus.bat " + CMD_LOG_FILE_GPG_CARDSTATUS;
            //String cmdCommand = "cmd /C \"echo TEST > testik";
            String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpg_cardstatus.bat", "", CMD_LOG_FILE_GPG_CARDSTATUS);
            StringBuilder stdErr = new StringBuilder(); 
            int exitVal = TestShared.executeShellCommand(cmdCommand, stdErr);
            String stdOut = TestShared.readFileAsString(TestShared.formatCardPersonalizedOutputLogFilePath(CMD_LOG_FILE_GPG_CARDSTATUS));

            // Search for 'Signature key ....:' string from --card-status output
            Pattern pattern = Pattern.compile("Signature key ....:");
            Matcher matcher = pattern.matcher(stdOut);

            if ((exitVal == 0) && matcher.find()) {
                // OK, sucesfully installed
                System.out.println(" --card-status output detected");
            }
            else {
                // Failed to display info
                assertTrue(false);
            }
        }
        catch (IOException ex) {
            assertTrue(false);
        }
    }      
    public void test_gpg_changePIN() {
        System.out.println("* test_gpg_changePIN()");
        // Run bat file for gpg PIN change
        // Parse output log, determine result
        assertTrue(false);
    }    
    public void test_gpg_generate_RSA1024() {
        System.out.println("* test_gpg_generate_RSA1024()");
        // Run bat file for gpg key generation
        // Parse output log, determine result
        assertTrue(false);
    }    
    public void test_gpg_sign_RSA1024() {
        System.out.println("* test_gpg_sign_RSA1024()");
        // Run bat file for gpg sign: gpg --clearsign --output myfile.sig --sign myfile
        // Parse output log, determine result
        assertTrue(false);
    }    
       
     public void test_gpg_verify_RSA1024() {
        System.out.println("* test_gpg_verify_RSA1024()");
        // Run bat file for gpg sign: gpg --verify myfile.sig

        // Parse output log, determine result
        assertTrue(false);
    }    
           
     public void test_gpg_encrypt_RSA1024() {
        System.out.println("* test_gpg_encrypt_RSA1024()");
        // Run bat file for gpg sign: gpg --output gpshell.log.gpg --recipient petr@svenda.com --encrypt gpshell.log

        // Parse output log, determine result
        assertTrue(false);
    }         
    
     public void test_gpg_decrypt_RSA1024() {
        System.out.println("* test_gpg_encrypt_RSA1024()");
        // Run bat file for gpg sign: gpg --decrypt gpshell.log.gpg

        // Parse output log, determine result
        assertTrue(false);
    }         
    
    
     public void test_gpg_auth_RSA1024() {
        System.out.println("* test_gpg_auth_RSA1024()");
        assertTrue(false);
    }       

}
