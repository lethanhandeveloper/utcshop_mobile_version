package com.vku.lethanhan.utcshop.util;

import android.content.Context;
import android.widget.Toast;

public class Message {
    Context context;
    String message;

    public Message(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    public void showMeasage(){
        Toast.makeText(context, this.message, Toast.LENGTH_SHORT).show();
    }
}
