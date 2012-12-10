package com.wadpam.open.analytics;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.analytics.google.*;
import com.wadpam.open.web.AbstractRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling dispatch tasks and also forwarding events to GA
 * on behalf on an app.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/analytics")
public class AnalyticsController extends AbstractRestController {
    static final Logger LOG = LoggerFactory.getLogger(AnalyticsController.class);


    // The default tracker that will be used if no domain-tracker mapping can be found
    private String defaultTrackerId;

    // Map between domain and a tracker id
    private Map<String, String> domainTrackerIdMapping = new HashMap<String, String>();

    /**
     * A GAE task for sending and event to Google Analytics.
     */
    @RequestMapping(value="task", method= RequestMethod.GET)
    @ResponseBody
    public void sendEventTask(HttpServletRequest request,
                              @RequestParam(required = true) String url,
                              @RequestParam(required = false) String userAgent) {
        LOG.debug("GAE sent event task");

        // TODO incomplete implementation

        // Decode

        // Forward
        EventDispatcher dispatcher = new SynchronousEventDispatcher("host", "remote", userAgent);
        try {
            dispatcher.dispatch(new URI(url));
        } catch (URISyntaxException e) {
            LOG.error("Not possible to convert url to uri with reason:{}", e.getMessage());
            // No need to throw exception, no one is listening to the result
        }
    }


    /**
     * Forward a page view on the behalf of an app.
     * @param userId a unique user id. Must be consistent through requests
     * @param pageUrl the url or name of the page
     * @param title the title of the page
     */
    @RequestMapping(value="pageview", method= RequestMethod.GET)
    @ResponseBody
    @RestReturn(value=Map.class, entity=Map.class, code={
            @RestCode(code=200, message="OK", description="Page view forwarded"),
    })
    public void forwardPageView(HttpServletRequest request,
                                @PathVariable String domain,
                                @RequestParam int userId,
                                @RequestParam String pageUrl,
                                @RequestParam String title) {
        LOG.debug("Forward page view");

        String trackerId = this.domainTrackerIdMapping.get(domain);
        String trackerName = domain;
        if (null != trackerId) {
            trackerId = this.defaultTrackerId;
            trackerName = "default";
        }

        Profile profile = new Profile(trackerName, trackerId);

        Visitor visitor = Visitor.visitorWithNewSession(userId, now(), now(), 1);
        Device device = Device.defaultDevice(request);
        OpenAnalyticsTracker tracker = profile.getTracker(visitor, device);

        tracker.trackPageView(pageUrl, title, request.getRemoteHost());
    }

    private static long now() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * Forward an event on the behalf of an app.
     * @param userId a unique user id. Must be consistent through requests
     * @param category the event category
     * @param action the action name
     * @param label a label
     * @param value a value
     */
    @RequestMapping(value="event", method= RequestMethod.GET)
    @ResponseBody
    @RestReturn(value=Map.class, entity=Map.class, code={
            @RestCode(code=200, message="OK", description="Event forwarded"),
    })
    public void forwardEvent(HttpServletRequest request,
                             @PathVariable String domain,
                             @RequestParam int userId,
                             @RequestParam String category,
                             @RequestParam String action,
                             @RequestParam(required = false) String label,
                             @RequestParam(required = false) int value) {
        LOG.debug("Forward event");

        String trackerId = this.domainTrackerIdMapping.get(domain);
        String trackerName = domain;
        if (null != trackerId) {
            trackerId = this.defaultTrackerId;
            trackerName = "default";
        }

        Profile profile = new Profile(trackerName, trackerId);

        Visitor visitor = Visitor.visitorWithNewSession(userId, now(), now(), 1);
        Device device = Device.defaultDevice(request);
        OpenAnalyticsTracker tracker = profile.getTracker(visitor, device);

        tracker.trackEvent(category, action, label, value, null);
    }


    // Setter and getters
    public void setDefaultTrackerId(String defaultTrackerId) {
        this.defaultTrackerId = defaultTrackerId;
    }

    public void setDomainTrackerIdMapping(Map<String, String> domainTrackerIdMapping) {
        this.domainTrackerIdMapping = domainTrackerIdMapping;
    }
}