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
import cn.com.larunda.monitor.bean.GasBean;
import cn.com.larunda.monitor.util.Util;


/**
 * Created by sddt on 18-3-15.
 */

public class GasRecyclerAdapter extends RecyclerView.Adapter<GasRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<GasBean> gasBeanList = new ArrayList<>();

    public GasRecyclerAdapter(Context context, List<GasBean> gasBeanList) {
        this.context = context;
        this.gasBeanList = gasBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView total;
        TextView history_average;
        TextView range;
        TextView totalUnit;
        TextView history_averageUnit;
        RelativeLayout history_averageLayout;
        RelativeLayout rangeLayout;
        ImageView rangeImg;


        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.gas_item_date);
            total = itemView.findViewById(R.id.gas_item_total);
            history_average = itemView.findViewById(R.id.gas_item_history_average);
            range = itemView.findViewById(R.id.gas_item_range);
            totalUnit = itemView.findViewById(R.id.gas_item_total_unit);
            history_averageUnit = itemView.findViewById(R.id.gas_item_history_average_unit);
            history_averageLayout = itemView.findViewById(R.id.gas_item_history_average_layout);
            rangeLayout = itemView.findViewById(R.id.gas_item_range_layout);
            rangeImg = itemView.findViewById(R.id.gas_item_range_img);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gas_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GasBean bean = gasBeanList.get(position);

        holder.totalUnit.setText("区间用气量 (" + bean.getRatio() + ")");
        holder.history_averageUnit.setText("同期历史值 (" + bean.getRatio() + ")");
        holder.time.setText(bean.getTime() + "");
        if (bean.getTotal() != null) {
            if (bean.getTotal().equals("/")) {
                holder.total.setText("-");
            } else {
                holder.total.setText(Util.formatNum(Float.valueOf(bean.getTotal())) + "");
            }
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
        return gasBeanList.size();
    }
}
