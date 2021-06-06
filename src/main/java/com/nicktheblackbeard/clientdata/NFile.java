package com.nicktheblackbeard.clientdata;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class NFile{
    private String name;
    private String format;
    private String analysis;

    public NFile(){
        this.name = "";
        this.format = "";
        this.analysis = "";
    }

    public NFile(String name, String format, String analysis) {
        this.name = name;
        this.format = format;
        this.analysis = analysis;
    }

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public String getAnalysis() {
        return this.analysis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
