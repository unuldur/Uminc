package com.unuldur.uminc.model;

import java.io.Serializable;

/**
 * Created by julie on 07/12/2017.
 */

public class UE implements Serializable {
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
}