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
        try {
            // Run bat file for on-card installation
            //String cmdCommand = "cmd /c start test\\gpshell_install.bat " + "install_" + TestShared.TARGET_CARD_NAME + ".txt " + CMD_LOG_FILE_INSTALL;
            String cmdCommand = TestShared.formatCardPersonalizedCmdBatString("gpshell_install.bat", "install_" + TestShared.TARGET_CARD_NAME + ".txt", CMD_LOG_FILE_INSTALL);
            StringBuilder stdErr = new StringBuilder(); 
            int exitVal = TestShared.executeShellCommand(cmdCommand, stdErr);
            String stdOut = TestShared.readFileAsString(TestShared.formatCardPersonalizedOutputLogFilePath(CMD_LOG_FILE_INSTALL));

            // Search for install command and expected response 0x9000
            Pattern pattern = Pattern.compile("Wrapped command --> 84E60C00[0-9A-F.]+?\r*\nResponse <-- 009000");
            Matcher matcher = pattern.matcher(stdOut);
            
            if ((exitVal == 0) && matcher.find()) {
                // OK, sucesfully installed
                System.out.println(" applet sucesfully installed");
            }
            else {
                // Failed to install
                assertTrue(false);
                System.out.println(stdOut);
            }
        }
        catch (IOException ex) {
            assertTrue(false);
        }
    }
/*    
    public void testSelect() {
        System.out.println("* MyPGPid:testSelect()");
        // Send select command
        assertTrue(false);
    }
    */
    

}
