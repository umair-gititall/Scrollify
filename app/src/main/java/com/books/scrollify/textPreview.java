package com.books.scrollify;

import android.widget.TextView;

public class textPreview{
    TextView textbox;
    String[] textData;
    int textLoad = 0, loadedString = 0;
    StringBuilder builder = new StringBuilder();
    Boolean controller = false;

    public textPreview(String[] text, TextView preview)
    {
        textData = text;
        textbox = preview;
    }
    public void run(){
        textbox.post(new Runnable() {
            @Override
            public void run() {
                if (controller) clear();
                else write();
                textbox.postDelayed(this, 70);
            }
        });
    }
    public void write() {
        builder.append(textData[loadedString].charAt(textLoad++));
        textbox.setText(builder + "|");
        if (textLoad == textData[loadedString].length()) {
            controller = true;
        }
    }

    public void clear() {
        if (textLoad == 1) {
            controller = false;
            loadedString = (++loadedString) % textData.length;
        }
        builder.setLength(builder.length() - 1);
        textbox.setText(builder + "|");
        textLoad--;
    }
}
