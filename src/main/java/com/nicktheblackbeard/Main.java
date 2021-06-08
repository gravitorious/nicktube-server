package com.nicktheblackbeard;

import com.nicktheblackbeard.server.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class Main {

    static public Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        log.info("Server is on...");
        FilesCreator filescreator = new FilesCreator();

    }

}
