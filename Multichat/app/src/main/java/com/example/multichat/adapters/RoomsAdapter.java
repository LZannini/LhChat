package com.example.multichat.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    private ArrayList<Stanza> stanze = new ArrayList<>();
    private OnStanzaListener onStanzaListener;

    public RoomsAdapter(ArrayList<Stanza> stanze, RoomsAdapter.OnStanzaListener onStanzaListener){
        this.stanze = stanze;
        this.onStanzaListener = onStanzaListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stanza, parent, false);
        return new ViewHolder(view, onStanzaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.ViewHolder holder, int position) {
        Stanza item = stanze.get(position);
        holder.textView.setText(item.getNome_stanza());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onStanzaListener != null) {
                    onStanzaListener.onStanzaClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return stanze.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        OnStanzaListener onStanzaListener;

        public ViewHolder(View itemView, OnStanzaListener onStanzaListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.roomNametextView);

            this.onStanzaListener = onStanzaListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStanzaListener.onStanzaClick(getAdapterPosition());
        }
    }

    public interface OnStanzaListener{
        void onStanzaClick(int position);
    }
}