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
public class test_install extends TestCase {
    final static String CMD_LOG_FILE_INSTALL = "cmd_log_gpshell_install.txt";
   
    public test_install(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testInstall() {
        System.out.println("* MyPGPid:testInstall()");
        // Run bat file for on-card installation
        String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpshell_install.bat", "install_" + TestShared.TARGET_CARD_NAME + ".txt", CMD_LOG_FILE_INSTALL);
        String output = TestShared.executeShellCommandWithOutput(cmdCommand, CMD_LOG_FILE_INSTALL);
        // Search for install command and expected response 0x9000
        assertTrue(TestShared.isRegexInOutput(output, 
                "Wrapped command --> 84E60C00[0-9A-F.]+?\r*\nResponse <-- 009000"));
    }
/*    
    public void testSelect() {
        System.out.println("* MyPGPid:testSelect()");
        // Send select command
        assertTrue(false);
    }
    */
    

}
