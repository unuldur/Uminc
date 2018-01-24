package com.unuldur.uminc.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Unuldur on 24/01/2018.
 */

public class Note implements Comparable, Serializable{
    private String note;
    private String name;
    private String UE;

    public Note(String note, String name, String UE) {
        this.note = note;
        this.name = name;
        this.UE = UE;
    }

    @Override
    public String toString() {
        return UE + " - " + name + " : " + note;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int res = ((Note)o).UE.compareTo(UE);
        if (res != 0) return res;
        return name.compareTo(((Note)o).name);
    }
}
