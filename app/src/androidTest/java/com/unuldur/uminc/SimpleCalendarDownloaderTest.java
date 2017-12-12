package com.unuldur.uminc;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.unuldur.uminc.calendarRecuperator.SimpleCalendarDownloader;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.model.IEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by julie on 12/12/2017.
 */
@RunWith(AndroidJUnit4.class)
public class SimpleCalendarDownloaderTest {
    @Test
    public void test(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        SimpleCalendarDownloader scd = new SimpleCalendarDownloader(new Connection(appContext), appContext);
        List<IEvent> events = scd.getEventsFromAdress("https://cal.ufr-info-p6.jussieu.fr/caldav.php/ANDROIDE/M1_ANDROIDE");
        assertFalse(events.isEmpty());
    }
}
