package com.example.rift.jiofinal.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rift.jiofinal.Event.Event;
import com.example.rift.jiofinal.Event.ItemClickListener;
import com.example.rift.jiofinal.EventDetailActivity;
import com.example.rift.jiofinal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.EventViewHolder> {

    private final LayoutInflater inflater;
    View view;
    EventViewHolder holder;
    //Creating an arraylist of events objects
    private ArrayList<Event> events = new ArrayList<>();
    private Context context;
    private AlertDialog.Builder build;


    public CustomAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.events = events;
    }


    //This method inflates view present in the RecyclerView
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.event_list_item, parent, false);
        holder = new EventViewHolder(view);
        return holder;
    }

    //Binding the data using get() method of events object
    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        Event list_items = events.get(position);
        holder.rank.setText(list_items.getRank());
        holder.name.setText(list_items.getName());
        holder.score.setText(list_items.getScore());

/*     Glide.with(context)
                .load(list_items.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.event_image)
                .into(holder.picture);
*/

     /*   holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                Event eventCurrent = events.get(position);
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("eventObject", eventCurrent);
                context.startActivity(intent);

            }
        });
*/
    }

  /*  public String parseDate(String date, int activity) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern2 = "d\nMMM";
        String outputPattern1 = "EEE, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat2 = new SimpleDateFormat(outputPattern2);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);
        Date formatDate;
        String str = null;

        try {
            formatDate = inputFormat.parse(date);
            if (activity == 2)
                str = outputFormat2.format(formatDate);
            else
                str = outputFormat1.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
*/

    public void setListContent(ArrayList<Event> list_members) {
        this.events = list_members;
        notifyItemRangeChanged(0, list_members.size());

    }


    @Override
    public int getItemCount() {
        return events.size();
    }

/*    public void removeAt(int position) {
        events.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, events.size());
    }*/

    //View holder class, where all view components are defined
    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        TextView rank, name, score;
      //  ImageView picture;
        private ItemClickListener itemClickListener;

        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            rank = (TextView) itemView.findViewById(R.id.rank);
            name = (TextView) itemView.findViewById(R.id.name);
            score = (TextView) itemView.findViewById(R.id.score);
          //  picture = (ImageView) itemView.findViewById(R.id.picture);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

}



