package com.nicktheblackbeard.server;

import com.nicktheblackbeard.clientdata.NFile;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author nicktheblackbeard
 * 7/6/21
 */



public class FilesCreator {
    private ArrayList<NFile> allFiles;

    private FFmpeg ffmpeg = null;
    private FFprobe ffprobe = null;

    private String ffmpegPath;

    private FFmpegBuilder builder;
    private FFmpegExecutor executor;

    //we use this attribute in findMaxQualityFromFileName to save the file that we will use for transcoding
    private NFile fileWithMaxQuality;


    public FilesCreator() throws IOException {
        this.loadFFmpegDir();
        FilesLoader filesLoader = new FilesLoader();
        this.allFiles = filesLoader.getAllFiles();
        this.printAllFiles();
        this.addNewFiles();




    }


    public void loadFFmpegDir() throws IOException {
        this.ffmpegPath = System.getProperty("user.dir") + "/ffmpeg/";
        this.ffmpeg = new FFmpeg(ffmpegPath + "ffmpeg");
        this.ffprobe = new FFprobe(ffmpegPath + "ffprobe");
        this.builder = new FFmpegBuilder();
        this.executor = new FFmpegExecutor(ffmpeg, ffprobe);
    }


    /*
        for every file we are searching for the maximum quality. Then we
     */
    public void addNewFiles(){
        boolean result;
        Integer max;
        ArrayList<String> testedFilesNames = new ArrayList<>(); //contains all file names that we have already create new files
        ArrayList<NFile> testingFiles; //all files that we will create new files. it will contains max 3 objects each time
        for(NFile checkingFile : allFiles){
            result = this.searchFileName(testedFilesNames, checkingFile.getName());
            if(result) continue;
            testedFilesNames.add(checkingFile.getName());
            testingFiles = new ArrayList<>();
            testingFiles.add(checkingFile);
            max = checkingFile.getMaxQuality();
            fileWithMaxQuality = checkingFile;
            max = this.findMaxQualityFromFileName(checkingFile, max, testingFiles);
            System.out.println("μεγαλύτερη ανάλυση έιναι: " + max);
            System.out.println("Το αρχεί ομε την μεγαλύτερη ανάλυση: " + fileWithMaxQuality.getName()+fileWithMaxQuality.getFormat());
            //in this stage we found the max quality from a file (without caring the format)
            //System.out.println("=====");
            //this.printAllFiles(testingFiles);
            for(NFile file : testingFiles){
                this.createNewFiles(file, max);
            }


        }



    }

    private void createNewFiles(NFile file, Integer max){
        String new_name;
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

        /*
        if(!file_qualities.contains(1080) && 1080 <= max){
            file.addQuality("1080p");
            new_name = file.getName()+"-1080p."+file.getFormat();
            this.createNewQuality(old_name, new_name, 1920, 1080);
        }*/

    }


    private void createNewQuality(String oldName, String newName, int width, int height){
        this.builder = new FFmpegBuilder()
                .setInput(FilesLoader.videosPath + oldName)
                .addOutput(FilesLoader.videosPath + newName)
                .setVideoResolution(width, height)
                .done();
        this.executor.createJob(this.builder).run();
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
            if(otherFile.getMaxQuality() > max){
                max = otherFile.getMaxQuality();
                testingFiles.add(otherFile);
                this.fileWithMaxQuality = otherFile;
            }
        }
        return max;
    }

    private boolean searchFileName(ArrayList<String> testedFilesNames, String name){
        if(testedFilesNames.contains(name)) return true;
        else return false;
    }

    private void printAllFiles(){
        for(NFile file : this.allFiles){
            System.out.println(file.toString());
        }
    }

    private void printAllFiles(ArrayList<NFile> files){
        for(NFile file : files){
            System.out.println(file.toString());
        }
    }


}
