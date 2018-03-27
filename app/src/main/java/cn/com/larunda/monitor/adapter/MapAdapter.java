package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;

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
    private MapOnClickListener mapOnClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView name;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.map_item_text1);
            name = itemView.findViewById(R.id.map_item_text2);
            layout = itemView.findViewById(R.id.map_item_layout);
        }
    }

    public MapAdapter(Context context, List<PointBean> pointBeanList) {
        this.context = context;
        this.pointBeanList = pointBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapOnClickListener!=null){
                    int position = viewHolder.getAdapterPosition();
                    mapOnClickListener.onClick(v,position,pointBeanList.get(position).getLatLng());
                }
            }
        });
        return viewHolder;
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

    public interface MapOnClickListener {
        void onClick(View view, int position, LatLng latLng);
    }

    public void setMapOnClickListener(MapOnClickListener mapOnClickListener) {
        this.mapOnClickListener = mapOnClickListener;
    }
}
