package com.unuldur.uminc.calendarRecuperator;

import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEtudiant;

/**
 * Created by julie on 11/12/2017.
 */

public interface ICalendarRecuperator {
    ICalendar getCalendar(IEtudiant e);
}
