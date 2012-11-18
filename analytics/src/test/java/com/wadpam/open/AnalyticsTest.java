package com.wadpam.open;


import com.wadpam.open.analytics.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests.
 */
public class AnalyticsTest {

    static final Logger LOG = LoggerFactory.getLogger(AnalyticsTest.class);

    private String trackingID;
    private DeviceData deviceData = new DeviceData();
    private VisitorData visitorData;

    private OpenGATracker tracker;

    public AnalyticsTest() {

        // Set config
        this.trackingID = "UA-35889513-2";

        // Set visitor
        this.visitorData = VisitorData.createVisitorWithNewSession(999, now() - 50000, now() - 4000, 10);

        // Set device data
        this.deviceData.setEncoding("UTF-8");
        this.deviceData.setFlashVersion("11");
        this.deviceData.setScreenResolution("800x600");
        this.deviceData.setUserLanguage("fi");
        this.deviceData.setColorDepth("24-bit");
        this.deviceData.setUserAgent("Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10");
    }


    @Before
    public void setup() {
        this.visitorData.startNewSession();
        this.tracker = new OpenGATracker(this.trackingID, this.visitorData, this.deviceData);
        //tracker.setDebug(true);
    }

    @After
    public void tearDown() {
        // Do nothing right now
    }


    @Test
    public void basicEvent() {
        LOG.info("Test basic event");

        tracker.trackEvent("category1", "action1");

        assertTrue(true);
    }

    @Test
    public void eventWithLabel() {
        LOG.info("Event with label and value");

        tracker.trackEvent("category1", "action1", "label", 2, null);

        assertTrue(true);
    }

    @Test
    public void eventWithCustomVar() {
        LOG.info("Event with custom variable");

        List<CustomVariable> customVars = new ArrayList<CustomVariable>();
        customVars.add(new CustomVariable(1, "name1", "value1"));
        customVars.add(new CustomVariable(2, "name2", "value2"));

        tracker.trackEvent("category1", "action1", "label", 2, customVars);

        assertTrue(true);
    }


    @Test
    public void basicPageView() {
        LOG.info("Test basic page view");

        tracker.trackPageView("/page/url", "Title", "legend-passbook.appspot.com");

        assertTrue(true);
    }

    @Test
    public void pageViewWithReferrer() {
        LOG.info("Page view with referrer");

        tracker.trackPageView("/page/url", "Title", "www.testhost.com", "/referrer/page", "www.referrerhost.com", null);

        assertTrue(true);
    }

    @Test
    public void pageViewWithCustomVars() {
        LOG.info("Page view with referrer");

        List<CustomVariable> customVars = new ArrayList<CustomVariable>();
        customVars.add(new CustomVariable(1, "name1", "value1"));
        customVars.add(new CustomVariable(2, "name2", "value2"));

        tracker.trackPageView("/page/url", "Title", "www.testhostname.com", "/referrer/page", "www.referrerhose.com", customVars);

        assertTrue(true);
    }

    // Return now
    private static long now() {
        return System.currentTimeMillis() / 1000L;
    }
}