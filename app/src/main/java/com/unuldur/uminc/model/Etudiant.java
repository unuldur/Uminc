package com.unuldur.uminc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Unuldur on 06/12/2017.
 */

public class Etudiant implements IEtudiant{
    private String numEtu;
    private String password;
    private Set<UE> ues;
    private List<UE> actualsUes;
    private Set<Note> notes;
    private ICalendar calendar = new SimpleCalendar();

    public Etudiant(String numEtu, String password, List<UE> ues) {
        this.numEtu = numEtu;
        this.password = password;
        this.ues = new TreeSet<>();
        this.ues.addAll(ues);
        this.actualsUes = new ArrayList<>();
        notes = new TreeSet<>();
    }

    public Etudiant(String numEtu, String password, List<UE> ues, List<Note> notes) {
        this.numEtu = numEtu;
        this.password = password;
        this.ues = new TreeSet<>();
        this.ues.addAll(ues);
        this.actualsUes = new ArrayList<>();
        this.notes = new TreeSet<>();
        this.notes.addAll(notes);
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
        List<UE> u = new ArrayList<>();
        u.addAll(ues);
        return u;
    }

    @Override
    public List<UE> getActualUEs() {
        return actualsUes;
    }

    @Override
    public List<Note> getNotes() {
        List<Note> u = new ArrayList<>();
        u.addAll(notes);
        return u;
    }

    @Override
    public void setNotes(List<Note> notes) {
        this.notes = new TreeSet<>();
        this.notes.addAll(notes);
    }

    @Override
    public void addCalendar(ICalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public ICalendar getCalendar() {
        return calendar;
    }
}
