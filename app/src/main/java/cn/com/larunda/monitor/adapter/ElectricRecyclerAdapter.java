package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.ElectricBean;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-15.
 */

public class ElectricRecyclerAdapter extends RecyclerView.Adapter<ElectricRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<ElectricBean> electricBeanList = new ArrayList<>();

    public ElectricRecyclerAdapter(Context context, List<ElectricBean> electricBeanList) {
        this.context = context;
        this.electricBeanList = electricBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView peak;
        TextView rush;
        TextView normal;
        TextView valley;
        TextView total;
        TextView history_average;
        TextView range;
        TextView peakUnit;
        TextView rushUnit;
        TextView normalUnit;
        TextView valleyUnit;
        TextView totalUnit;
        TextView history_averageUnit;

        RelativeLayout peakLayout;
        RelativeLayout rushLayout;
        RelativeLayout normalLayout;
        RelativeLayout valleyLayout;
        RelativeLayout history_averageLayout;
        RelativeLayout rangeLayout;

        ImageView rangeImg;


        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.electric_item_date);
            peak = itemView.findViewById(R.id.electric_item_peak);
            rush = itemView.findViewById(R.id.electric_item_rush);
            normal = itemView.findViewById(R.id.electric_item_normal);
            valley = itemView.findViewById(R.id.electric_item_valley);
            total = itemView.findViewById(R.id.electric_item_total);
            history_average = itemView.findViewById(R.id.electric_item_history_average);
            range = itemView.findViewById(R.id.electric_item_range);
            peakUnit = itemView.findViewById(R.id.electric_item_peak_unit);
            rushUnit = itemView.findViewById(R.id.electric_item_rush_unit);
            normalUnit = itemView.findViewById(R.id.electric_item_normal_unit);
            valleyUnit = itemView.findViewById(R.id.electric_item_valley_unit);
            totalUnit = itemView.findViewById(R.id.electric_item_total_unit);
            history_averageUnit = itemView.findViewById(R.id.electric_item_history_average_unit);

            peakLayout = itemView.findViewById(R.id.electric_item_peak_layout);
            rushLayout = itemView.findViewById(R.id.electric_item_rush_layout);
            normalLayout = itemView.findViewById(R.id.electric_item_normal_layout);
            valleyLayout = itemView.findViewById(R.id.electric_item_valley_layout);
            history_averageLayout = itemView.findViewById(R.id.electric_item_history_average_layout);
            rangeLayout = itemView.findViewById(R.id.electric_item_range_layout);

            rangeImg = itemView.findViewById(R.id.electric_item_range_img);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_electric_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ElectricBean bean = electricBeanList.get(position);
        holder.peakUnit.setText("峰 (" + bean.getRatio() + ")");
        holder.rushUnit.setText("尖 (" + bean.getRatio() + ")");
        holder.normalUnit.setText("平 (" + bean.getRatio() + ")");
        holder.valleyUnit.setText("谷 (" + bean.getRatio() + ")");
        holder.totalUnit.setText("区间用电量 (" + bean.getRatio() + ")");
        holder.history_averageUnit.setText("同期历史值 (" + bean.getRatio() + ")");
        holder.time.setText(bean.getTime() + "");
        if (bean.getTotal() != null) {
            if (holder.total.equals("/")) {
                holder.total.setText("-");
            } else {
                holder.total.setText(Util.formatNum(Float.valueOf(bean.getTotal())) );
            }
        }
        if (bean.getRush() != null) {
            holder.rushLayout.setVisibility(View.VISIBLE);
            if (bean.getRush().equals("/")) {
                holder.rush.setText("-");
            } else {
                holder.rush.setText(Util.formatNum(Float.valueOf(bean.getRush())));
            }
        } else {
            holder.rushLayout.setVisibility(View.GONE);
        }

        if (bean.getPeak() != null) {
            holder.peakLayout.setVisibility(View.VISIBLE);
            if (bean.getPeak().equals("/")) {
                holder.peak.setText("-");
            } else {
                holder.peak.setText(Util.formatNum(Float.valueOf(bean.getPeak())));
            }
        } else {
            holder.peakLayout.setVisibility(View.GONE);
        }

        if (bean.getNormal() != null) {
            holder.normalLayout.setVisibility(View.VISIBLE);
            if (bean.getNormal().equals("/")) {
                holder.normal.setText("-");
            } else {
                holder.normal.setText(Util.formatNum(Float.valueOf(bean.getNormal())));
            }
        } else {
            holder.normalLayout.setVisibility(View.GONE);
        }

        if (bean.getValley() != null) {
            holder.valleyLayout.setVisibility(View.VISIBLE);
            if (bean.getValley().equals("/")) {

            } else {
                holder.valley.setText(Util.formatNum(Float.valueOf(bean.getValley())));
            }
        } else {
            holder.valleyLayout.setVisibility(View.GONE);
        }
        if (bean.getHistory_average() != null) {
            holder.history_averageLayout.setVisibility(View.VISIBLE);
            if (bean.getHistory_average().equals("/")) {
                holder.history_average.setText("-");
            } else {
                holder.history_average.setText(Util.formatNum(Float.valueOf(bean.getHistory_average())));
            }
        } else {
            holder.history_averageLayout.setVisibility(View.GONE);
        }

        if (bean.getRange() != null) {
            holder.rangeLayout.setVisibility(View.VISIBLE);
            if (bean.getRange().equals("/")) {
                holder.range.setText("-");
                holder.rangeImg.setImageResource(R.drawable.none);
            } else {
                holder.range.setText(Util.formatNum(Float.valueOf(bean.getRange())));
                float range = Float.valueOf(bean.getRange());
                if (range > 0) {
                    holder.rangeImg.setImageResource(R.drawable.rise);
                } else if (range < 0) {
                    holder.rangeImg.setImageResource(R.drawable.decline);
                } else {
                    holder.rangeImg.setImageResource(R.drawable.none);
                }
            }
        } else {
            holder.rangeLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return electricBeanList.size();
    }
}
