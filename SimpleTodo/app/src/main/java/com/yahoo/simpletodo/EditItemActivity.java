package com.yahoo.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private String itemText;
    private EditText editListItem;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemText = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position", 0);
        editListItem = (EditText) findViewById(R.id.editText);
        editListItem.setText(itemText);
        editListItem.setSelection(itemText.length());
        //setContentView(editListItem);
    }

    public void editItem(View v) {
        EditText editItem = (EditText) findViewById(R.id.editText);
        Intent editData = new Intent();
        editData.putExtra("editItem", editItem.getText().toString());
        editData.putExtra("position", position);
        setResult(RESULT_OK, editData);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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
