package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.RenewableRankingBean;

/**
 * Created by sddt on 18-3-20.
 */

public class RenewableRankingRecyclerAdapter extends RecyclerView.Adapter<RenewableRankingRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<RenewableRankingBean> renewableRankingBeanList = new ArrayList<>();

    public RenewableRankingRecyclerAdapter(Context context, List<RenewableRankingBean> renewableRankingBeanList) {
        this.context = context;
        this.renewableRankingBeanList = renewableRankingBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView name;
        TextView installedCapacity;
        TextView installedCapacityUnit;
        TextView total;
        TextView history_average;
        TextView range;
        TextView totalUnit;
        TextView history_averageUnit;

        ImageView rangeImg;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.renewable_ranking_item_img);
            textView = itemView.findViewById(R.id.renewable_ranking_item_text);
            name = itemView.findViewById(R.id.renewable_ranking_item_name_text);
            installedCapacity = itemView.findViewById(R.id.renewable_ranking_item_installed_capacity);
            installedCapacityUnit = itemView.findViewById(R.id.renewable_ranking_item_installed_capacity_unit);
            total = itemView.findViewById(R.id.renewable_ranking_item_total);
            history_average = itemView.findViewById(R.id.renewable_ranking_item_history_average);
            range = itemView.findViewById(R.id.renewable_ranking_item_range);
            totalUnit = itemView.findViewById(R.id.renewable_ranking_item_total_unit);
            history_averageUnit = itemView.findViewById(R.id.renewable_ranking_item_history_average_unit);

            rangeImg = itemView.findViewById(R.id.renewable_ranking_item_range_img);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_renewable_ranking_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RenewableRankingBean renewableRankingBean = renewableRankingBeanList.get(position);
        if (position == 0) {
            holder.imageView.setImageResource(R.drawable.top1);
        } else if (position == 1) {
            holder.imageView.setImageResource(R.drawable.top2);
        } else if (position == 2) {
            holder.imageView.setImageResource(R.drawable.top3);
        } else {
            holder.imageView.setImageResource(R.drawable.top4);
        }
        holder.textView.setText("发电排行" + renewableRankingBean.getRank());
        holder.name.setText(renewableRankingBean.getName() + "");

        holder.installedCapacity.setText(renewableRankingBean.getInstalledCapacity() + "");
        holder.installedCapacityUnit.setText("装机容量(" + renewableRankingBean.getInstalledCapacityRatio() + ")");
        holder.total.setText(renewableRankingBean.getTotal() + "");
        holder.history_average.setText(renewableRankingBean.getHistory_average() + "");
        holder.totalUnit.setText("实际发电量(" + renewableRankingBean.getRatio() + ")");
        holder.history_averageUnit.setText("历史同期发电量(" + renewableRankingBean.getRatio() + ")");
        if (renewableRankingBean.getRange().equals("/")) {
            holder.range.setText("-");
            holder.rangeImg.setImageResource(R.drawable.none);
        } else {
            holder.range.setText(renewableRankingBean.getRange() + "");
            float range = Float.valueOf(renewableRankingBean.getRange());
            if (range > 0) {
                holder.rangeImg.setImageResource(R.drawable.rise);
            } else if (range < 0) {
                holder.rangeImg.setImageResource(R.drawable.decline);
            } else {
                holder.rangeImg.setImageResource(R.drawable.none);
            }
        }

    }

    @Override
    public int getItemCount() {
        return renewableRankingBeanList.size();
    }
}
