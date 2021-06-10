package com.nicktheblackbeard;

import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.*;
import com.nicktheblackbeard.server.serverfiles.FilesCreator;
import com.nicktheblackbeard.server.serverfiles.FilesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class Main {

    public static ArrayList<NFile> allFiles;

    static public Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        log.info("Server is on...");

        FilesCreator filescreator = new FilesCreator();
        allFiles = filescreator.getAllFiles();
        Server server = new Server();



        /*
        // creating list of commands
        List<String> commands = new ArrayList<String>();
        commands.add("ffplay"); // command
        //commands.add("-version");
        commands.add(FilesLoader.videosPath + "Amnesia_eurov-480p.mkv"); // command
        System.out.println(FilesLoader.videosPath + "Amnesia_eurov-720p.mp4");
        // command in Mac OS
        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(System.getProperty("user.dir") + "/ffmpeg/"));
        pb.redirectErrorStream(true);

        // startinf the process
        Process process = pb.start();

        //int ret = process.waitFor();

        //System.out.printf("Program exited with code: %d", ret);

        // for reading the output from stream

        BufferedReader stdInput
                = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }*/

    }

}
