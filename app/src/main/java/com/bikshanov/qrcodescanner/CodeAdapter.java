package com.bikshanov.qrcodescanner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CodeAdapter extends ListAdapter<Code, CodeAdapter.CodeHolder> {

    public CodeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Code> DIFF_CALLBACK = new DiffUtil.ItemCallback<Code>() {
        @Override
        public boolean areItemsTheSame(@NonNull Code oldItem, @NonNull Code newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Code oldItem, @NonNull Code newItem) {
            return oldItem.getCode().equals(newItem.getCode()) &&
                    oldItem.getFormat().equals(newItem.getFormat()) &&
                    oldItem.getType().equals(newItem.getType()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public CodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new CodeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeHolder holder, int position) {
        Code currentCode = getItem(position);
//        holder.mCodeTextView.setText(currentCode.getCode());
        holder.mCodeTextView.setText(Utils.getParsedResult(currentCode.getCode(),
                Utils.getBarcodeFormat(currentCode.getFormat())).getDisplayResult());
        holder.mFormatTextView.setText(currentCode.getFormat());

        switch (currentCode.getType()) {
            case "ADDRESSBOOK":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_addressbook);
                break;
            case "EMAIL_ADDRESS":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_email);
                break;
            case "PRODUCT":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_barcode);
                break;
            case "URI":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_url);
                break;
            case "WIFI":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_wifi);
                break;
            case "GEO":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_geo);
                break;
            case "TEL":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_phone);
                break;
            case "SMS":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_sms);
                break;
            case "CALENDAR":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_calendar);
                break;
            case "ISBN":
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_book);
                break;
            default:
                holder.mCodeTypeImageView.setImageResource(R.drawable.ic_text);
                break;
        }
//        holder.mDateTextView.setText(currentCode.getDate().toString());
    }

    public Code getCodeAt(int position) {
        return getItem(position);
    }

    class CodeHolder extends RecyclerView.ViewHolder {
        private TextView mCodeTextView;
        private TextView mFormatTextView;
        private ImageView mCodeTypeImageView;
//        private TextView mDateTextView;

        public CodeHolder(@NonNull View itemView) {
            super(itemView);
            mCodeTextView = itemView.findViewById(R.id.code_text_view);
            mFormatTextView = itemView.findViewById(R.id.format_text_view);
            mCodeTypeImageView = itemView.findViewById(R.id.code_type_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Code code = getItem(position);

                    Intent intent = new Intent(v.getContext(), ScanResultActivity.class);
                    intent.putExtra("ScanResult", code.getCode());
//                    intent.putExtra("ImagePath", imagePath);
                    intent.putExtra("Format", code.getFormat());
                    intent.putExtra("Type", code.getType());
                    intent.putExtra("ID", code.getId());
                    v.getContext().startActivity(intent);
                }
            });
//            mDateTextView = itemView.findViewById(R.id.date_text_view);
        }
    }
}
