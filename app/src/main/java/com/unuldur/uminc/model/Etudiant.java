package com.unuldur.uminc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unuldur on 06/12/2017.
 */

public class Etudiant implements IEtudiant{
    private String numEtu;
    private String password;
    private List<UE> ues;
    private List<UE> actualsUes;

    public Etudiant(String numEtu, String password, List<UE> ues) {
        this.numEtu = numEtu;
        this.password = password;
        this.ues = ues;
        this.actualsUes = new ArrayList<>();
    }

    public Etudiant() {
    }

    @Override
    public String getNUmEtu() {
        return numEtu;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<UE> getUEs() {
        return ues;
    }

    @Override
    public List<UE> getActualUEs() {
        return actualsUes;
    }
}
