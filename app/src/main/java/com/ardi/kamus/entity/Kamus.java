package com.ardi.kamus.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Kamus implements Parcelable {

    private int id;
    private String word;
    private String description;

    public Kamus() {

    }

    public Kamus(String word, String description) {
        this.word = word;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.word);
        parcel.writeString(this.description);
    }

    private Kamus(Parcel in) {
        this.id = in.readInt();
        this.word = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Kamus> CREATOR = new Creator<Kamus>() {
        @Override
        public Kamus createFromParcel(Parcel parcel) {
            return new Kamus(parcel);
        }

        @Override
        public Kamus[] newArray(int i) {
            return new Kamus[i];
        }
    };

}
