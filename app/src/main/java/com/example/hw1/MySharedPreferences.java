package com.example.hw1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.hw1.GamePlayFinals;
import com.example.hw1.GameUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {

    /**
     * Define the max size of the high score table
     */
    private final int MAX_HIGH_SCORE_LENGTH = 10;

    private SharedPreferences shared;
    private ArrayList<GameUser> list;

    public MySharedPreferences(Context context) {
        shared = context.getSharedPreferences("App", MODE_PRIVATE);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(GameUser gameUser, Comparator<GameUser> comparator) {
        list = readDataFromStorage();
        if (!list.contains(gameUser)) {
            if (list.size() < MAX_HIGH_SCORE_LENGTH) {
                list.add(gameUser);
                list.sort(comparator);
                writeDataToStorage(list);
            } else if (gameUser.getScore() > list.get(list.size() - 1).getScore()) {
                list.set(list.size() - 1, gameUser);
                list.sort(comparator);
                writeDataToStorage(list);
            }
        } else {
            int idx = list.indexOf(gameUser);
            GameUser currentUser = list.get(idx);
            if (currentUser.getScore() < gameUser.getScore()) {
                list.set(idx, gameUser);
                list.sort(comparator);
                writeDataToStorage(list);
            }
        }
    }


    private void writeDataToStorage(ArrayList<GameUser> list) {
        SharedPreferences.Editor editor = shared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("task_list", json);
        editor.apply();
    }

    public ArrayList<GameUser> readDataFromStorage() {
        Gson gson = new Gson();
        String json = shared.getString("task_list", null);
        Type type = new TypeToken<ArrayList<GameUser>>() {
        }.getType();
        if ((list = gson.fromJson(json, type)) == null)
            list = new ArrayList<>();
        return list;
    }

    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.apply();
    }
}
