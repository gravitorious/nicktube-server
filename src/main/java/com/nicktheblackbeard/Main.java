package com.nicktheblackbeard;

import com.nicktheblackbeard.clientdata.NFile;
import com.nicktheblackbeard.server.*;
import com.nicktheblackbeard.server.serverfiles.FilesCreator;
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        log.info("Server is on...");
        FilesCreator filescreator = new FilesCreator();
        allFiles = filescreator.getAllFiles();
        Server server = new Server();

    }

}
