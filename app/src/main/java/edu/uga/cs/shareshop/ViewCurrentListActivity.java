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
 */
public class ViewCurrentListActivity extends AppCompatActivity implements PayItemDialogFragment.PayItemDialogListener {

    private final String TAG = "testing recycler view";

    private RecyclerView recyclerView; // view
    private RecyclerView.LayoutManager layoutManager;
    private CurrentRecyclerAdapter recyclerAdapter; // adapter class

    private List<Item> currentList; // list for storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list);

        recyclerView = findViewById(R.id.recyclerView);

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener( new View.OnClickListener() {
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
                currentList.clear();
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    if ( !item.getIsPurchased() ) // needs to not be purchased
                    {
                        currentList.add(item);
                        Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): added: " + item.getName() );
                    }  // if
                }  // for
                Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a CurrentRecyclerAdapter to populate a RecyclerView to display the job leads.
                recyclerAdapter = new CurrentRecyclerAdapter( currentList, new CurrentRecyclerAdapter.ButtonListeners() {
                    @Override
                    public void payOnClick(View v, int position) {
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
                    public void editOnClick(View v, int position) {
                        Intent intent = new Intent(v.getContext(), EditItemActivity.class);
                        //String itemName = currentList.get(position).getName();
                        Item item = currentList.get(position);
                        intent.putExtra("Item", item);
                        v.getContext().startActivity(intent);
                        Log.d(TAG, "editOnClick at position " + item);
                    } // edit on click
                    @Override
                    public void deleteOnClick(View v, int position) {
                        String search = currentList.get(position).getName();

                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = db.getReference();
                        Query delQuery =dbRef.child("Items").orderByChild("name").equalTo(search);

                        delQuery.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot )
                            {
                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren() )
                                {
                                    postSnapshot.getRef().removeValue();
                                } // for
                            } // on data change

                            @Override
                            public void onCancelled( DatabaseError databaseError )
                            {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            } // on cancelled
                        });
                        currentList.remove(position);
                        recyclerAdapter.notifyItemRemoved(position);
                        recyclerView.setAdapter( recyclerAdapter );
                    } // delete on click
                }); // setting the recycler adapter
                recyclerView.setAdapter( recyclerAdapter );
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