package com.nicktheblackbeard.clientdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author nicktheblackbeard
 * 6/6/21
 */
public class NClient implements Serializable {
    private static final long serialVersionUID = -5399605122490343339L;
    ArrayList<NFile> mkvFiles;
    ArrayList<NFile> mp4Files;
    ArrayList<NFile> aviFiles;

    public NClient(){
        this.mkvFiles = new ArrayList<>();
        this.mp4Files = new ArrayList<>();
        this.aviFiles = new ArrayList<>();
    }

    public void addMkvFile(NFile file){
        this.mkvFiles.add(file);
    }

    public void addMp4File(NFile file){
        this.mp4Files.add(file);
    }

    public void addAviFile(NFile file){
        this.aviFiles.add(file);
    }


}
