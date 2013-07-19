/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import junit.framework.TestCase;

/**
 *
 * @author petr
 */
public class test_gpg extends TestCase {
    
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
        // Run bat file for gpg --card-status
        // Parse output log, determine result
        assertTrue(false);
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
