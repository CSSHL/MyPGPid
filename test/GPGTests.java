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
public class GPGTests extends TestCase {
    final static String CMD_LOG_FILE_GPG_CARDSTATUS = "cmd_log_gpg_cardstatus.txt";
    final static String CMD_LOG_FILE_GPG_GENERATERSA1024 = "cmd_log_gpg_generateRSA1024.txt";
    
/*
--sign
--detach-sign
--encrypt
--decrypt

 */
    
    
    public GPGTests(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    

    
    public void test_gpg_card_status() {
        System.out.println("* MyPGPid::test_gpg_card_status()");
        // Run bat file for gpg --card-status
        String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpg_cardstatus.bat", "", CMD_LOG_FILE_GPG_CARDSTATUS);
        String output = TestShared.executeShellCommandWithOutput(cmdCommand, CMD_LOG_FILE_GPG_CARDSTATUS);
        // Search for 'Signature key ....:' string from --card-status output
        assertTrue(TestShared.isRegexInOutput(output,  
            "Signature key ....:"));            

    }      
    public void test_gpg_changePIN() {
        System.out.println("* MyPGPid::test_gpg_changePIN()");
        // Run bat file for gpg PIN change
        String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpg_changepin.bat", "", "");
        TestShared.executeShellCommandWithOutput(cmdCommand, "");
        
        // TODO: Try to login with old (invalid) PIN - retry counter should decrease
        // TODO: Try to login with new (valid) PIN - retry counter should increase back to 3
        
        
        // Parse output log, determine result
        assertTrue(false);
    }    
    public void test_gpg_generate_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_generate_RSA1024()");
        
        // Generate keys
        //String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpg_cardedit_generateRSA1024.bat", "", "");
        //String output = TestShared.executeShellCommandWithOutput(cmdCommand, "");
        
        String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpg_cardstatus.bat", "", CMD_LOG_FILE_GPG_GENERATERSA1024);
        String output = TestShared.executeShellCommandWithOutput(cmdCommand, CMD_LOG_FILE_GPG_GENERATERSA1024);
        
        assertTrue(TestShared.isRegexInOutput(output, "Signature counter : 5"));     
        assertTrue(TestShared.isRegexInOutput(output, "Signature key ....:[ 0-9A-F.]+?\r*\n      created ....: "));     
        assertTrue(TestShared.isRegexInOutput(output, "Encryption key....:[ 0-9A-F.]+?\r*\n      created ....: "));     
        assertTrue(TestShared.isRegexInOutput(output, "Authentication key:[ 0-9A-F.]+?\r*\n      created ....: "));   
        assertTrue(TestShared.isRegexInOutput(output, "General key info..: pub  1024R/[0-9A-F.]+? [-0-9]+? MyPGPid <test@test.org>"));   
        
    }    
    public void test_gpg_sign_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_sign_RSA1024()");
        // Run bat file for gpg sign: gpg --clearsign --output myfile.sig --sign myfile
        // Parse output log, determine result
        assertTrue(false);
    }    
       
     public void test_gpg_verify_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_verify_RSA1024()");
        // Run bat file for gpg sign: gpg --verify myfile.sig

        // Parse output log, determine result
        assertTrue(false);
    }    
           
     public void test_gpg_encrypt_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_encrypt_RSA1024()");
        // Run bat file for gpg sign: gpg --output gpshell.log.gpg --recipient petr@svenda.com --encrypt gpshell.log

        // Parse output log, determine result
        assertTrue(false);
    }         
    
     public void test_gpg_decrypt_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_encrypt_RSA1024()");
        // Run bat file for gpg sign: gpg --decrypt gpshell.log.gpg

        // Parse output log, determine result
        assertTrue(false);
    }         
    
    
     public void test_gpg_auth_RSA1024() {
        System.out.println("* MyPGPid::test_gpg_auth_RSA1024()");
        assertTrue(false);
    }       

}
