package com.unuldur.uminc.model;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * Created by julie on 07/12/2017.
 */

public class UE implements Serializable, Comparable {
    private String id;
    private String name;
    private String groupe;
    private int color;
    public UE(String id, String name, String groupe) {
        this.id = id;
        this.name = name;
        this.groupe = groupe;
        Random rnd = new Random();
        color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupe() {
        return groupe;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UE)) return false;
        UE ue = (UE) obj;
        if(!ue.getName().equals(name)) return false;
        if(!ue.getId().equals(id)) return false;
        if(!ue.getGroupe().equals(groupe)) return false;
        return true;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        UE e = (UE)o;
        return id.compareTo(e.getId());
    }
}
