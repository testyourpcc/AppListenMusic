package com.example.applistenmusic.singletons;

import android.util.Log;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Song;
import java.io.File;
import android.os.Environment;
import java.util.ArrayList;
import java.util.List;

public class DownloadListSingleton {

    private static volatile DownloadListSingleton instance;
    private List<Song> allDownloads;
    private boolean isDataLoaded = false;
    private DataLoadListener dataLoadListener;

    private DownloadListSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static DownloadListSingleton getInstance() {
        if (instance == null) {
            synchronized (DownloadListSingleton.class) {
                if (instance == null) {
                    instance = new DownloadListSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasDownload() {
        return allDownloads != null;
    }

    public synchronized List<Song> getAllDownloadIfExist(){
        return allDownloads;
    }
    public synchronized void setAllDownload(List<Song> songs){
        allDownloads = songs;
    }

    public synchronized void getAllDownload(DataLoadListener listener) {
        if (isDataLoaded) {
            // If data is already loaded, return it immediately
            listener.onDataLoaded(allDownloads);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.dataLoadListener = listener;
            fetchDownloadsFromDevice();
        }
    }

    private void fetchDownloadsFromDevice() {
        if (allDownloads == null) {
            allDownloads = new ArrayList<>();
        }

        // Get the directory for the user's public download directory.
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File songsFolder = new File(downloadFolder, "songs");

        // Get all the files in the songs directory
        File[] files = songsFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    // Assuming the name of the song is the name of the file
                    String songName = file.getName();
                    // Create a new Song object for each file
                    Song song = new Song();
                    song.setName(songName);
                    allDownloads.add(song);
                }
            }
        }

        isDataLoaded = true;
        if (dataLoadListener != null) {
            dataLoadListener.onDataLoaded(allDownloads); // Notify listener when data is loaded
        }
    }
}