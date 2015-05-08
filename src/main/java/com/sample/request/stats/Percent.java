package com.sample.request.stats;
/**
 * Constant for Percentile value
 * 
 */
public enum Percent {
    TEN(10), TWENTY_FIVE(25), FIFTY(50), SEVENTY_FIVE(75), NINETY(90),
    NINETY_FIVE(95), NINETY_EIGHT(98), NINETY_NINE(99), NINETY_NINE_POINT_FIVE(99.5),HUNDRED(100);

    private double val;

    Percent(double val) {
        this.val = val;
    }

    public double getValue() {
        return val;
    }


}
