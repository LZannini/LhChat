package com.example.multichat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> messages;

    public MessageAdapter(Context context, ArrayList<String> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_messaggio, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.messageTextView = convertView.findViewById(R.id.text_view_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String message = messages.get(position);
        viewHolder.messageTextView.setText(message);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView messageTextView;
    }
}
