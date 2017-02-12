package com.david.todo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.models.TodoItem;

import java.util.List;

/**
 * Created by David on 2/11/2017.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {

    // View lookup cache
    private static class ViewHolder {
        TextView tvText;
    }

    public TodoItemsAdapter(Context context, List<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    /**
     * Translates the model to view for display.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            // Lookup view for data population
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvText.setText(todoItem.getText());
        // Return the completed view to render on screen
        return convertView;
    }
}
