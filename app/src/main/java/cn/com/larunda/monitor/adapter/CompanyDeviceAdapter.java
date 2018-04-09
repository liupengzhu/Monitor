package cn.com.larunda.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-4-9.
 */

public class CompanyDeviceAdapter extends RecyclerView.Adapter<CompanyDeviceAdapter.ViewHolder> {
    private List<String> deviceList = new ArrayList<>();
    private Context context;


    public CompanyDeviceAdapter(List<String> deviceList, Context context) {
        this.deviceList = deviceList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_company_list_text_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company_list_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String device = deviceList.get(position);
        String text = "<font color='#999999'>" + device.split(" ")[0] + ": "
                + "</font>" + "<font color='#d94a2b'>"
                + device.split(" ")[1] + "</font>" + "<font color='#333333'>台</font>";
        holder.textView.setText(Html.fromHtml(text));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
