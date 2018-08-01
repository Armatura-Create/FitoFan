package com.example.alex.fitofan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.util.HashMap;

public class VideoLoadAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private String videoPath;
    public static ImageView imageView;
    private Bitmap bitmap = null;
    private MediaMetadataRetriever mediaMetadataRetriever = null;

    public VideoLoadAsyncTask(String videoPath, ImageView imageView) {
        this.videoPath = videoPath;
        VideoLoadAsyncTask.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        imageView.setImageBitmap(result);
    }
}
