package com.nicktheblackbeard.server.serverfiles;

import com.nicktheblackbeard.Main;
import com.nicktheblackbeard.clientdata.NFile;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author nicktheblackbeard
 * 7/6/21
 */

/*
    We use FilesCreator to create all missing files.
    allFiles : this list contains ALL files that server will read and create. we will use this list for everything
    newFormatFiles : this list contains only the files that we needed to create new format. In the end we copy this list
    to the allFiles list.
 */


public class FilesCreator {
    private ArrayList<NFile> allFiles;
    private ArrayList<NFile> newFormatFiles;

    private FFmpeg ffmpeg = null;
    private FFprobe ffprobe = null;

    private String ffmpegPath;

    private FFmpegBuilder builder;
    private FFmpegExecutor executor;

    //we use this attribute in findMaxQualityFromFileName to save the file that we will use for transcoding
    private NFile fileWithMaxQuality;

    private boolean has_mp4 = false;
    private boolean has_mkv = false;
    private boolean has_avi = false;



    public FilesCreator() throws IOException, ServerException{
        this.loadFFmpegDir();
        Main.log.info("Load files from directory...");
        com.nicktheblackbeard.server.serverfiles.FilesLoader filesLoader = new FilesLoader();
        this.allFiles = filesLoader.getAllFiles();
        this.newFormatFiles = new ArrayList<>();
        this.printAllFilesLog();
        Main.log.info("Create files...");
        this.addNewFiles();
    }

    /*
        load ffmpeg directory that contains ffmpeg, ffplay and ffprobe.
        Also create the executor
     */
    public void loadFFmpegDir() throws IOException {
        this.ffmpegPath = System.getProperty("user.dir") + "/ffmpeg/";
        Main.log.debug("ffmpeg directory is: " + this.ffmpegPath);
        this.ffmpeg = new FFmpeg(ffmpegPath + "ffmpeg");
        this.ffprobe = new FFprobe(ffmpegPath + "ffprobe");
        this.builder = new FFmpegBuilder();
        this.executor = new FFmpegExecutor(ffmpeg, ffprobe);
    }


    /*
        for every file we are searching for the maximum quality. Then we are creating the missing qualities.
        After that, we are creating files with the missing formats base on the max quality.
        testedFilesNames : contains all file names that we have already created new files
        testingFiles : contains filenames that we checked for new qualities. This will have maximum three size
        for each file, cause formats are three (mkv, mp4, avi)/
     */
    public void addNewFiles(){
        boolean result;
        Integer max;
        ArrayList<String> testedFilesNames = new ArrayList<>();
        ArrayList<NFile> testingFiles;
        for(NFile checkingFile : allFiles){
            this.changeToFalse();
            result = this.searchFileName(testedFilesNames, checkingFile.getName()); //check if we have already tested this file
            if(result) continue;
            testedFilesNames.add(checkingFile.getName());
            testingFiles = new ArrayList<>();
            max = checkingFile.getMaxQuality();
            fileWithMaxQuality = checkingFile;
            max = this.findMaxQualityFromFileName(checkingFile, max, testingFiles);
            //in this stage we found the max quality from a file (without caring the format)

            //first stage: create new files from existing formats
            for(NFile file : testingFiles){
                this.createNewFiles(file, max);
            }
            if(testingFiles.size() == 3) continue; //we don't need to make new files with new format
            else{
                /*
                search the new formats that we will need to create
                 */
                for(NFile file : testingFiles){
                    String format = file.getFormat();
                    if(format.equals("mp4")) this.has_mp4 = true;
                    else if(format.equals("mkv")) this.has_mkv = true;
                    else if(format.equals("avi")) this.has_avi = true;
                }
                if(!this.has_mp4){
                    NFile new_file = new NFile(checkingFile.getName(), "mp4");
                    this.newFormatFiles.add(new_file); //put new file to memory
                    createNewFiles(new_file, max);
                }
                if(!this.has_mkv){
                    NFile new_file = new NFile(checkingFile.getName(), "mkv");
                    this.newFormatFiles.add(new_file); //put new file to memory
                    createNewFiles(new_file, max);
                }
                if(!this.has_avi){
                    NFile new_file = new NFile(checkingFile.getName(), "avi");
                    this.newFormatFiles.add(new_file); //put new file to memory
                    createNewFiles(new_file, max);
                }
            }
        }
        this.putNewFilesToAllFiles(); //put to allFiles list the new created files

    }

    /*
        We are trying to find the max quality from a file name (not checking the format)
        We are checking all files that have the same name with checkingFile
        We will return the max quality and we will keep the file with the max quality. We will need this file
        to make the transcoding
    */
    private Integer findMaxQualityFromFileName(NFile checkingFile, Integer max, ArrayList<NFile> testingFiles){
        boolean result;
        for(NFile otherFile : allFiles){
            result = otherFile.getName().equals(checkingFile.getName());
            if(!result) continue;
            testingFiles.add(otherFile);
            if(otherFile.getMaxQuality() > max){
                max = otherFile.getMaxQuality();
                this.fileWithMaxQuality = otherFile;
            }
        }
        return max;
    }

    /*
        create new files with new quality
     */
    private void createNewFiles(NFile file, Integer max){
        String new_name = null;
        String old_name = this.fileWithMaxQuality.getName()+"-"+max+"p."+this.fileWithMaxQuality.getFormat();
        ArrayList<Integer> file_qualities = file.getIntQualities();
        if(!file_qualities.contains(240) && 240 <= max){
            file.addQuality("240p");
            new_name = file.getName()+"-240p."+file.getFormat();
            this.createNewQuality(old_name, new_name, 426, 240);
        }

        if(!file_qualities.contains(360) && 360 <= max){
            file.addQuality("360p");
            new_name = file.getName()+"-360p."+file.getFormat();
            this.createNewQuality(old_name, new_name, 640, 360);
        }

        if(!file_qualities.contains(480) && 480 <= max){
            file.addQuality("480p");
            new_name = file.getName()+"-480p."+file.getFormat();
            this.createNewQuality(old_name, new_name, 854, 480);
        }

        if(!file_qualities.contains(720) && 720 <= max){
            file.addQuality("720p");
            new_name = file.getName()+"-720p."+file.getFormat();
            this.createNewQuality(old_name, new_name, 1280, 720);
        }
        if(new_name != null && new_name.length() != 0){
            Main.log.debug("Created file : " + new_name);
        }

    }

    /*
        create the new file with new quality
     */
    private void createNewQuality(String oldName, String newName, int width, int height){
        this.builder = new FFmpegBuilder()
                .setInput(FilesLoader.videosPath + oldName)
                .addOutput(FilesLoader.videosPath + newName)
                .setVideoResolution(width, height)
                .done();
        this.executor.createJob(this.builder).run();
    }



    private boolean searchFileName(ArrayList<String> testedFilesNames, String name){
        if(testedFilesNames.contains(name)) return true;
        else return false;
    }

    private void printAllFilesLog(){
        for(NFile file : this.allFiles){
            Main.log.info("Read file: " + file.toString());
        }
    }

    private void changeToFalse(){
        this.has_avi = false;
        this.has_mp4 = false;
        this.has_mkv = false;
    }

    private void putNewFilesToAllFiles(){
        Iterator<NFile> it = this.newFormatFiles.iterator();

        while (it.hasNext()) {
            this.allFiles.add(it.next());
        }
    }

    public ArrayList<NFile> getAllFiles() {
        return allFiles;
    }
}
