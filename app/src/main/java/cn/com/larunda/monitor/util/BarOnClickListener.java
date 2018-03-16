package cn.com.larunda.monitor.util;

import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by sddt on 18-3-16.
 */

public interface BarOnClickListener {
    void onClick(Entry e, Highlight highlight, View v);
}
