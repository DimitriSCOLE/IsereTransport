package com.mazzone.isere.transport.ui.fragment.stop;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mazzone.isere.transport.R;
import com.mazzone.isere.transport.ui.model.HourItem;
import com.mazzone.isere.transport.util.MapItemUtil;

import java.util.ArrayList;
import java.util.List;

import static com.mazzone.isere.transport.util.DateUtil.TIME_PATTERN;
import static com.mazzone.isere.transport.util.DateUtil.dateToString;

public class StopHoursAdapter extends RecyclerView.Adapter<StopHoursAdapter.ViewHolder> {
    private List<HourItem> mListHours;
    private Context context;

    public StopHoursAdapter(Context context) {
        this.context = context;
        mListHours = new ArrayList<>();
    }

    public void setListHours(List<HourItem> listHours) {
        if (listHours == null) { // Avoid null
            mListHours.clear();
        } else {
            mListHours = listHours;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourItem current = mListHours.get(position);
        holder.name.setText(current.getLineNumber());
        holder.direction.setText(current.getJourneyDestination());
        holder.time.setText(dateToString(current.getTheoreticalDepartureDateTime(), TIME_PATTERN));

        int operatorColor = ContextCompat.getColor(context, MapItemUtil.getColorResourceFromOperatorId(current.getOperatorId()));
        holder.name.setBackgroundColor(operatorColor);
    }

    @Override
    public int getItemCount() {
        return mListHours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView direction;
        public TextView time;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            direction = (TextView) view.findViewById(R.id.direction);
            time = (TextView) view.findViewById(R.id.time);
        }
    }
}
