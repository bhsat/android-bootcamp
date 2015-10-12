package com.yahoo.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
   // ArrayList<Todo> todoItems;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    //TodoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.listItems);
        //dbHelper = TodoDbHelper.getsInstance(this);
        readItems();
        //todoItems = new ArrayList<>();
        //todoItems.addAll(dbHelper.getAllItems());
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = itemsAdapter.getItem(position);
                //Toast.makeText(MainActivity.this, "clicked-"+items, Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("item", itemValue);
                i.putExtra("position", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void onAddItem(View v) {
        //Todo item = new Todo();
        EditText newItem = (EditText) findViewById(R.id.editNewItem);
        String itemText = newItem.getText().toString();
        itemsAdapter.add(itemText);
        //item.setTodoItem(itemText);
        newItem.setText("");
        //long id = dbHelper.addTodoItem(item);
        //itemsAdapter.insert(itemText, (int)id);
        writeItems();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getExtras().getString("editItem");
            int pos = data.getExtras().getInt("position");
            items.remove(pos);
            items.add(pos, text);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todofile.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todofile.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupListViewListener() {
        lvItems.setLongClickable(true);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
