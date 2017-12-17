package com.unuldur.uminc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by Unuldur on 06/12/2017.
 */

public interface IEtudiant extends Serializable {
    String getNUmEtu();
    String getPassword();
    List<UE> getUEs();
    List<UE> getActualUEs();
    void addCalendar(ICalendar calendar);
    ICalendar getCalendar();
}
