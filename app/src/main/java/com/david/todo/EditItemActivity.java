package com.david.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Screen for editing an item.
 */
public class EditItemActivity extends AppCompatActivity {

    private int position;
    private String text;
    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        text = getIntent().getStringExtra("text");
        position = getIntent().getIntExtra("pos", 0);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(text);
    }

    /**
     * Handler for save button.
     * @param view
     */
    public void onSave(View view) {
        text = etEditItem.getText().toString();
        Intent data = new Intent();
        data.putExtra("text", text);
        data.putExtra("pos", position);
        setResult(RESULT_OK, data);
        finish();
    }
}
