package edu.uga.cs.shareshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
 *
 * Author - Drew Jenkins
 */
public class ViewPurchasedListActivity extends AppCompatActivity {

    private final String TAG = "testing recycler view";

    private RecyclerView recyclerView; // view
    private RecyclerView.LayoutManager layoutManager;
    private PurchasedRecyclerAdapter recyclerAdapter; // adapter class

    private List<Item> purchasedList; // list for storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_list);

        recyclerView = findViewById(R.id.recyclerView2);

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get a Firebase DB instance reference
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Items");

        purchasedList = new ArrayList<Item>();

        // listener for handling reading from the databse as it is updated in real time.
        dbRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    if ( item.getIsPurchased() ) // needs to not be purchased
                    {
                        purchasedList.add(item);
                        Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): added: " + item );
                    }  // if
                }  // for
                Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a purchasedRecyclerAdapter to populate a ReceyclerView to display the job leads.
                recyclerAdapter = new PurchasedRecyclerAdapter( purchasedList, new PurchasedRecyclerAdapter.ButtonListeners() {
                    @Override
                    public void undoOnClick(View v, int position) {
                        String search = purchasedList.get(position).getName();

                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = db.getReference();
                        Query delQuery =dbRef.child("Items").orderByChild("name").equalTo(search);

                        delQuery.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot )
                            {
                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren() )
                                {
                                    postSnapshot.getRef().child("isPurchased").setValue(false);
                                    postSnapshot.getRef().child("purchaser").setValue(null);
                                } // for
                            } // on data change

                            @Override
                            public void onCancelled( DatabaseError databaseError )
                            {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            } // on cancelled
                        });
                        purchasedList.remove(position);
                        recyclerAdapter.notifyItemRemoved(position);
                        recyclerView.setAdapter( recyclerAdapter );
                    } // pay on click
                    @Override
                    public void editOnClick(View v, int position) {
                        Log.d(TAG, "editOnClick at position "+position);
                    } // edit on click
                    @Override
                    public void deleteOnClick(View v, int position) {
                        String search = purchasedList.get(position).getName();

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
                        purchasedList.remove(position);
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
} // ViewPurchasedListActivity