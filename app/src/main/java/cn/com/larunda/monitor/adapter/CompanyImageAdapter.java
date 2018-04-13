package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.bean.MaintenanceCompany;

/**
 * Created by sddt on 18-4-9.
 */

public class CompanyImageAdapter extends RecyclerView.Adapter<CompanyImageAdapter.ViewHolder> {
    private List<String> typeList = new ArrayList<>();
    private Context context;
    private HashMap<String, Integer> iconList = new HashMap<>();

    public CompanyImageAdapter(List<String> typeList, Context context, HashMap<String, Integer> iconList) {
        this.typeList = typeList;
        this.context = context;
        this.iconList = iconList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_company_list_item_image);
            textView = itemView.findViewById(R.id.item_company_list_item_image_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company_list_item_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = typeList.get(position);
        holder.imageView.setImageDrawable(context.getResources().getDrawable(iconList.get(name)));
        if (name != null && name.equals("用电安全")) {
            holder.textView.setText(" 电");
        } else {
            holder.textView.setText(" " + name);
        }
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}
