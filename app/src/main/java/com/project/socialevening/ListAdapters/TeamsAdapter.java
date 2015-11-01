package com.project.socialevening.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.project.socialevening.holders.BaseRecycleHolder;
import com.project.socialevening.holders.TeamHolder;

import java.util.List;

/**
 * Created by nitin on 01/11/15.
 */
public class TeamsAdapter extends MyTeamsAdapter {
    public TeamsAdapter(Context ctx, List<ParseObject> list, int resourceId) {
        super(ctx, list, resourceId);
    }

    @Override
    public BaseRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(resId, null);
        TeamHolder holder = new TeamHolder(v);
        return holder;
    }
}
