package com.example.spaceimages;

import android.graphics.Bitmap;
import java.util.*;

public class DataSingleton {
    private static DataSingleton instance;
    private final Map<String, ImageData> imageCache;
    private final List<HistoryEntry> downloadHistory;

    public static synchronized DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

    private DataSingleton() {
        imageCache = new HashMap<>();
        downloadHistory = new ArrayList<>();
    }

    public void addImageData(String filename, ImageData data) {
        imageCache.put(filename, data);
    }

    public ImageData getImageData(String filename) {
        return imageCache.get(filename);
    }

    public Collection<ImageData> getAllImages() {
        return imageCache.values();
    }

    public static String extractFilename(String url) {
        if (url == null || url.isEmpty()) return "";

        // Handle URLs with query parameters
        int queryIndex = url.indexOf('?');
        if (queryIndex != -1) {
            url = url.substring(0, queryIndex);
        }

        // Extract after last slash
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return url; // Return original if no slashes found
    }
    public void updateImageData(ImageData updatedData) {
        // Replace existing data with updated version
        imageCache.put(extractFilename(updatedData.src), updatedData);
    }
    public void logImageAccess(String filename) {
        for (HistoryEntry entry : downloadHistory) {
            if (entry.filename.equals(filename)) {
                entry.viewCount++;
                return;
            }
        }
        downloadHistory.add(new HistoryEntry(filename));
    }

    public List<HistoryEntry> getDownloadHistory() {
        return downloadHistory;
    }

    public void clearAll() {
        imageCache.clear();
        downloadHistory.clear();
    }

    // ------- CLASES INTERNAS --------
    public static class ImageData {
        public String title;
        public int width;
        public int height;
        public String src;
        public Bitmap thumbnail;
        public Bitmap fullImage;
    }

    public static class HistoryEntry {
        public String filename;
        public int viewCount;
        public Date downloadTime;

        public HistoryEntry(String filename) {
            this.filename = filename;
            this.viewCount = 1;
            this.downloadTime = new Date();
        }
    }
}

