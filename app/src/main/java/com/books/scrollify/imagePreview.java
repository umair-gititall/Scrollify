package com.books.scrollify;

import android.widget.ImageView;

public class imagePreview {
    ImageView preview;
    int[] images;
    int imageLoad = 0;
    public imagePreview(int[] images, ImageView preview){
        this.images = images;
        this.preview = preview;
    }
    public void load()
    {
        preview.post(new Runnable() {
            @Override
            public void run() {
                preview.setImageResource(images[(imageLoad++) % images.length]);
                preview.postDelayed(this, 3000);
            }
        });
    }
}
