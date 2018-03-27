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
import cn.com.larunda.monitor.bean.MapCompanyBean;

/**
 * Created by sddt on 18-3-27.
 */

public class MapCompanyAdapter extends RecyclerView.Adapter<MapCompanyAdapter.ViewHolder> {
    private Context context;
    private List<MapCompanyBean> mapCompanyBeanList = new ArrayList<>();

    public MapCompanyAdapter(Context context, List<MapCompanyBean> mapCompanyBeanList) {
        this.context = context;
        this.mapCompanyBeanList = mapCompanyBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView originalText;
        TextView original;
        TextView data;

        public ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.map_item_type);
            originalText = itemView.findViewById(R.id.map_item_original_text);
            original = itemView.findViewById(R.id.map_item_original);
            data = itemView.findViewById(R.id.map_item_data);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapCompanyBean companyBean = mapCompanyBeanList.get(position);
        holder.type.setText(companyBean.getType() + "");
        holder.data.setText(companyBean.getData() + "");
        if (companyBean.getOriginal() == null) {
            holder.originalText.setVisibility(View.INVISIBLE);
            holder.original.setVisibility(View.INVISIBLE);
        } else {
            holder.originalText.setVisibility(View.VISIBLE);
            holder.original.setVisibility(View.VISIBLE);
            holder.original.setText(companyBean.getOriginal());
        }

    }

    @Override
    public int getItemCount() {
        return mapCompanyBeanList.size();
    }
}
