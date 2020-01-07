package com.example.hw1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameUser implements Comparable<GameUser>, Parcelable {

    private String userName;
    private int score;
    private double longitude;
    private double latitude;
    private int playerCharacter;

    public GameUser(String userName, double longitude, double latitude, int score, int playerCharacter) {
        this.userName = userName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.score = score;
        this.playerCharacter = playerCharacter;
    }

    protected GameUser(Parcel in) {
        userName = in.readString();
        score = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
        playerCharacter = in.readInt();
    }

    public static final Creator<GameUser> CREATOR = new Creator<GameUser>() {
        @Override
        public GameUser createFromParcel(Parcel in) {
            return new GameUser(in);
        }

        @Override
        public GameUser[] newArray(int size) {
            return new GameUser[size];
        }
    };

    public int getScore() {
        return score;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getUserName() {
        return userName;
    }

    public int getPlayerCharacter() {
        return playerCharacter;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @NonNull
    @Override
    public String toString() {
        return "User Name: " + userName + " Score: " + score + "Location: " + getLongitude() + ", " + getLatitude();
    }

    @Override
    public int compareTo(GameUser o) {
        return userName.compareTo(o.userName);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return getUserName().equals(((GameUser) obj).userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(score);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(playerCharacter);
    }
}
