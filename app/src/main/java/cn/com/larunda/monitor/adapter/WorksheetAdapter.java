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
import cn.com.larunda.monitor.bean.WorksheetBean;

/**
 * Created by sddt on 18-3-22.
 */

public class WorksheetAdapter extends RecyclerView.Adapter<WorksheetAdapter.ViewHolder> {
    private Context context;
    private List<WorksheetBean> worksheetBeanList = new ArrayList<>();

    public WorksheetAdapter(Context context, List<WorksheetBean> worksheetBeanList) {
        this.context = context;
        this.worksheetBeanList = worksheetBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView type;
        TextView name;
        TextView time;
        TextView data;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.worksheet_item_title);
            type = itemView.findViewById(R.id.worksheet_item_type);
            name = itemView.findViewById(R.id.worksheet_item_name_text);
            time = itemView.findViewById(R.id.worksheet_item_time_text);
            data = itemView.findViewById(R.id.worksheet_item_data_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_worksheet, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WorksheetBean worksheetBean = worksheetBeanList.get(position);
        holder.title.setText(worksheetBean.getTitle() + "");
        holder.type.setText(worksheetBean.getType() + "");
        holder.name.setText(worksheetBean.getName() + "");
        holder.time.setText(worksheetBean.getTime() + "");
        holder.data.setText(worksheetBean.getData() + "");
        if (worksheetBean.getType() != null && worksheetBean.getType().equals("处理中")) {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_yellow_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_yellow));
        } else if (worksheetBean.getType() != null && worksheetBean.getType().equals("已完成")) {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_blue_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_blue));
        } else if (worksheetBean.getType() != null && worksheetBean.getType().equals("已取消")) {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_gray_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_gray));
        } else {
            holder.type.setTextColor(context.getResources().getColor(R.color.type_red_color1));
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.text_red));
        }
    }

    @Override
    public int getItemCount() {
        return worksheetBeanList.size();
    }
}
