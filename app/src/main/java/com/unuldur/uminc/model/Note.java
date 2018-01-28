package com.unuldur.uminc.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Unuldur on 24/01/2018.
 */

public class Note implements Comparable, Serializable{
    private String note;
    private String name;
    private UE UE;

    public Note(String note, String name, UE UE) {
        this.note = note;
        this.name = name;
        this.UE = UE;
    }

    @Override
    public String toString() {
        return UE.getId()+ " - " + name + " : " + note;
    }

    public com.unuldur.uminc.model.UE getUE() {
        return UE;
    }

    public String getNote() {
        return note;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int res = ((Note)o).UE.compareTo(UE);
        if (res != 0) return res;
        return name.compareTo(((Note)o).name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note1 = (Note) o;

        if (note != null ? !note.equals(note1.note) : note1.note != null) return false;
        if (name != null ? !name.equals(note1.name) : note1.name != null) return false;
        return UE != null ? UE.equals(note1.UE) : note1.UE == null;
    }

    @Override
    public int hashCode() {
        int result = note != null ? note.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (UE != null ? UE.hashCode() : 0);
        return result;
    }
}
