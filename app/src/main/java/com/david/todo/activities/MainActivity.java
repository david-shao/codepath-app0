package com.david.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditItemDialogListener, Comparator<TodoItem> {

    //sort constants
    private final int SORT_CREATED = 0;
    private final int SORT_PRIORITY = 1;
    private final int SORT_DUE_DATE = 2;
    private final int SORT_ALPHA = 3;

    //Request code for editing item
    private final int REQUEST_CODE = 20;

    private List<TodoItem> todoItems;
    private TodoItemsAdapter aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;

    private int sortOrder = 0;

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
        Collections.sort(todoItems, this);
        aToDoAdapter.notifyDataSetChanged();
        lvItems.setSelection(todoItems.indexOf(newItem));
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
        Collections.sort(todoItems, this);
        aToDoAdapter.notifyDataSetChanged();
        lvItems.setSelection(todoItems.indexOf(todoItem));
        todoItem.save();
    }

    /**
     * Create custom top bar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortPriority:
                sortOrder = SORT_PRIORITY;
                break;
            case R.id.sortDueDate:
                sortOrder = SORT_DUE_DATE;
                break;
            case R.id.sortAlpha:
                sortOrder = SORT_ALPHA;
                break;
            case R.id.sortCreated:
                sortOrder = SORT_CREATED;
                break;
        }
        Collections.sort(todoItems, this);
        aToDoAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public int compare(TodoItem lhs, TodoItem rhs) {
        switch (sortOrder) {
            case SORT_PRIORITY:
                if (lhs.getPriority().getValue() < rhs.getPriority().getValue()) {
                    return -1;
                } else if (lhs.getPriority().getValue() == rhs.getPriority().getValue()) {
                    return 0;
                } else {
                    return 1;
                }
            case SORT_DUE_DATE:
                if (lhs.getDueDate() != null && rhs.getDueDate() != null) {
                    return lhs.getDueDate().compareTo(rhs.getDueDate());
                } else if (lhs.getDueDate() != null) {
                    return -1;
                } else if (rhs.getDueDate() != null) {
                    return 1;
                } else {
                    return 0;
                }
            case SORT_ALPHA:
                return lhs.getText().compareTo(rhs.getText());
            case SORT_CREATED:
            default:
                return lhs.getCreated().compareTo(rhs.getCreated());
        }
    }

}
