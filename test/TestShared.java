
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author petr
 */
public class TestShared {
    final static String TARGET_CARD_NAME = "NXP_JCOP_CJ3A081";
    
    final static String TEST_BASE_PATH = "test";
    final static String OUTPUT_LOG_BASE_PATH = "logs";
    
    
    public static String readFileAsString(String filePath) throws IOException {
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
    
    public static int executeShellCommand(String command, StringBuilder stderrString) {
        int exitVal = -1;
        try {
            Process proc = Runtime.getRuntime().exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null) { stderrString.append(line); }
            exitVal = proc.waitFor();
        }
        catch (IOException ex) {
            exitVal = -1;
        }
        catch (InterruptedException ex) {
            exitVal = -1;
        }
        return exitVal;
    }    
/*    
    public static int executeShellCommandApache(String command, StringBuilder stderrString) {
        int exitVal = -1;
        try {
            Executor exec = new DefaultExecutor();
            exec.setWorkingDirectory(TEST_BASE_PATH)
            CommandLine cl = new CommandLine(command);
            exitVal = exec.execute(cl);
        }
        catch (IOException ex) {
            exitVal = -1;
        }
        catch (InterruptedException ex) {
            exitVal = -1;
        }
        return exitVal;
    }   
*/    

    
    public static String formatCardPersonalizedCmdBatString(String batFileName) {
        return formatCardPersonalizedCmdBatString(batFileName, "", "");
    }
    public static String formatCardPersonalizedCmdBatString(String batFileName, String arg1) {
        return formatCardPersonalizedCmdBatString(batFileName, arg1, "");
    }
    public static String formatCardPersonalizedCmdBatString(String batFileName, String arg1, String outputLogFile) {
        return "cmd /c start " + batFileName + " " + arg1 + " " + OUTPUT_LOG_BASE_PATH + "\\" + TARGET_CARD_NAME + "_" + outputLogFile;
    }

    public static String formatCardPersonalizedOutputLogFilePath(String outputLogFile) {
        return OUTPUT_LOG_BASE_PATH + "\\" + TARGET_CARD_NAME + "_" + outputLogFile;
    }    
    
    public static boolean assertRegexAndExitVal(String output, String regex, int exitVal) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(output);

        if ((exitVal == 0) && matcher.find()) {
            return true;
        }
        else {
            return false;
        }
    }
}
