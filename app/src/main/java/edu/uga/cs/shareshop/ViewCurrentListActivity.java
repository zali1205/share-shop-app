package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the created recycler adapter to populate a layout with proper cards
 * Author - Drew Jenkins
 */
public class ViewCurrentListActivity extends AppCompatActivity implements PayItemDialogFragment.PayItemDialogListener {

    private final String TAG = "current list testing";

    private RecyclerView recyclerView; // view
    private RecyclerView.LayoutManager layoutManager;
    private CurrentRecyclerAdapter recyclerAdapter; // adapter class

    private List<Item> currentList; // list for storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list); // inflate

        recyclerView = findViewById(R.id.recyclerView); // bind

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener( new View.OnClickListener() { // launch new item activity
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(v.getContext(), AddNewItemActivity.class);
                startActivity(mainActivityIntent);
            }
        });
        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get a Firebase DB instance reference
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Items");

        currentList = new ArrayList<Item>();

        // listener for handling reading from the database as it is updated in real time.
        dbRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                currentList.clear(); // very important clear
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    if ( !item.getPurchased() ) // needs to not be purchased
                    {
                        currentList.add(item); // update list from read
                        Log.d( TAG, "added: " + item.getName() );
                    }  // if
                }  // for
                Log.d( TAG, "setting recyclerAdapter" );

                // Now, create a CurrentRecyclerAdapter to populate a RecyclerView to display the job leads.
                recyclerAdapter = new CurrentRecyclerAdapter( currentList, new CurrentRecyclerAdapter.ButtonListeners() {
                    @Override
                    public void payOnClick(View v, int position) { // setting intent for the pay dialog
                        Log.d(TAG, "payOnClick at position "+position);
                        DialogFragment newFragment = new PayItemDialogFragment();
                        Bundle args = new Bundle();
                        Item item = currentList.get(position);
                        args.putSerializable("Item", item);
                        args.putInt("Position", position);
                        newFragment.setArguments(args);
                        showDialogFragment(newFragment);
                    } // pay on click
                    @Override
                    public void editOnClick(View v, int position) { // setting intent for editing
                        Intent intent = new Intent(v.getContext(), EditItemActivity.class);
                        Item item = currentList.get(position);
                        intent.putExtra("Item", item);
                        v.getContext().startActivity(intent);
                        Log.d(TAG, "editOnClick at position " + item);
                    } // edit on click
                    @Override
                    public void deleteOnClick(View v, int position) {
                        String search = currentList.get(position).getName(); // get name of item selected

                        // open up database reference
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = db.getReference();
                        Query delQuery =dbRef.child("Items").orderByChild("name").equalTo(search); // find the search

                        delQuery.addListenerForSingleValueEvent( new ValueEventListener() { // only listen once please
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot )
                            {
                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren() )
                                {
                                    postSnapshot.getRef().removeValue(); // once found remove
                                } // for
                            } // on data change

                            @Override
                            public void onCancelled( DatabaseError databaseError )
                            {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            } // on cancelled
                        });
                        currentList.remove(position); // update list
                        recyclerAdapter.notifyItemRemoved(position); // notify adapter
                        recyclerView.setAdapter( recyclerAdapter ); // reset
                    } // delete on click
                }); // setting the recycler adapter
                recyclerView.setAdapter( recyclerAdapter ); // set at end of data change
            } // on data change

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            } // on cancelled
        } );
    } // onCreate

    @Override
    public void onFinishNewJobDialog(int position) {
        currentList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void showDialogFragment(DialogFragment newFragment) {
        newFragment.show(getSupportFragmentManager(), null);
    }
} // ViewCurrentListActivity