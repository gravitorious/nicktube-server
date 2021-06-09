package com.nicktheblackbeard.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author nicktheblackbeard
 * 9/6/21
 */
public class Server{

    private ServerSocket server;
    private int port = 5000; //default port

    public Server() throws IOException, ClassNotFoundException {

        server = new ServerSocket(port);
        for(;;){
            System.out.println("Listening for client requests");
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            float msgReceived = (float) ois.readObject();
            System.out.println("I got downloadspeed: " + msgReceived);
            ois.close();
            socket.close();
            if(msgReceived == 0) break;
        }
        server.close();

    }





}
