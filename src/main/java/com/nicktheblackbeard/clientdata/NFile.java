package com.nicktheblackbeard.clientdata;

import java.util.ArrayList;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class NFile{
    private String name;
    private ArrayList<String> formatsList = null;
    private ArrayList<String> qualitiesList = null;

    private String max_quality;

    public NFile() {
        this.name = "";
        this.formatsList = new ArrayList<>();
        this.qualitiesList = new ArrayList<>();
    }

    public NFile(String name, ArrayList<String> formatsList, ArrayList<String> qualitiesList) {
        this.name = name;
        this.formatsList = formatsList;
        this.qualitiesList = qualitiesList;
    }

    public void addAnalysis(String analysis){
        this.qualitiesList.add(analysis);
    }

    public void addFormat(String format){
        this.formatsList.add(format);
    }




    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(ArrayList<String> formatsList) {
        this.formatsList = formatsList;
    }

    public void setAnalysis(ArrayList<String> analysis) {
        this.qualitiesList = analysis;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getFormat() {
        return this.formatsList;
    }

    public ArrayList<String> getAnalysis() {
        return this.qualitiesList;
    }
}
