package com.example.wmmc88.traffictracker;

public class KCFTrackerCountingSolution extends CountingSolution {

    protected KCFTrackerCountingSolution(Builder builder) {
        super(builder);
    }

    public static class Builder extends CountingSolution.Builder {

        public Builder() {
        }

        public KCFTrackerCountingSolution build() {
            return new KCFTrackerCountingSolution(this);
        }
    }
}
