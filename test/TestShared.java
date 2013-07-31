
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.io.File;


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
 
            BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
             
            
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
    
    public static String executeShellCommandWithOutput(String command, String logFileName) {
        try {
            StringBuilder stdErr = new StringBuilder();
            int exitVal = TestShared.executeShellCommand(command, stdErr);
            if (exitVal != 0) { return ""; }
            String stdOut = TestShared.readFileAsString(TestShared.formatCardPersonalizedOutputLogFilePath(logFileName));

            return stdOut;
        }
        catch (IOException ex) {
            return "";
        }
    }
    public static boolean isRegexInOutput(String output, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(output);

        if (matcher.find()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public static String[] getReadOut(String command, String input) throws IOException, InterruptedException
    {
        Runtime rt = Runtime.getRuntime();
        FileWriter writer = new FileWriter( "d:\\Documents\\Develop\\MyPGPid\\test\\gpg_cardedit_generateRSA1024.stdin");
        writer.write(input);
        writer.close();
        Process p;
        String[] params = new String[1];
        params[0] = "";
        File f = new File("d:\\Documents\\Develop\\MyPGPid\\test\\");
        p = rt.exec(command, params, f);

        BufferedReader processOutput = new BufferedReader(new InputStreamReader(p.getInputStream()), 500000);
        BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        for (int i = 0; i < 100; i++) {
            processInput.write(input);
            processInput.flush();
        }
        
        ReadThread r = new ReadThread(processOutput);
        Thread th = new Thread(r);
        th.start();
        p.waitFor();
        r.stop();
        String s = r.res;

        p.destroy();
        th.join();
        return s.split("\n");
    }


    public static class ReadThread implements Runnable{

        BufferedReader reader;
        char[] buf = new char[100000];
        String res = "";
        boolean stop;
        public ReadThread(BufferedReader reader)
        {
            this.reader = reader;
            stop = false;
        }

        
        public void run() {
        res = "";

            while (!stop) {
                try {
                    reader.read(buf);
                    res += new String(buf);

                } catch (IOException ex) {
                    //Logger.getLogger(SennaReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public void stop()
        {
            stop = true;
        }
    }    
}
