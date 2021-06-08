package com.nicktheblackbeard.clientdata;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */


public class NFile{
    private String name;
    private String format;
    private ArrayList<String> qualities;
    private ArrayList<Integer> intQualities;

    private Integer maxQuality;

    private boolean HAS_240P = false;
    private boolean HAS_360P = false;
    private boolean HAS_480P = false;
    private boolean HAS_720P = false;
    private boolean HAS_1080P = false;

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

    /*
    @Override
    public String toString() {
        return "NFile{" +
                "name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", qualities=" + qualities +
                '}';
    }*/

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
            this.HAS_240P = true;
        }
        else if(quality.equals("360p")){
            this.intQualities.add(360);
            this.HAS_360P = true;
        }
        else if(quality.equals("480p")){
            this.intQualities.add(480);
            this.HAS_480P = true;
        }
        else if(quality.equals("720p")){
            this.intQualities.add(720);
            this.HAS_720P = true;
        }
        else if(quality.equals("1080p")){
            this.intQualities.add(1080);
            this.HAS_1080P = true;
        }
    }

    public ArrayList<Integer> getIntQualities() {
        return intQualities;
    }

    public boolean getHAS_240P() {
        return HAS_240P;
    }

    public boolean getHAS_360P() {
        return HAS_360P;
    }

    public boolean getHAS_480P() {
        return HAS_480P;
    }

    public boolean getHAS_720P() {
        return HAS_720P;
    }

    public boolean getHAS_1080P() {
        return HAS_1080P;
    }

    public void setHAS_240P(boolean HAS_240P) {
        this.HAS_240P = HAS_240P;
    }

    public void setHAS_360P(boolean HAS_360P) {
        this.HAS_360P = HAS_360P;
    }

    public void setHAS_480P(boolean HAS_480P) {
        this.HAS_480P = HAS_480P;
    }

    public void setHAS_720P(boolean HAS_720P) {
        this.HAS_720P = HAS_720P;
    }

    public void setHAS_1080P(boolean HAS_1080P) {
        this.HAS_1080P = HAS_1080P;
    }

    public void findMaxQuality(){
        this.maxQuality = Collections.max(this.intQualities);
    }
}
