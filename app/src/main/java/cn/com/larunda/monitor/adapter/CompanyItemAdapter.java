package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.MaintenanceCompany;

/**
 * Created by sddt on 18-4-9.
 */

public class CompanyItemAdapter extends RecyclerView.Adapter<CompanyItemAdapter.ViewHolder> {
    private List<MaintenanceCompany> maintenanceCompanyList = new ArrayList<>();
    private Context context;
    private HashMap<String, Integer> iconList = new HashMap<>();
    private ItemOnClickListener itemOnClickListener;

    public CompanyItemAdapter(List<MaintenanceCompany> maintenanceCompanyList, Context context, HashMap<String, Integer> iconList) {
        this.maintenanceCompanyList = maintenanceCompanyList;
        this.context = context;
        this.iconList = iconList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;
        LinearLayout telLayout;
        LinearLayout typeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_company_list_item_name);
            recyclerView = itemView.findViewById(R.id.item_company_list_item_recycler);
            telLayout = itemView.findViewById(R.id.item_company_list_item_tel_layout);
            typeLayout = itemView.findViewById(R.id.item_company_list_item_type);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MaintenanceCompany maintenanceCompany = maintenanceCompanyList.get(position);
        holder.textView.setText(maintenanceCompany.getName() + "");
        if (maintenanceCompany.getName().equals("无")) {
            holder.typeLayout.setVisibility(View.GONE);
        } else {
            holder.typeLayout.setVisibility(View.VISIBLE);
        }
        if (maintenanceCompany.getTypeList() != null) {
            LinearLayoutManager manager = new LinearLayoutManager(this.context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            CompanyImageAdapter adapter = new CompanyImageAdapter(maintenanceCompany.getTypeList(), this.context, iconList);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.setAdapter(adapter);
        }
        holder.telLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    itemOnClickListener.telOnClick(maintenanceCompany.getTel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return maintenanceCompanyList.size();
    }

    public interface ItemOnClickListener {
        void telOnClick(String tel);
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
