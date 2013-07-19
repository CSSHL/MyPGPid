/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import junit.framework.TestCase;
/**
 *
 * @author petr
 */
public class test_install extends TestCase {
    
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
            Runtime.getRuntime().exec("cmd /c start test\\install.bat");
            
            String result = readFileAsString("test\\install_log_gpshell.txt");
            // Search for install command and expected response
            if (result.matches("Wrapped command --> 84E60C00.*Response <-- 009000")) {
                // OK, sucesfully installed
            }
            else {
                // Failed to install
                assertTrue(false);
            }
        }
        catch (IOException ex) {
            assertTrue(false);
        }

        // Parse output log, determine result
        assertTrue(false);
    }
/*    
    public void testSelect() {
        System.out.println("* MyPGPid:testSelect()");
        // Send select command
        assertTrue(false);
    }
    */
    
    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}
