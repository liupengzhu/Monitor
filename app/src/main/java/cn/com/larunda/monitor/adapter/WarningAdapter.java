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
import cn.com.larunda.monitor.bean.WarningBean;

/**
 * Created by sddt on 18-3-22.
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder> {
    private Context context;
    private List<WarningBean> warningBeanList = new ArrayList<>();

    public WarningAdapter(Context context, List<WarningBean> warningBeanList) {
        this.context = context;
        this.warningBeanList = warningBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView type;
        TextView name;
        TextView time;
        TextView data;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.warning_item_title);
            type = itemView.findViewById(R.id.warning_item_type);
            name = itemView.findViewById(R.id.warning_item_name_text);
            time = itemView.findViewById(R.id.warning_item_time_text);
            data = itemView.findViewById(R.id.warning_item_data_text);
            status = itemView.findViewById(R.id.warning_item_status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_warning, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WarningBean warningBean = warningBeanList.get(position);
        holder.title.setText(warningBean.getTitle() + "");
        holder.type.setText(warningBean.getType() + "");
        holder.name.setText(warningBean.getName() + "");
        holder.time.setText(warningBean.getTime() + "");
        holder.data.setText(warningBean.getData() + "");
        holder.status.setText(warningBean.getStatus() + "");
        if (warningBean.getType() != null && warningBean.getType().equals("处理中")) {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_yellow_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_yellow));
        } else {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_red_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_red));
        }
        if (warningBean.getStatus() != null) {
            holder.status.setVisibility(View.VISIBLE);
            if (warningBean.getStatus().equals("已下发")) {
                holder.status.setBackground(context.getResources().getDrawable(R.drawable.type_blue));
            } else {
                holder.status.setBackground(context.getResources().getDrawable(R.drawable.type_gray));
            }
        } else {
            holder.status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return warningBeanList.size();
    }
}
