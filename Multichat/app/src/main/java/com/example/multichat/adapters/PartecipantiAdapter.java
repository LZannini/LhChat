package com.example.multichat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.model.Appartenenza_stanza;
import com.example.multichat.model.Utente;

import java.util.ArrayList;

public class PartecipantiAdapter extends RecyclerView.Adapter<PartecipantiAdapter.ViewHolder>{
    private ArrayList<Appartenenza_stanza> partecipanti = new ArrayList<>();
    private OnUtenteListener onUtenteListener;

    public PartecipantiAdapter(ArrayList<Appartenenza_stanza> partecipanti, PartecipantiAdapter.OnUtenteListener onUtenteListener){
        this.partecipanti = partecipanti;
        this.onUtenteListener = onUtenteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partecipanti, parent, false);
        return new ViewHolder(view, onUtenteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PartecipantiAdapter.ViewHolder holder, int position) {
        Appartenenza_stanza item = partecipanti.get(position);
        holder.textView.setText(item.getUsername());
    }


    @Override
    public int getItemCount() {
        return partecipanti.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        PartecipantiAdapter.OnUtenteListener onUtenteListener;

        public ViewHolder(View itemView, PartecipantiAdapter.OnUtenteListener onUtenteListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.partecipantiNametextView);

            this.onUtenteListener = onUtenteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onUtenteListener.onUtenteClick(getAdapterPosition());
        }
    }

    public interface OnUtenteListener{
        void onUtenteClick(int position);
    }
}
