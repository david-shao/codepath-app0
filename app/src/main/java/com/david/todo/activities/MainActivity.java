package com.david.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.adapters.TodoItemsAdapter;
import com.david.todo.fragments.EditItemFragment;
import com.david.todo.models.TodoItem;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditItemDialogListener {

    //Request code for editing item
    private final int REQUEST_CODE = 20;

    private List<TodoItem> todoItems;
    private TodoItemsAdapter aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Stetho debug tool setup
        Stetho.initializeWithDefaults(this);

        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // add for verbose logging
        // FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                item.delete();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = todoItems.get(position);
                //use activity to display edit view
//                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
//                i.putExtra("todoItem", todoItem);
//                i.putExtra("pos", position);
//                startActivityForResult(i, REQUEST_CODE);

                //use fragment to display edit view
                FragmentManager fm = getSupportFragmentManager();
                EditItemFragment editItemFragment = EditItemFragment.newInstance(position, todoItem);
                editItemFragment.show(fm, "fragment_edit_item");
            }
        });

        //set enter key listener
        etEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onAddItem(v);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Callback when other activities finish.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int pos = data.getExtras().getInt("pos", 0);
            TodoItem modified = (TodoItem) data.getSerializableExtra("todoItem");
            todoItems.set(pos, modified);
            aToDoAdapter.notifyDataSetChanged();
            modified.save();
        }
    }

    /**
     * Populate initial list of items.
     */
    public void populateArrayItems() {
        readFromDb();
        aToDoAdapter = new TodoItemsAdapter(this, todoItems);
    }

    /**
     * Handler for adding an item.
     * @param view
     */
    public void onAddItem(View view) {
        TodoItem newItem = new TodoItem();
        newItem.setText(etEditText.getText().toString());
        aToDoAdapter.add(newItem);
        lvItems.setSelection(aToDoAdapter.getCount() - 1);
        etEditText.setText("");
        newItem.save();
    }

    /**
     * Helper function to query db and populate list.
     */
    private void readFromDb() {
        todoItems = SQLite.select().
                from(TodoItem.class).
                queryList();
    }

    /**
     * Helper function to read items from file.
     */
    private void readItems() {
//        File filesDir = getFilesDir();
//        File file = new File(filesDir, "todo.txt");
//        try {
//            todoItems = new ArrayList<String>(FileUtils.readLines(file));
//        } catch (FileNotFoundException e) {
//            todoItems = new ArrayList<String>();
//        } catch (IOException e) {
//
//        }
    }

    /**
     * Helper function to write items to file.
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
    }

    /**
     * Implement EditItemDialogListener here to get result from fragment.
     * @param position
     * @param todoItem
     */
    @Override
    public void onSave(int position, TodoItem todoItem) {
        todoItems.set(position, todoItem);
        aToDoAdapter.notifyDataSetChanged();
        todoItem.save();
    }

}
