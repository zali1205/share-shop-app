package edu.uga.cs.shareshop;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * basic adapter class to show all the purchased items
 *
 * Author - Drew Jenkins
 */
public class SettleRecyclerAdapter extends RecyclerView.Adapter<SettleRecyclerAdapter.SettleHolder> {

    private ArrayList<User> settleList; // list for working with recycler


    public SettleRecyclerAdapter( ArrayList<User> settleList ) {
        this.settleList = settleList;
    } // constructor takes a list to start

    // basic holder class for storing one card's worth of data
    class SettleHolder extends RecyclerView.ViewHolder {
        // ui controls
        TextView name;
        TextView price;

        public SettleHolder(View itemView ) { // constructor with item view to attach objects
            super(itemView);

            // value stores
            name = itemView.findViewById( R.id.name );
            price = itemView.findViewById( R.id.price );
        }
    }

    @Override
    public SettleHolder onCreateViewHolder( ViewGroup parent, int viewType ) { // for proper inflation of the relevant view
        // borrows inflation techniques from jobleadsqlite program
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.settle_item, parent, false );
        return new SettleHolder( view );
    }

    // supplies the necessary view edits for the relevant cards
    @Override
    public void onBindViewHolder( SettleHolder holder, int position ) {
        User user = settleList.get( position ); // find correct position
        // set ui's to proper values from the imported list
        holder.name.setText( "Bought by: " + user.getName() );
        holder.price.setText( "Price: $" + user.getTotal() );
    }

    @Override
    public int getItemCount() { return settleList.size(); }
}