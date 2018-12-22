package com.example.franco.testids;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
So this is literally the code needed in order to run an adb command on the phone.
There is typically no restriction PROVIDED you have root access to the device.
 */
public class AdbCommands {

    public AdbCommands() {

    }
    public String runPSCommand() {

        StringBuffer output = new StringBuffer();
        Process p;
        try {
            //su -c allows commands to run in superuser, issue is, this way does not allow us to read files
            p = Runtime.getRuntime().exec("su -c ps");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                //dictates how the output from the command will be formatted, self-explanatory just creates a new line for every output
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;

    }
    public String runStraceCommand(String PID) {

        StringBuffer output = new StringBuffer();
        Process p;
        try {
            //su -c allows commands to run in superuser, issue is, this way does not allow us to read files
            p = Runtime.getRuntime().exec("su -c strace -p " + PID + " &> data/local/tmp/logs.txt");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                //dictates how the output from the command will be formatted, self-explanatory just creates a new line for every output
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;

    }

}