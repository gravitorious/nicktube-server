package com.nicktheblackbeard.server;

import com.nicktheblackbeard.Main;
import com.nicktheblackbeard.clientdata.NClient;
import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.serverfiles.FilesCreator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
                String protocol = (String) input.readObject();
                System.out.println("Διάβασα το αρχείο: " + fileToPlay);
                System.out.println("Και το πρωτόκολλο είναι: " + protocol);
                if (protocol == "-1") break;
            }

            output.close();
            socket.close();
            if(speedResponse == 0) break;
        }
        server.close();
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
