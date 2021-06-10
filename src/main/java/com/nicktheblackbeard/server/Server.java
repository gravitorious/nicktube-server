package com.nicktheblackbeard.server;

import com.nicktheblackbeard.Main;
import com.nicktheblackbeard.clientdata.NClient;
import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.serverfiles.FilesCreator;
import com.nicktheblackbeard.server.serverfiles.FilesLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nicktheblackbeard
 * 9/6/21
 */
public class Server{

    private ServerSocket server;
    private int port = 5000; //default port


    public Server() throws IOException, ClassNotFoundException {
        //allFiles = FilesCreator.
        server = new ServerSocket(port);
        for(;;){
            System.out.println("Listening for client requests");
            Socket socket = server.accept();
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            float speedResponse = (float) input.readObject();
            System.out.println("I got downloadspeed: " + speedResponse);
            NClient newClient = new NClient();
            this.addFilesToClient(speedResponse, newClient);
            output.writeObject(newClient);
            for(;;) {
                String fileToPlay = (String) input.readObject();
                if (fileToPlay.equals("-1")) break;
                String protocol = (String) input.readObject();
                System.out.println("Διάβασα το αρχείο: " + fileToPlay);
                System.out.println("Και το πρωτόκολλο είναι: " + protocol);
                if(protocol.equals("TCP")){
                    this.streamWithTCP(fileToPlay, output);
                }

            }

            output.close();
            socket.close();
            if(speedResponse == 0) break;
        }
        server.close();
    }

    private void streamWithTCP(String fileName, ObjectOutputStream output) throws IOException {
        String[] split = fileName.split("\\."); //split[1] contains the format
        List<String> commands = new ArrayList<String>();
        commands.add("ffmpeg"); // command
        commands.add("-i");
        commands.add(FilesLoader.videosPath + fileName);
        commands.add("-f");
        commands.add(split[1]);
        commands.add("tcp://127.0.0.1:4001?listen");
        System.out.println(FilesLoader.videosPath + "Amnesia_eurov-720p.mp4");
        // command in Mac OS
        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(System.getProperty("user.dir") + "/ffmpeg/"));
        pb.redirectErrorStream(true);

        // startinf the process
        Process process = pb.start();
        output.writeObject("TCP");

        //int ret = process.waitFor();

        //System.out.printf("Program exited with code: %d", ret);

        // for reading the output from stream

        BufferedReader stdInput
                = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }

    private void addFilesToClient(float speed, NClient newClient){
        int maxQuality = this.returnMaxQualityForClient(speed);
        for(NFile file : Main.allFiles){
            NFile newFile = new NFile();
            newFile.copyFile(file);
            for(int quality : file.getIntQualities()){//delete all qualities that must not be send
                if(quality  <= maxQuality){
                    if(quality == 240){
                        newFile.addQuality("240p");
                    }
                    else if(quality == 360){
                        newFile.addQuality("360p");
                    }
                    else if(quality == 480){
                        newFile.addQuality("480p");
                    }
                    else if(quality == 720){
                        newFile.addQuality("720p");
                    }
                    else if(quality == 1080){
                        newFile.addQuality("1080p");
                    }
                }
            }
            if(newFile.getFormat().equals("mp4")) newClient.addMp4File(newFile);
            else if(newFile.getFormat().equals("avi")) newClient.addAviFile(newFile);
            else if(newFile.getFormat().equals("mkv")) newClient.addMkvFile(newFile);
        }
    }

    private int returnMaxQualityForClient(float speed){
        if(speed < 1000) return 240;
        else if (speed < 2000) return 360;
        else if (speed < 4000) return 480;
        else if (speed < 6000) return 720;
        else return 1080;
    }





}
