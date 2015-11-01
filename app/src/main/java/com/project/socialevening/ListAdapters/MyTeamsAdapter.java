package com.project.socialevening.ListAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.project.socialevening.holders.BaseRecycleHolder;
import com.project.socialevening.holders.MyTeamHolder;

import java.util.List;

/**
 * Created by nitin on 31/10/15.
 */
public class MyTeamsAdapter extends RecyclerView.Adapter<BaseRecycleHolder> {
    List<ParseObject> myTeams;
    Context ctx;
    int resId;

    public MyTeamsAdapter(Context ctx, List<ParseObject> list, int resourceId) {
        this.myTeams = list;
        this.ctx = ctx;
        this.resId = resourceId;
    }

    @Override
    public BaseRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(resId, null);
        MyTeamHolder holder = new MyTeamHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecycleHolder holder, int position) {
        holder.onBindViewHolder(myTeams.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return myTeams.size();
    }
}
