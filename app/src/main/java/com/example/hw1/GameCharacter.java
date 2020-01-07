package com.example.hw1;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

public class GameCharacter extends AppCompatImageView {

    public GameCharacter(Context context) {
        super((context));
    }

    public GameCharacter(Context context, float x_location, float y_location, int width, int height) {
        super(context);
        setX(x_location);
        setY(y_location);
        setLayoutParams(new android.view.ViewGroup.LayoutParams(width, height));
    }

}
