package com.bikshanov.qrcodescanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeHolder> {

    private List<Code> codes = new ArrayList<>();

    @NonNull
    @Override
    public CodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new CodeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeHolder holder, int position) {
        Code currentCode = codes.get(position);
        holder.mCodeTextView.setText(currentCode.getCode());
        holder.mFormatTextView.setText(currentCode.getFormat());
//        holder.mDateTextView.setText(currentCode.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
        notifyDataSetChanged();
    }

    class CodeHolder extends RecyclerView.ViewHolder {
        private TextView mCodeTextView;
        private TextView mFormatTextView;
//        private TextView mDateTextView;

        public CodeHolder(@NonNull View itemView) {
            super(itemView);
            mCodeTextView = itemView.findViewById(R.id.code_text_view);
            mFormatTextView = itemView.findViewById(R.id.format_text_view);
//            mDateTextView = itemView.findViewById(R.id.date_text_view);
        }
    }
}
