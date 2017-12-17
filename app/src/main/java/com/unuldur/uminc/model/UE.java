package com.unuldur.uminc.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by julie on 07/12/2017.
 */

public class UE implements Serializable, Comparable {
    private String id;
    private String name;
    private String groupe;

    public UE(String id, String name, String groupe) {
        this.id = id;
        this.name = name;
        this.groupe = groupe;
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

    @Override
    public int compareTo(@NonNull Object o) {
        UE e = (UE)o;
        return id.compareTo(e.getId());
    }
}
