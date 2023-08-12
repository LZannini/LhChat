package com.example.multichat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.VediRichiesteActivity;
import com.example.multichat.model.Richiesta_stanza;

import java.util.ArrayList;

public class RichiesteAdapter extends RecyclerView.Adapter<RichiesteAdapter.ViewHolder> {
    private ArrayList<Richiesta_stanza> richieste = new ArrayList<>();
    private OnUtenteListener onUtenteListener;

    public RichiesteAdapter(ArrayList<Richiesta_stanza> richieste, RichiesteAdapter.OnUtenteListener onUtenteListener){
        this.richieste = richieste;
        this.onUtenteListener = (OnUtenteListener) onUtenteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_richiesta, parent, false);
        return new ViewHolder(view, onUtenteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RichiesteAdapter.ViewHolder holder, int position) {
        Richiesta_stanza item = richieste.get(position);
        holder.textView.setText(item.getUtente());
    }


    @Override
    public int getItemCount() {
        return richieste.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        RichiesteAdapter.OnUtenteListener onUtenteListener;

        public ViewHolder(View itemView, RichiesteAdapter.OnUtenteListener onUtenteListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.richiesteNametextView);

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

    public void rimuoviRichiesta(int position) {
        if (position >= 0 && position < richieste.size()) {
            richieste.remove(position);
            notifyItemRemoved(position);
        }
    }
}
