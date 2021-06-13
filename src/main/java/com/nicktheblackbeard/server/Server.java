package com.nicktheblackbeard.server;

import com.nicktheblackbeard.Main;
import com.nicktheblackbeard.clientdata.NClient;
import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.serverfiles.FilesLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author nicktheblackbeard
 * 9/6/21
 */
public class Server{

    private ServerSocket server;
    private int port; //default port


    public Server() throws IOException, ClassNotFoundException, InterruptedException {
        this.port = 5000;
        for(;;){
            server = new ServerSocket(port);
            Main.log.info("Listening for client requests");
            Socket socket = server.accept(); //waiting for client
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            float speedResponse = (float) input.readObject(); //read download speed from client
            Main.log.debug("Client download speed: " + speedResponse);
            NClient newClient = new NClient(); //create client so we can save the data
            this.addFilesToClient(speedResponse, newClient);
            output.writeObject(newClient);
            for(;;) {
                String fileToPlay = (String) input.readObject(); //get file name from client
                if (fileToPlay.equals("-1")) break;
                String protocol = (String) input.readObject(); //get protocol from client
                Main.log.debug("Chosen file from client: " + fileToPlay);
                Main.log.debug("Chosen protocol from client: " + protocol);
                if(protocol.equals("TCP")){
                    Main.log.trace("Start streaming via TCP");
                    this.streamWithTCP(fileToPlay, output); //stream with TCP
                }
                else if(protocol.equals("UDP")){
                    Main.log.trace("Start streaming via UDP");
                    this.streamWithUDP(fileToPlay, output, input); //stream with UDP
                }
                else if(protocol.equals("RTP/UDP")){
                    Main.log.trace("Start streaming via RTP/UDP");
                    this.streamWithRTP(fileToPlay, output, input); //stream RTP/UDP
                }
            }
            output.close();
            input.close();
            socket.close();
            server.close();
        }
    }

    private void streamWithTCP(String fileName, ObjectOutputStream output) throws IOException{
        String[] split_by_dash = fileName.split("-", 2);
        String[] split_by_dot = split_by_dash[1].split("\\.", 2);
        String format = null;
        if(split_by_dot[1].equals("mkv")) format = "matroska";
        else if(split_by_dot[1].equals("avi")) format = "avi";
        else if(split_by_dot[1].equals("mp4")) format = "mp4";

        List<String> commands = new ArrayList<>();
        if(format.equals("matroska") || format.equals("avi")) {
            commands.add("ffmpeg"); // command
            commands.add("-i");
            commands.add(FilesLoader.videosPath + fileName);
            commands.add("-f");
            commands.add(format);
            commands.add("tcp://127.0.0.1:4010?listen");
        }
        else if(format.equals("mp4")){
            commands.add("ffmpeg"); // command
            commands.add("-i");
            commands.add(FilesLoader.videosPath + fileName);
            commands.add("-movflags");
            commands.add("frag_keyframe+empty_moov");
            commands.add("-f");
            commands.add("mp4");
            commands.add("tcp://127.0.0.1:4010?listen");
        }
        Main.log.debug("The file path is : " + FilesLoader.videosPath + fileName);
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(System.getProperty("user.dir") + "/ffmpeg/"));
        pb.redirectErrorStream(true);

        // startinf the process
        Process process = pb.start();
        output.writeObject("TCP");
    }

    private void streamWithUDP(String fileName, ObjectOutputStream output, ObjectInputStream input) throws IOException, InterruptedException, ClassNotFoundException {
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg"); // command
        commands.add("-i");
        commands.add(FilesLoader.videosPath + fileName);
        commands.add("-f");
        commands.add("mpegts");
        commands.add("udp://127.0.0.1:5000?listen");
        // command in Mac OS
        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(System.getProperty("user.dir") + "/ffmpeg/"));
        pb.redirectErrorStream(true);

        output.writeObject("UDP");
        TimeUnit.SECONDS.sleep(5);
        // startinf the process
        Process process = pb.start();

        String closed = (String) input.readObject(); //wait for client to close video
        process.destroy(); //stop streaming!
    }

    private void streamWithRTP(String fileName, ObjectOutputStream output, ObjectInputStream input) throws IOException, InterruptedException, ClassNotFoundException {
        List<String> commands = new ArrayList<>();
        System.out.println("θα παίξω το: " + FilesLoader.videosPath + fileName);
        commands.add("ffmpeg");
        commands.add("-re");
        commands.add("-i");
        commands.add(FilesLoader.videosPath + fileName);
        commands.add("-vcodec");
        commands.add("copy");
        commands.add("-an");
        commands.add("-f");
        commands.add("rtp");
        commands.add("-sdp_file");
        commands.add("video.sdp");
        commands.add("rtp://127.0.0.1:5006");
        commands.add("-vn");
        commands.add("-acodec");
        commands.add("copy");
        commands.add("-f");
        commands.add("rtp");
        commands.add("-sdp_file");
        commands.add("video.sdp");
        commands.add("rtp://127.0.0.1:5004");
        // command in Mac OS
        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(System.getProperty("user.dir") + "/ffmpeg/"));
        pb.redirectErrorStream(true);

        output.writeObject("RTP/UDP");
        // startinf the process
        Process process = pb.start();
        TimeUnit.SECONDS.sleep(3);
        String videoSdpFile = readFile(System.getProperty("user.dir") + "/ffmpeg/"+"video.sdp", Charset.defaultCharset());
        Main.log.debug("video.sdp contains: " + videoSdpFile);
        output.writeObject(videoSdpFile); //send the file
        String answer = (String) input.readObject(); //wait for the answer from client
        TimeUnit.SECONDS.sleep(5);

        String closed = (String) input.readObject(); //wait for client to close video
        process.destroy(); //stop streaming!
    }


    /*
        maxQuality has the max quality that client is able to get base on his download speed
        we will send all files that has quality less or equal than maxQuality.
     */

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


    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
