package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.PointBean;

/**
 * Created by sddt on 18-3-26.
 */

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private Context context;
    private List<PointBean> pointBeanList = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.map_item_text1);
            name = itemView.findViewById(R.id.map_item_text2);
        }
    }

    public MapAdapter(Context context, List<PointBean> pointBeanList) {
        this.context = context;
        this.pointBeanList = pointBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PointBean bean = pointBeanList.get(position);
        holder.textView.setText(bean.getRank());
        holder.name.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return pointBeanList.size();
    }
}
