package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.FilterCompanyBean;

/**
 * Created by sddt on 18-3-22.
 */

public class FilterCompanyAdapter extends RecyclerView.Adapter<FilterCompanyAdapter.ViewHolder> {
    private Context context;
    private List<FilterCompanyBean> filterCompanyBeanList = new ArrayList<>();
    private ItemOnClickListener itemOnClickListener;

    public FilterCompanyAdapter(Context context, List<FilterCompanyBean> filterCompanyBeanList) {
        this.context = context;
        this.filterCompanyBeanList = filterCompanyBeanList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.filter_company_item_name);
            type = itemView.findViewById(R.id.filter_company_item_type);
            layout = itemView.findViewById(R.id.filter_company_item_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter_company, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    itemOnClickListener.OnClick(v, filterCompanyBeanList.get(viewHolder.getAdapterPosition()).getId());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterCompanyBean companyBean = filterCompanyBeanList.get(position);
        holder.name.setText(companyBean.getName() + "");
        if ((companyBean.getTotal() != null && !companyBean.getTotal().equals("0"))
                || (companyBean.getUnderway() != null && !companyBean.getUnderway().equals("0"))) {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText(companyBean.getUnderway() + "/" + companyBean.getTotal());
        } else {
            holder.type.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return filterCompanyBeanList.size();
    }

    public interface ItemOnClickListener {
        void OnClick(View v, int id);
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
