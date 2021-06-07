package com.nicktheblackbeard.clientdata;

import java.util.ArrayList;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class NFile{
    private String name;
    private String format;
    private ArrayList<String> qualities;

    public NFile(){
        this.name = "";
        this.format = "";
        this.qualities = new ArrayList<>();
    }

    public NFile(String name, String quality, String format) {
        this.name = name;
        this.format = format;
        this.qualities = new ArrayList<>();
        this.qualities.add(quality);
    }

    public void addQuality(String quality){
        this.qualities.add(quality);
    }

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
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
                '}';
    }
}
