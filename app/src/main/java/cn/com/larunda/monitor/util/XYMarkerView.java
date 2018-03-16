package cn.com.larunda.monitor.util;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-3-16.
 */

public class XYMarkerView extends MarkerView {
    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;

    private BarOnClickListener barOnClickListener;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public XYMarkerView(Context context) {
        super(context, R.layout.item_mark);
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (barOnClickListener != null && tvContent != null) {
            barOnClickListener.onClick(e, highlight, tvContent);
        }
        super.refreshContent(e, highlight);
    }

    public void setBarOnClickListener(BarOnClickListener barOnClickListener) {
        this.barOnClickListener = barOnClickListener;
    }
}
