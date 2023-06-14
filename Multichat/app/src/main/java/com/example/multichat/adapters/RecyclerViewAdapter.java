package com.example.multichat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Stanza> stanze = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<Stanza> lista_stanze) {
        this.stanze = lista_stanze;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stanza, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Stanza item = stanze.get(position);
        holder.textView.setText(item.getNome_stanza());
    }


    @Override
    public int getItemCount() {
        return stanze.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    /*public interface StanzeListener {
        void onStanzaClick(int position);
    }*/
}