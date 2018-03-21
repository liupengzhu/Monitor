package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.MaintenanceCompany;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceSituationAdapter extends RecyclerView.Adapter<MaintenanceSituationAdapter.ViewHolder> {
    private Context context;
    private List<MaintenanceCompany> companyList = new ArrayList<>();
    private HashMap<String, Integer> iconList = new HashMap<>();
    private MaintenanceCompanyOnClickListener onClickListener;

    public void setOnClickListener(MaintenanceCompanyOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MaintenanceSituationAdapter(Context context, List<MaintenanceCompany> companyList, HashMap<String, Integer> iconList) {
        this.context = context;
        this.companyList = companyList;
        this.iconList = iconList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tel;
        TextView address;
        ImageView imageView;
        TextView user;
        TextView person;
        TextView car;
        TextView company;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.maintenance_situation_item_name);
            tel = itemView.findViewById(R.id.maintenance_situation_item_tel);
            address = itemView.findViewById(R.id.maintenance_situation_item_address);
            imageView = itemView.findViewById(R.id.maintenance_situation_item_company_img);
            user = itemView.findViewById(R.id.maintenance_situation_item_user);
            person = itemView.findViewById(R.id.maintenance_situation_item_person);
            car = itemView.findViewById(R.id.maintenance_situation_item_car);
            company = itemView.findViewById(R.id.maintenance_situation_item_company);
            img1 = itemView.findViewById(R.id.maintenance_situation_item_img1);
            img2 = itemView.findViewById(R.id.maintenance_situation_item_img2);
            img3 = itemView.findViewById(R.id.maintenance_situation_item_img3);
            img4 = itemView.findViewById(R.id.maintenance_situation_item_img4);
            img5 = itemView.findViewById(R.id.maintenance_situation_item_img5);
            layout = itemView.findViewById(R.id.maintenance_situation_item_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_maintenance_situation, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (onClickListener != null) {
                    onClickListener.onClick(v, companyList.get(position).getId());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MaintenanceCompany company = companyList.get(position);
        holder.name.setText(company.getName() + "");
        holder.tel.setText(company.getTel() + "");
        holder.address.setText(company.getAddress() + "");
        if (company.getImg() != null) {
            Glide.with(context).load(company.getImg())
                    .placeholder(R.drawable.company_null)
                    .error(R.drawable.company_null).into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.company_null));
        }

        holder.user.setText(company.getUser() + "");
        holder.person.setText(company.getPerson() + "");
        holder.car.setText(company.getCar() + "");
        holder.company.setText(company.getCompany() + "");
        if (company.getTypeList().size() >= 1) {
            holder.img1.setVisibility(View.VISIBLE);
            holder.img1.setImageDrawable(context.getResources().getDrawable(iconList.get(company.getTypeList().get(0))));
        } else {
            holder.img1.setVisibility(View.GONE);
        }
        if (company.getTypeList().size() >= 2) {
            holder.img2.setVisibility(View.VISIBLE);
            holder.img2.setImageDrawable(context.getResources().getDrawable(iconList.get(company.getTypeList().get(1))));
        } else {
            holder.img2.setVisibility(View.GONE);
        }
        if (company.getTypeList().size() >= 3) {
            holder.img3.setVisibility(View.VISIBLE);
            holder.img3.setImageDrawable(context.getResources().getDrawable(iconList.get(company.getTypeList().get(2))));
        } else {
            holder.img3.setVisibility(View.GONE);
        }
        if (company.getTypeList().size() >= 4) {
            holder.img4.setVisibility(View.VISIBLE);
            holder.img4.setImageDrawable(context.getResources().getDrawable(iconList.get(company.getTypeList().get(3))));
        } else {
            holder.img4.setVisibility(View.GONE);
        }
        if (company.getTypeList().size() >= 5) {
            holder.img5.setVisibility(View.VISIBLE);
            holder.img5.setImageDrawable(context.getResources().getDrawable(iconList.get(company.getTypeList().get(4))));
        } else {
            holder.img5.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public interface MaintenanceCompanyOnClickListener {
        void onClick(View v, int id);
    }
}
