package com.example.smc1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URLEncoder;
import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private int layoutResourceId;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    public RecyclerViewAdapter(MainActivity mainActivity, int grid_item, ArrayList<GridItem> mGridData, RecyclerView mGridView) {
        this.mContext =mainActivity;
        this.layoutResourceId = layoutResourceId;
        this.mGridData = mGridData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
            return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        System.out.println("this one"+mGridData.get(position).getDistance());
        System.out.println("teo"+mGridData.get(position).getDuration());
        System.out.println("Slots AVAILABEL:"+position+" "+mGridData.get(position).getSlot());
        holder.name.setText(mGridData.get(position).getName());
        holder.dist.setText(mGridData.get(position).getDistance());
        holder.dur.setText(mGridData.get(position).getDuration());
        holder.slot.setText(Integer.toString(mGridData.get(position).getSlot()));

        holder.dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKABLE "+mGridData.get(position).getName());

                  Intent i=new Intent(mContext, WebViewexample.class);
                  i.putExtra("dest", URLEncoder.encode(mGridData.get(position).getName()));
                  mContext.startActivity(i);
//                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ URLEncoder.encode(mGridData.get(position).getName()));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                mContext.startActivity(mapIntent);
//                System.out.println("SSSSS");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView dist;
        public TextView dur;
        public TextView dir;
        public TextView slot;
        CardView recyclerView;

        public MyViewHolder(View row) {
            super(row);
            name=(TextView) row.findViewById(R.id.name);
            dist = (TextView) row.findViewById(R.id.distance);
            dur = (TextView) row.findViewById(R.id.duration);
            dir=(TextView) row.findViewById(R.id.direction);
            slot=(TextView) row.findViewById(R.id.slot);
//            backImage = (ImageView) row.findViewById(R.id.poster1);
//            recyclerView = (CardView) row.findViewById(R.id.send);
        }
    }
}
