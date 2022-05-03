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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class uses the created recycler adapter to populate a layout with proper cards
 *
 * Author - Drew Jenkins
 */
public class SettleUpActivity extends AppCompatActivity {

    private final String TAG = "testing recycler view";

    private RecyclerView recyclerView; // view
    private RecyclerView.LayoutManager layoutManager;
    private SettleRecyclerAdapter recyclerAdapter; // adapter class

    private ArrayList<User> settleList; // list for storage
    private ArrayList<String> identities;
   // private ArrayList<User> contributionList; // list for payments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);

        recyclerView = findViewById(R.id.contributionView);

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        // get a Firebase DB instance reference
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Items");

        FloatingActionButton floatingButton = findViewById(R.id.floatingCompleteButton);
        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query delQuery =dbRef.child("Items").orderByChild("isPurchased").equalTo(true);

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

                Intent mainActivityIntent = new Intent(v.getContext(), MainMenu.class);
                startActivity(mainActivityIntent);
            }
        });

        settleList = new ArrayList<User>();
        identities = new ArrayList<String>();


        // listener for handling reading from the database as it is updated in real time.
        dbRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    if ( item.getIsPurchased() ) // needs to not be purchased
                    {
                        int index = -1;
                        User user = new User( item.getPurchaser(), item.getPrice() );
                        if ( (index = identities.indexOf(item.getPurchaser()) ) == -1 ) {
                            identities.add(item.getPurchaser());
                            settleList.add(user);
                            Log.d( TAG, "cont: added: " +user.getName() + user.getTotal() );

                        } // if
                        else {
                            User increment = new User(settleList.get(index).getName(), settleList.get(index).getTotal());
                            increment.addTotal(user.getTotal());
                            settleList.set(index, increment);
                            Log.d(TAG, "cont: updated: " + increment.getName() + increment.getTotal());

                        } // else
                    }  // if
                }  // for

                recyclerAdapter = new SettleRecyclerAdapter( settleList );
                recyclerView.setAdapter( recyclerAdapter );
            } // on data change

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            } // on cancelled
        });
    } // onCreate

} // SettleUpActivity