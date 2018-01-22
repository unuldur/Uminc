package com.unuldur.uminc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Unuldur on 01/12/2017.
 */

public interface IEvent extends Serializable {
    int getYear();
    int getWeek();
    Calendar getStartDate();
    Calendar getEndDate();
    String getTitre();
    String getLocalisation();
    UE getUE();
    void setUE(UE e);

}
