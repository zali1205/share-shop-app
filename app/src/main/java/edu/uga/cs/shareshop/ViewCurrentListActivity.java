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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the created recycler adapter to populate a layout with proper cards
 *
 * Author - Drew Jenkins
 */
public class ViewCurrentListActivity extends AppCompatActivity {

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

        // listener for handling reading from the databse as it is updated in real time.
        dbRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    if ( !item.getIsPurchased() ) // needs to not be purchased
                    {
                        currentList.add(item);
                        Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): added: " + item );
                    }  // if
                }  // for
                Log.d( TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a CurrentRecyclerAdapter to populate a ReceyclerView to display the job leads.
                recyclerAdapter = new CurrentRecyclerAdapter( currentList, new CurrentRecyclerAdapter.ButtonListeners() {
                    @Override
                    public void payOnClick(View v, int position) {
                        Log.d(TAG, "payOnClick at position "+position);
                    } // pay on click
                    @Override
                    public void editOnClick(View v, int position) {
                        Log.d(TAG, "editOnClick at position "+position);
                    } // edit on click
                    @Override
                    public void deleteOnClick(View v, int position) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        // Query delQuery =db.child("")
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
} // ViewCurrentListActivity