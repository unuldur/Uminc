package com.unuldur.uminc;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.unuldur.uminc.calendarRecuperator.BiWeeklyCalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.ICalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.Ical4jCalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.MyCalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.TaskCalendarDownloader;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by julie on 12/12/2017.
 */
@RunWith(AndroidJUnit4.class)
public class Ical4jCalendarDownloaderTest {
    @Test
    public void test(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Ical4jCalendarDownloader scd = new Ical4jCalendarDownloader(new Connection(appContext), appContext);
        List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        assertFalse(events.isEmpty());
    }

    @Test
    public void test2(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Ical4jCalendarDownloader scd = new Ical4jCalendarDownloader(new Connection(appContext), appContext);
        List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        assertFalse(events.isEmpty());
    }

    @Test
    public void test5(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BiWeeklyCalendarDownloader scd = new BiWeeklyCalendarDownloader(new Connection(appContext), appContext);
        List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        assertFalse(events.isEmpty());
    }

    @Test
    public void test6(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ICalendarDownloader scd = new MyCalendarDownloader(new Connection(appContext), appContext);
        List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        assertFalse(events.isEmpty());
    }

    @Test
    public void test3(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Ical4jCalendarDownloader scd = new Ical4jCalendarDownloader(new Connection(appContext), appContext);
        TaskCalendarDownloader t1 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t2 = new TaskCalendarDownloader(scd);
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        t2.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        //List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        assertFalse(false);
    }

    @Test
    public void test4(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ICalendarDownloader scd = new BiWeeklyCalendarDownloader(new Connection(appContext), appContext);
        TaskCalendarDownloader t1 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t2 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t3 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t4 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t5 = new TaskCalendarDownloader(scd);
        TaskCalendarDownloader t6 = new TaskCalendarDownloader(scd);
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        t1.execute("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        //List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/IMA/M1_IMA/");
        assertFalse(false);
    }
}
