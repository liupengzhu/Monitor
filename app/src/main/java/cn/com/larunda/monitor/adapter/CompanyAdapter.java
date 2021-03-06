package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import cn.com.larunda.circleview.CircleTextView;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.Company;
import cn.com.larunda.monitor.bean.MaintenanceCompany;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sddt on 18-3-21.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    private Context context;
    private List<Company> companyList = new ArrayList<>();
    private HashMap<String, Integer> iconList = new HashMap<>();
    private AlarmOnClickListener alarmOnClickListener;
    private MaintenanceOnClickListener maintenanceOnClickListener;
    private TelOnClickListener telOnClickListener;

    public CompanyAdapter(Context context, List<Company> companyList, HashMap<String, Integer> iconList) {
        this.context = context;
        this.companyList = companyList;
        this.iconList = iconList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;
        TextView industry;
        TextView tel;
        TextView address;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;
        CircleTextView circleTextView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;

        TextView alarm;
        TextView maintenance;

        RecyclerView recyclerView;
        RecyclerView recyclerViewRight;

        LinearLayout telLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.company_list_item_company_img);
            name = itemView.findViewById(R.id.company_list_item_name);
            industry = itemView.findViewById(R.id.company_list_item_industry);
            tel = itemView.findViewById(R.id.company_list_item_tel);
            address = itemView.findViewById(R.id.company_list_item_address);
           /* img1 = itemView.findViewById(R.id.company_list_item_img1);
            img2 = itemView.findViewById(R.id.company_list_item_img2);
            img3 = itemView.findViewById(R.id.company_list_item_img3);
            img4 = itemView.findViewById(R.id.company_list_item_img4);
            img5 = itemView.findViewById(R.id.company_list_item_img5);*/
            circleTextView = itemView.findViewById(R.id.company_list_item_circle_text);
            /*textView1 = itemView.findViewById(R.id.company_list_item_text1);
            textView2 = itemView.findViewById(R.id.company_list_item_text2);
            textView3 = itemView.findViewById(R.id.company_list_item_text3);
            textView4 = itemView.findViewById(R.id.company_list_item_text4);
            textView5 = itemView.findViewById(R.id.company_list_item_text5);*/
            alarm = itemView.findViewById(R.id.company_list_item_alarm_text);
            maintenance = itemView.findViewById(R.id.company_list_item_maintenance_text);

            recyclerView = itemView.findViewById(R.id.company_list_item_recycler);
            recyclerViewRight = itemView.findViewById(R.id.company_list_item_right_recycler);

            telLayout = itemView.findViewById(R.id.company_list_item_tel_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmOnClickListener != null) {
                    alarmOnClickListener.onClick(v, companyList.get(position).getId());
                }
            }
        });

        holder.maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maintenanceOnClickListener != null) {
                    maintenanceOnClickListener.onClick(v, companyList.get(position).getId());
                }
            }
        });
        final Company company = companyList.get(position);
        if (company.getImg() != null) {
            Glide.with(context).load(company.getImg())
                    .placeholder(R.drawable.circle_null)
                    .error(R.drawable.circle_null).into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_null));
        }
        holder.name.setText(company.getName() + "");
        holder.industry.setText(company.getIndustry() + "");
        holder.tel.setText(company.getTel() + "");
        holder.address.setText(company.getAddress() + "");
        holder.alarm.setText("警报数量: " + company.getAlarm() + "个");
        holder.maintenance.setText("最近维护: " + company.getMaintenance() + "次");
        holder.circleTextView.setNumber(company.getTotal());
        if (company.getAngle() > 240) {
            holder.circleTextView.setCircleColor(context.getResources().getColor(R.color.circle_color1));
            holder.circleTextView.setNumberColor(context.getResources().getColor(R.color.circle_color1));
        } else if (company.getAngle() > 120) {
            holder.circleTextView.setCircleColor(context.getResources().getColor(R.color.circle_color2));
            holder.circleTextView.setNumberColor(context.getResources().getColor(R.color.circle_color2));
        } else {
            holder.circleTextView.setCircleColor(context.getResources().getColor(R.color.circle_color3));
            holder.circleTextView.setNumberColor(context.getResources().getColor(R.color.circle_color3));
        }
        holder.circleTextView.setCircleAngle(company.getAngle());
        /*String electricText = "<font color='#999999'>安全用电:</font>" + " " + "<font color='#d94a2b'>"
                + company.getElectric() + "</font>" + "<font color='#333333'>台</font>";
        holder.textView1.setText(Html.fromHtml(electricText));
        if (company.getDeviceList().size() >= 1) {
            holder.textView2.setVisibility(View.VISIBLE);
            String text2 = "<font color='#999999'>" + company.getDeviceList().get(0).split(" ")[0] + ": "
                    + "</font>" + "<font color='#d94a2b'>"
                    + company.getDeviceList().get(0).split(" ")[1] + "</font>" + "<font color='#333333'>台</font>";
            holder.textView2.setText(Html.fromHtml(text2));
        } else {
            holder.textView2.setVisibility(View.GONE);
        }

        if (company.getDeviceList().size() >= 2) {
            holder.textView3.setVisibility(View.VISIBLE);
            String text3 = "<font color='#999999'>" + company.getDeviceList().get(1).split(" ")[0] + ": "
                    + "</font>" + "<font color='#d94a2b'>"
                    + company.getDeviceList().get(1).split(" ")[1] + "</font>" + "<font color='#333333'>台</font>";
            holder.textView3.setText(Html.fromHtml(text3));
        } else {
            holder.textView3.setVisibility(View.GONE);
        }
        if (company.getDeviceList().size() >= 3) {
            holder.textView4.setVisibility(View.VISIBLE);
            String text4 = "<font color='#999999'>" + company.getDeviceList().get(2).split(" ")[0] + ": "
                    + "</font>" + "<font color='#d94a2b'>"
                    + company.getDeviceList().get(2).split(" ")[1] + "</font>" + "<font color='#333333'>台</font>";
            holder.textView4.setText(Html.fromHtml(text4));
        } else {
            holder.textView4.setVisibility(View.GONE);
        }

        if (company.getDeviceList().size() >= 4) {
            holder.textView5.setVisibility(View.VISIBLE);
            String text5 = "<font color='#999999'>" + company.getDeviceList().get(3).split(" ")[0] + ": "
                    + "</font>" + "<font color='#d94a2b'>"
                    + company.getDeviceList().get(3).split(" ")[1] + "</font>" + "<font color='#333333'>台</font>";
            holder.textView5.setText(Html.fromHtml(text5));
        } else {
            holder.textView5.setVisibility(View.GONE);
        }*/
        if (company.getDeviceList() != null) {
            CompanyDeviceAdapter adapter = new CompanyDeviceAdapter(company.getDeviceList(), this.context);
            LinearLayoutManager manager = new LinearLayoutManager(this.context);
            holder.recyclerViewRight.setAdapter(adapter);
            holder.recyclerViewRight.setLayoutManager(manager);
        }
        if (company.getMaintenanceCompanyList() != null) {
            CompanyItemAdapter adapter = new CompanyItemAdapter(company.getMaintenanceCompanyList(), this.context, iconList);
            LinearLayoutManager manager = new LinearLayoutManager(this.context);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setLayoutManager(manager);
            adapter.setItemOnClickListener(new CompanyItemAdapter.ItemOnClickListener() {
                @Override
                public void telOnClick(String tel) {
                    if (telOnClickListener != null) {
                        if (tel != null) {
                            telOnClickListener.telOnClick(tel);
                        } else {
                            telOnClickListener.telOnClick("");
                        }
                    }
                }
            });
        }
        holder.telLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (telOnClickListener != null) {
                    telOnClickListener.telOnClick(company.getTel());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public interface AlarmOnClickListener {
        void onClick(View v, int id);
    }

    public interface MaintenanceOnClickListener {
        void onClick(View v, int id);
    }

    public void setAlarmOnClickListener(AlarmOnClickListener alarmOnClickListener) {
        this.alarmOnClickListener = alarmOnClickListener;
    }

    public void setMaintenanceOnClickListener(MaintenanceOnClickListener maintenanceOnClickListener) {
        this.maintenanceOnClickListener = maintenanceOnClickListener;
    }

    public interface TelOnClickListener {
        void telOnClick(String tel);
    }

    public void setTelOnClickListener(TelOnClickListener telOnClickListener) {
        this.telOnClickListener = telOnClickListener;
    }
}
