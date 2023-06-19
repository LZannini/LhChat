package com.example.multichat.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.R;
import com.example.multichat.model.Messaggio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Messaggio> messages = new ArrayList<>();
    private static final int TYPE_INVIO_MESSAGGIO = 1;
    private static final int TYPE_RICEZIONE_MESSAGGIO = 2;

    public MessageAdapter(ArrayList<Messaggio> messages) {
        this.messages = messages;
    }

    public int getItemViewType(int position){
        Messaggio messaggio = messages.get(position);
        if(messaggio.isInviato()) {
            return TYPE_INVIO_MESSAGGIO;
        } else {
            return TYPE_RICEZIONE_MESSAGGIO;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_INVIO_MESSAGGIO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_invio_msg, parent, false);
            return new InvioMessaggioViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_rcv_msg, parent, false);
            return new RicezioneMessaggioViewHolder(view);
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Messaggio messaggio = messages.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(messaggio.getOra_invio());
        if(holder instanceof InvioMessaggioViewHolder) {
            ((InvioMessaggioViewHolder) holder).messageTextView.setText(messaggio.getTesto());
            ((InvioMessaggioViewHolder) holder).orarioTextView.setText(formattedDate);
        } else if(holder instanceof RicezioneMessaggioViewHolder) {
            ((RicezioneMessaggioViewHolder) holder).messageTextView.setText(messaggio.getTesto());
            ((RicezioneMessaggioViewHolder) holder).orarioTextView.setText(formattedDate);
            ((RicezioneMessaggioViewHolder) holder).mittenteTextView.setText((messaggio.getMittente()));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class InvioMessaggioViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView orarioTextView;

        public InvioMessaggioViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
            orarioTextView = itemView.findViewById(R.id.text_orario);
        }
    }

    public static class RicezioneMessaggioViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView orarioTextView;
        public TextView mittenteTextView;

        public RicezioneMessaggioViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_message);
            orarioTextView = itemView.findViewById(R.id.text_orario);
            mittenteTextView = itemView.findViewById(R.id.text_mittente);
        }
    }
}
