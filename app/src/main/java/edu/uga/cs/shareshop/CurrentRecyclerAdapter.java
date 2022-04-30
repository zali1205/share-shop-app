package edu.uga.cs.shareshop;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * basic adapter class to show all the current items
 *
 * Author - Drew Jenkins
 */
public class CurrentRecyclerAdapter extends RecyclerView.Adapter<CurrentRecyclerAdapter.CurrentHolder> {

    private List<Item> currentList; // list for working with recycler

    public CurrentRecyclerAdapter( List<Item> currentList ) {
        this.currentList = currentList;
    } // constructor takes a list to start

    // basic holder class for storing one card's worth of data
    class CurrentHolder extends RecyclerView.ViewHolder {
        // ui controls
        TextView id;
        TextView date;
        TextView result;

        public CurrentHolder(View itemView ) { // constructor with item view to attach objects
            super(itemView);

            id = itemView.findViewById( R.id.id );
            date = itemView.findViewById( R.id.date );
            result = itemView.findViewById( R.id.result );
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
        holder.id.setText( "Set # :\t\t\t" + quiz.getId() );
        holder.date.setText( "Date :\t\t\t" + quiz.getQuizDate() );
        holder.result.setText( "# correct : \t"+ quiz.getResult() );
    }

    @Override
    public int getItemCount() { // needed for the recycler to populate
        if( currentList != null )
            return currentList.size();
        else
            return 0;
    }
}