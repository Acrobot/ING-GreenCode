package com.pomirski.atm;

import com.dslplatform.json.CompiledJson;

@CompiledJson
public class Order implements Comparable<Order> {
    /**
     * Request type, sorting according to priority (highest is most valuable).
     * <p>
     * Restart is the most important (should be done in first place), then
     * we handle low cash signal (since it has to be done before priority tasks) and finally
     * we have priority and standard tasks.
     */
    public enum RequestType {
        FAILURE_RESTART,
        SIGNAL_LOW,
        PRIORITY,
        STANDARD,
    }

    public int region;
    public RequestType requestType;
    public int atmId;

    @Override
    public int compareTo(Order o) {
        int regionComparison = Integer.compare(this.region, o.region);
        if (regionComparison != 0) {
            return regionComparison;
        }

        int requestComparison = this.requestType.compareTo(o.requestType);
        if (requestComparison != 0) {
            return requestComparison;
        }

        return Integer.compare(this.atmId, o.atmId);
    }
}
