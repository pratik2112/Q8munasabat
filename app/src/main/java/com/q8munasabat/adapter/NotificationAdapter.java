package com.q8munasabat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.q8munasabat.R;
import com.q8munasabat.model.NotificationResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.q8munasabat.config.CommonFunctions.parseDateToddMMyyyy;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    AdapterCallback adapterCallback;
    NotificationResponse NotificationResponse;
    public List<NotificationResponse.DataEntity> dataEntities = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_date;
        public LinearLayout ll_main;

        public MyViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public NotificationAdapter(Context mContext, NotificationResponse NotificationResponse, AdapterCallback adapterCallback1) {
        this.mContext = mContext;
        this.NotificationResponse = NotificationResponse;
        this.dataEntities.addAll(this.NotificationResponse.data);
        this.adapterCallback = adapterCallback1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final NotificationResponse.DataEntity dataEntity = NotificationResponse.data.get(position);
            holder.tv_title.setText(dataEntity.message);

            try {
                String start_date = parseDateToddMMyyyy("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", dataEntity.createddate);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en"));

                Date startDate = sdf.parse(start_date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en"));// HH:mm:ss");
                String end_date = df.format(calendar.getTime());
                Date enddt = df.parse(end_date);
                holder.tv_date.setText(df.format(startDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataEntities.size();
    }

    public interface AdapterCallback {
        void onMyAdsClick();
    }
}