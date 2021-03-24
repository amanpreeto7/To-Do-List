package com.dgl114.improvedtodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AddItemInterface {

    private EditText mItemEditText;
    private TextView mItemListTextView;
    private ToDoList mToDoList;
    AddItemFragment addItemFragment;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get view
        mItemListTextView = findViewById(R.id.itemList);

        //Get model
        mToDoList = new ToDoList(this);

        //implemented the long click on the text view to remove the list
        mItemListTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //clear to-do list data from view
                //Call on ToDoList method
                mToDoList.clear();
                try {
                    mToDoList.saveToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                displayList();
                return false;
            }
        });
    }


    //method used to inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Override method to handle the click on the menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                {
                    addItemFragment = new AddItemFragment(this);
                    addItemFragment.show(this.getSupportFragmentManager(), TAG);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            // Attempt to load a previously saved list
            mToDoList.readFromFile();
            displayList();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // Save list for later
            mToDoList.saveToFile();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayList() {
        // Display a numbered list of items
        StringBuffer itemText = new StringBuffer();
        String[] items = mToDoList.getItems();
        for (int i = 0; i < items.length; i++) {
            itemText.append(i + 1).append(". ").append(items[i]).append("\n");
        }
        mItemListTextView.setText(itemText);
    }


    //Interface used to get the item name added in the dialog fragment
    @Override
    public void addItem(String item) {
        if (item.length() > 0) {
            mToDoList.addItem(item);
            displayList();
        }
    }
}