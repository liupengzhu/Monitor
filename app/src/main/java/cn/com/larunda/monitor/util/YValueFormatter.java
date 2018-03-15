package cn.com.larunda.monitor.util;

import com.github.mikephil.charting.components.AxisBase;

/**
 * Created by sddt on 18-3-15.
 */

public interface YValueFormatter {
    public String getFormattedValue(float value, AxisBase axis);
}
