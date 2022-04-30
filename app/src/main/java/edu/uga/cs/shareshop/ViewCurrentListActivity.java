package edu.uga.cs.shareshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private CurrentListRecyclerAdapter recyclerAdapter; // adapter class

    private List<Item> currentList = null; // list for storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list);

        recyclerView = findViewById(R.id.recyclerView);

        currentList = new ArrayList<Item>();
        recyclerAdapter = new CurrentListRecyclerAdapter(currentList); // pass basic list into the adapter
        recyclerView.setAdapter(recyclerAdapter); // set the view to the adapter

        // read from database

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    } // onCreate
} // ViewCurrentListActivity