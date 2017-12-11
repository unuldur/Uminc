package com.unuldur.uminc;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.unuldur.uminc.calendarRecuperator.FullCalendarRecuperator;
import com.unuldur.uminc.calendarRecuperator.ICalendarRecuperator;


import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

/**
 *
 * Created by julie on 11/12/2017.
 */
@RunWith(AndroidJUnit4.class)
public class FullCalendarRecuperatorTest {
    @Test
    public void testGetFullAdress(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        FullCalendarRecuperator cr = new FullCalendarRecuperator(appContext);
        assertFalse(cr.getGlobalAdress().isEmpty());
    }

    @Test
    public void testGetAllAdress(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        FullCalendarRecuperator cr = new FullCalendarRecuperator(appContext);
        assertFalse(cr.getAllAdress(cr.getGlobalAdress()).isEmpty());
    }
}
