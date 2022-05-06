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
 * basic adapter class to show all the purchased items
 *
 * Author - Drew Jenkins
 */
public class PurchasedRecyclerAdapter extends RecyclerView.Adapter<PurchasedRecyclerAdapter.PurchasedHolder> {

    private List<Item> purchasedList; // list for working with recycler

    public ButtonListeners onClick; // interface class reference

    public interface ButtonListeners {
        // interface for new set of buttons on the cards
        void undoOnClick( View v, int position );
        void editOnClick( View v, int position );
        void deleteOnClick( View v, int position );
    }

    // adapter needs list and listeners for button controls
    public PurchasedRecyclerAdapter( List<Item> purchasedList, ButtonListeners listener ) {
        this.purchasedList = purchasedList;
        onClick = listener;
    } // constructor takes a list to start

    // basic holder class for storing one card's worth of data
    class PurchasedHolder extends RecyclerView.ViewHolder {
        // ui controls
        TextView title;
        TextView detail;
        ImageView undo;
        ImageView edit;
        ImageView delete;

        public PurchasedHolder(View itemView ) { // constructor with item view to attach objects
            super(itemView);

            // value stores
            title = itemView.findViewById( R.id.title2 );
            detail = itemView.findViewById( R.id.detail2 );

            // button identifiers
            undo = itemView.findViewById( R.id.undo );
            edit = itemView.findViewById( R.id.edit2 );
            delete = itemView.findViewById( R.id.delete2 );

            // setting click listeners
            undo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.undoOnClick( view, getAdapterPosition() );
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
    public PurchasedHolder onCreateViewHolder( ViewGroup parent, int viewType ) { // for proper inflation of the relevant view
        // borrows inflation techniques from jobleadsqlite program
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.purchased_item, parent, false );
        return new PurchasedHolder( view );
    }

    // supplies the necessary view edits for the relevant cards
    @Override
    public void onBindViewHolder( PurchasedHolder holder, int position ) {
        Item item = purchasedList.get( position ); // find correct position
        // set ui's to proper values from the imported list
        holder.title.setText( item.getName() );

        holder.detail.setText( "Bought by: " + item.getPurchaser() + "\nPrice: $" + item.getPrice() + "\n" + item.getDetail() );
    }

    @Override
    public int getItemCount() { return purchasedList.size(); }
}