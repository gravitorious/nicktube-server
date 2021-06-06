package com.nicktheblackbeard.server;

import com.nicktheblackbeard.clientdata.*;
import com.nicktheblackbeard.Main;


import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class NServer {
    private ArrayList<NFile> allFiles = null;
    private ArrayList<NClient> clients = null;

    private FFmpeg ffmpeg = null;
    private FFprobe ffprobe = null;

    private String videosPath = System.getProperty("user.dir") + "/videos/";

    /*
    default constructor creates missing files and put them to memory
     */
    public NServer(){
        this.allFiles = new ArrayList<>();
        this.clients = new ArrayList<>();
        ArrayList<String> file_names = this.getFilesFromDirectory();
        Main.log.debug("Server found these files: " +file_names.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));
        this.findNewFiles(file_names);


    }

    private void loadFFmpeg(){

    }

    private void loadFFprobe(){

    }

    private ArrayList<String> getFilesFromDirectory(){
        ArrayList<String> file_names = new ArrayList<String>();

        File[] files = new File(videosPath).listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                file_names.add(file.getName());
            }
        }
        return file_names;
    }

    private boolean searchFileByName(String name){
        for(NFile nfile : allFiles){
            if(nfile.getName().equals(name)) return true;
        }
        return false;
    }



    private void findNewFiles(ArrayList<String> file_names){

        boolean result;
        String[] split_by_dash;
        String quality;
        for(String file : file_names){
            /*
                we assume that the name of the file has only one dash. Before the dash, is the name of th file
             */
            split_by_dash = file.split("-", 2);
            result = this.searchFileByName(split_by_dash[0]);
            if(result) continue;
            quality = split_by_dash[1].substring(split_by_dash[1].length() - 3); //last 3 chars define the format of files
            /*
            search the max quality for this file name while adding files to memory
             */







        }


    }


}
