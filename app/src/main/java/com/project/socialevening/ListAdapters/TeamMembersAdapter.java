package com.project.socialevening.ListAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import com.project.socialevening.holders.BaseRecycleHolder;
import com.project.socialevening.holders.MemberHolder;

import java.util.List;

/**
 * Created by nitin on 01/11/15.
 */
public class TeamMembersAdapter extends RecyclerView.Adapter<BaseRecycleHolder> {
    List<ParseUser> teamMembers;
    Context ctx;
    int resId;

    public TeamMembersAdapter(Context ctx, List<ParseUser> list, int resourceId) {
        this.teamMembers = list;
        this.ctx = ctx;
        this.resId = resourceId;
    }

    @Override
    public BaseRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(resId, null);
        MemberHolder holder = new MemberHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecycleHolder holder, int position) {
        holder.onBindViewHolder(teamMembers.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }


}
