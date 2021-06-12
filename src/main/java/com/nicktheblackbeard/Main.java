package com.nicktheblackbeard;

import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.*;
import com.nicktheblackbeard.server.serverfiles.FilesCreator;
import com.nicktheblackbeard.server.serverfiles.ServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class Main {

    public static ArrayList<NFile> allFiles;

    static public Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args){
        log.info("Server is on...");
        try {
            FilesCreator filescreator = new FilesCreator(); //load and create all files that server will be able to stream
            allFiles = filescreator.getAllFiles(); //get these files
            Server server = new Server();
        } catch (ServerException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
