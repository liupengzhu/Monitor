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
import cn.com.larunda.monitor.bean.GasRankingBean;
import cn.com.larunda.monitor.bean.SteamRankingBean;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-19.
 */

public class SteamRankingRecyclerAdapter extends RecyclerView.Adapter<SteamRankingRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<SteamRankingBean> steamRankingBeanList = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView name;
        TextView data;
        TextView percent;
        TextView nameText;
        TextView dataText;
        TextView percentText;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.steam_ranking_item_img);
            textView = itemView.findViewById(R.id.steam_ranking_item_text);
            name = itemView.findViewById(R.id.steam_ranking_item_name);
            data = itemView.findViewById(R.id.steam_ranking_item_data);
            percent = itemView.findViewById(R.id.steam_ranking_item_percent);
            nameText = itemView.findViewById(R.id.steam_ranking_item_name_text);
            dataText = itemView.findViewById(R.id.steam_ranking_item_data_text);
            percentText = itemView.findViewById(R.id.steam_ranking_item_percent_text);
        }
    }

    public SteamRankingRecyclerAdapter(Context context, List<SteamRankingBean> steamRankingBeanList) {
        this.context = context;
        this.steamRankingBeanList = steamRankingBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_steam_ranking_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SteamRankingBean bean = steamRankingBeanList.get(position);
        if (position == 0) {
            holder.imageView.setImageResource(R.drawable.top1);
        } else if (position == 1) {
            holder.imageView.setImageResource(R.drawable.top2);
        } else if (position == 2) {
            holder.imageView.setImageResource(R.drawable.top3);
        } else {
            holder.imageView.setImageResource(R.drawable.top4);
        }
        if (bean.getStyle().equals("industry")) {
            holder.name.setText("行业名称:");
            holder.data.setText("行业能耗:");
            holder.percent.setText("行业占比:");
        } else {
            holder.name.setText("企业名称:");
            holder.data.setText("企业能耗:");
            holder.percent.setText("企业占比:");
        }
        holder.textView.setText("耗汽排行" + (position + 1));
        holder.nameText.setText(bean.getName());
        holder.dataText.setText(Util.formatNum(Float.valueOf(bean.getData())) + bean.getRatio());
        holder.percentText.setText(Util.formatNum(Float.valueOf(bean.getPercent())) + "%");
    }

    @Override
    public int getItemCount() {
        return steamRankingBeanList.size();
    }
}
