package com.example.spaceimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewRef;
    private final DataSingleton.ImageData data;

    public FullImageDownloadTask(ImageView imageView, DataSingleton.ImageData data) {
        this.imageViewRef = new WeakReference<>(imageView);
        this.data = data;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            String filename = extractFilename(data.src);
            URL url = new URL("https://cdn.esahubble.org/archives/images/large/" + filename);
            Log.d("FullImageDownload", "Downloading from: " + url);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            inputStream = connection.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);


            int reqWidth = 2048;  // Max width for downsampled image
            int reqHeight = 2048; // Max height for downsampled image
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            connection.disconnect();
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, null, options);

        } catch (Exception e) {
            Log.e("FullImageDownload", "Error: " + e.getMessage());
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException ignored) {}
        }
    }

    //Save Full Image
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewRef.get();
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
            data.fullImage = bitmap;
        }
    }

    private String extractFilename(String src) {
        return src.substring(src.lastIndexOf("/") + 1);
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
