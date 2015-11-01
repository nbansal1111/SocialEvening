package com.project.socialevening.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nitin on 31/10/15.
 */
public class BaseRecycleHolder extends RecyclerView.ViewHolder {
    Context ctx;

    public BaseRecycleHolder(View itemView) {
        super(itemView);
        this.ctx = itemView.getContext();
        itemView.setOnClickListener(cardClickListener);
    }

    protected TextView findTV(int id) {
        return (TextView) findView(id);
    }

    protected ImageView findImage(int id) {
        return (ImageView) findView(id);
    }

    protected View findView(int id) {
        return itemView.findViewById(id);
    }

    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onCardClick(v);
        }
    };

    public void onBindViewHolder(Object object, int pos) {

    }

    protected void onCardClick(View v) {

    }
}
