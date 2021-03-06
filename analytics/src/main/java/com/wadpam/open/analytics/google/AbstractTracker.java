package com.wadpam.open.analytics.google;

import com.wadpam.open.analytics.Tracker;

import java.util.List;

/**
 * Abstract base class for analytics trackers, e.g Google Analytics and Piwik.
 * @author mattiaslevin
 */
public abstract class AbstractTracker implements Tracker {

    /** Track a page view without setting referrer */
    public void trackPageView(String pageURL, String pageTitle, String hostName) {
        this.trackPageView(pageURL, pageTitle, hostName, null, null, null);
    }

    /**
     * Track a page view referrer and custom variables.
     * @param pageURL the page url, e.g. /page/. Must be provided
     * @param pageTitle the title of the page
     * @param hostName the host name
     * @param referrerPage optional. The referrer page
     * @param referrerSite optional. The referrer site
     * @param customVariables optional. List of custom variables
     */
    @Override
    public abstract void trackPageView(String pageURL,
                                       String pageTitle,
                                       String hostName,
                                       String referrerPage,
                                       String referrerSite,
                                       List<CustomVariable> customVariables);


    /** Track without label and value */
    public void trackEvent(String category, String action) {
        this.trackEvent(category, action, null, null, null);
    }

    /** Track without custom variable */
    public void trackEvent(String category, String action, String label, int value) {
        this.trackEvent(category, action, label, value, null);
    }

    /**
     * Track an event.
     * @param category the category
     * @param action the action
     * @param label the label
     * @param value optional. The value
     * @param customVariables optional. custom variables
     */
    @Override
    public abstract void trackEvent(String category,
                                    String action,
                                    String label,
                                    Integer value,
                                    List<CustomVariable> customVariables);


}
