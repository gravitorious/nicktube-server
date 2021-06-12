package com.nicktheblackbeard.clientdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */



/*
    Store the data for each file. Implements Serializable interface so we can transfer it via network
 */

public class NFile implements Serializable{
    private static final long serialVersionUID = -5399605122490343339L;
    private String name;
    private String format;
    private ArrayList<String> qualities;
    private ArrayList<Integer> intQualities; //same qualities but with integer type

    private Integer maxQuality;

    public NFile(){
        this.name = "";
        this.format = "";
        this.qualities = new ArrayList<>();
        this.intQualities = new ArrayList<>();
        this.maxQuality = -1;
    }

    public NFile(String name, String quality, String format) {
        this.name = name;
        this.format = format;
        this.qualities = new ArrayList<>();
        this.qualities.add(quality);
        this.intQualities = new ArrayList<>();
        this.addLastQualityToIntList(quality);
        this.findMaxQuality();
    }

    public NFile(String name, String format){
        this.name = name;
        this.format = format;
        this.qualities = new ArrayList<>();
        this.intQualities = new ArrayList<>();
    }

    public void addQuality(String quality){
        this.qualities.add(quality);
        this.addLastQualityToIntList(quality);
        this.findMaxQuality();
    }

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public Integer getMaxQuality() {
        return this.maxQuality;
    }

    public ArrayList<String> getQualities() {
        return this.qualities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format){
        this.format = format;
    }

    public void setQualities(ArrayList<String> qualities) {
        this.qualities = qualities;
    }

    @Override
    public String toString() {
        return "NFile{" +
                "name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", qualities=" + qualities +
                ", intQualities=" + intQualities +
                ", maxQuality='" + maxQuality + '\'' +
                '}';
    }

    /*
        takes the last element from (String) qualities list and put it as an int to integer qualities list
     */

    public void addLastQualityToIntList(String quality){
        if(quality.equals("240p")){
            this.intQualities.add(240);
        }
        else if(quality.equals("360p")){
            this.intQualities.add(360);
        }
        else if(quality.equals("480p")){
            this.intQualities.add(480);
        }
        else if(quality.equals("720p")){
            this.intQualities.add(720);
        }
        else if(quality.equals("1080p")){
            this.intQualities.add(1080);
        }
    }

    public ArrayList<Integer> getIntQualities() {
        return intQualities;
    }


    public void findMaxQuality(){
        this.maxQuality = Collections.max(this.intQualities);
    }

    public void copyFile(NFile file){
        this.name = file.getName();
        this.format = file.getFormat();
    }
}
