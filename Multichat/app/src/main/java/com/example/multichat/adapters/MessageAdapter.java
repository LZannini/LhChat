package com.example.multichat.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.model.Messaggio;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<Messaggio> messages = new ArrayList<>();

    public MessageAdapter(ArrayList<Messaggio> messages) {
        this.messages = messages;
    }

    /*@Override
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
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_invio_msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Messaggio messaggio = messages.get(position);
        holder.messageTextView.setText(messaggio.getTesto());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
        }
    }
}
