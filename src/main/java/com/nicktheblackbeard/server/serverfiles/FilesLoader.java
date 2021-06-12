package com.nicktheblackbeard.server.serverfiles;

import com.nicktheblackbeard.clientdata.*;
import com.nicktheblackbeard.Main;


import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */


/*
    Load existing files (from videos folder) to memory
 */
public class FilesLoader {
    private ArrayList<NFile> allFiles;
    //private ArrayList<NClient> clients = null;

    public static String videosPath = System.getProperty("user.dir") + "/videos/";

    /*
    default constructor creates missing files and put them to memory
     */
    public FilesLoader() throws ServerException{
        Main.log.debug("Videos folder path is : " + videosPath);
        this.allFiles = new ArrayList<>();
        ArrayList<String> file_names = this.getFilesFromDirectory();
        if(file_names.size() == 0) throw new ServerException(1, "Files not found...");
        Main.log.debug("Server found these files: " +file_names.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));
        this.addInitialFilesToMemory(file_names);
    }

    private ArrayList<String> getFilesFromDirectory() throws ServerException {
        ArrayList<String> file_names = new ArrayList<>();

        File[] files = new File(videosPath).listFiles();
        if(files == null) throw new ServerException(2, "Videos directory is not found");

        for (File file : files) {
            if (file.isFile()) {
                file_names.add(file.getName());
            }
        }
        return file_names;
    }

    private NFile searchFileByNameAndFormat(String name, String format){
        for(NFile nfile : this.allFiles){
            if(nfile.getName().equals(name) && nfile.getFormat().equals(format)) return nfile;
        }
        return null;
    }

    /*
        we put every file from videos to memory.
        split_by_dash[0] : name of the file
        split_by_dot[0] : quality
        split_by_dot[1] : format
     */
    private void addInitialFilesToMemory(ArrayList<String> file_names){

        NFile return_file;
        String[] split_by_dash;
        String[] split_by_dot;
        for(String file : file_names){
            /*
                we assume that the name of the file has only one dash. Before the dash, is the name of the file
             */
            split_by_dash = file.split("-", 2);
            split_by_dot = split_by_dash[1].split("\\.", 2);
            return_file = this.searchFileByNameAndFormat(split_by_dash[0], split_by_dot[1]);
            if(return_file != null){
                return_file.addQuality(split_by_dot[0]);
            }else {
                this.allFiles.add(new NFile(split_by_dash[0], split_by_dot[0], split_by_dot[1]));
            }
        }
    }

    public ArrayList<NFile> getAllFiles() {
        return allFiles;
    }
}
