package com.app.translation.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.translation.Model.TranslateWord;
import com.app.translation.R;
import com.app.translation.TranslationList;

import java.util.List;

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.ViewHolder> {

    List<TranslateWord> list;

    Activity activity;


    public TranslationAdapter(List<TranslateWord> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TranslationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View translationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_list_row, parent, false);
        return new ViewHolder(translationView);
    }

    @Override
    public void onBindViewHolder(@NonNull TranslationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.txtViewFromText.setText(list.get(position).getFromText());
        viewHolder.txtViewToText.setText(list.get(position).getToText());
        viewHolder.mTxtType.setText(list.get(position).getFromType()+" → "+list.get(position).getToType());
        viewHolder.imgViewDelete.setOnClickListener(view -> {
            ((TranslationList)activity).deleteConfirmation(list.get(position).getTranslationID(),position);
        });
        if(list.get(position).getSaved()){
            viewHolder.imgViewSave.setImageResource(R.drawable.ic_bookmark);
        }else{
            viewHolder.imgViewSave.setImageResource(R.drawable.ic_bookmark_border);

        }
        viewHolder.imgViewSave.setOnClickListener(view -> {
            Log.e("imgViewSave",list.get(position).getSaved().toString());
            if(list.get(position).getSaved()){
                ((TranslationList)activity).removeTranslationLocal(position);
            }else{
                ((TranslationList)activity).saveTranslationLocal(list.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewFromText, txtViewToText,mTxtType;
        ImageView imgViewSave, imgViewDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewFromText = (TextView) itemView.findViewById(R.id.mTxtFrom);
            txtViewToText = (TextView) itemView.findViewById(R.id.mTxtTo);
            mTxtType = (TextView) itemView.findViewById(R.id.mTxtType);
            imgViewSave = (ImageView) itemView.findViewById(R.id.mImgSave);
            imgViewDelete = (ImageView) itemView.findViewById(R.id.mImgDelete);
        }
    }
}
