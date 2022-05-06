package edu.uga.cs.shareshop;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * basic adapter class to show all the current items
 *
 * Author - Drew Jenkins
 */
public class CurrentRecyclerAdapter extends RecyclerView.Adapter<CurrentRecyclerAdapter.CurrentHolder> {

    private List<Item> currentList; // list for working with recycler

    public ButtonListeners onClick; // need these for linking all buttons of cards

    public interface ButtonListeners {
        // self explanatory names
        void payOnClick( View v, int position );
        void editOnClick( View v, int position );
        void deleteOnClick( View v, int position );
    }

    public CurrentRecyclerAdapter( List<Item> currentList, ButtonListeners listener ) {
        this.currentList = currentList;
        onClick = listener;
    } // constructor takes a list to start

    // basic holder class for storing one card's worth of data
    class CurrentHolder extends RecyclerView.ViewHolder {
        // ui controls
        TextView title;
        ImageView status;
        TextView detail;
        ImageView pay;
        ImageView edit;
        ImageView delete;

        public CurrentHolder(View itemView ) { // constructor with item view to attach objects
            super(itemView);

            // value stores
            title = itemView.findViewById( R.id.title );
            status = itemView.findViewById( R.id.urgency );
            detail = itemView.findViewById( R.id.detail );

            // button identifiers
            pay = itemView.findViewById( R.id.pay );
            edit = itemView.findViewById( R.id.edit );
            delete = itemView.findViewById( R.id.delete );

            // setting click listeners, they need positions to provide
            pay.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.payOnClick( view, getAdapterPosition() );
                }
            });
            edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.editOnClick( view, getAdapterPosition() );
                }
            });
            delete.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.deleteOnClick( view, getAdapterPosition() );
                }
            });
        }
    }

    @Override
    public CurrentHolder onCreateViewHolder( ViewGroup parent, int viewType ) { // for proper inflation of the relevant view
        // borrows inflation techniques from jobleadsqlite program
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.current_item, parent, false );
        return new CurrentHolder( view );
    }

    // supplies the necessary view edits for the relevant cards
    @Override
    public void onBindViewHolder( CurrentHolder holder, int position ) {
        Item item = currentList.get( position ); // find correct position
        // set ui's to proper values from the imported list
        holder.title.setText( item.getName() );
        // fix the status sign
        String check = item.getPriority();
        if ( check.equals("Please") )
        {
            holder.status.setImageResource( R.drawable.please );
        } // if
        else if ( check.equals("Wanted") )
        {
            holder.status.setImageResource( R.drawable.wanted );
        } // else if
        else if ( check.equals("Urgent") )
        {
            holder.status.setImageResource( R.drawable.urgency );
        } // else if

        holder.detail.setText( item.getDetail() );
    }

    @Override
    public int getItemCount() { return currentList.size(); }
}