package com.books.scrollify;

import android.widget.TextView;

public class textPreview{
    TextView textbox;
    String[] textData;
    int textLoad = 0, loadedString = 0;
    StringBuilder builder = new StringBuilder();
    Boolean controller = false;
    boolean blinking = false;
    boolean cursorVisible = true;
    int blinkCount = 0;

    public textPreview(String[] text, TextView preview)
    {
        textData = text;
        textbox = preview;
    }
    public void run(){
        textbox.post(new Runnable() {
            @Override
            public void run() {
                if(blinking)
                    blinkCursor();
                else if (controller) clear();
                else write();
                textbox.postDelayed(this, 70);
            }
        });
    }
    public void write() {
        builder.append(textData[loadedString].charAt(textLoad++));
        textbox.setText(builder + "|");
        if (textLoad == textData[loadedString].length()) {
            blinking = true;
            blinkCount = 0;
        }
    }

    public void blinkCursor() {
        if(blinkCount % 3 == 0)
            cursorVisible = !cursorVisible;
        textbox.setText(builder + (cursorVisible ? "|" : ""));

        blinkCount++;
        if (blinkCount >= 14) {
            blinking = false;
            controller = true;
            cursorVisible = true;
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
