package com.project.socialevening.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.socialevening.ListAdapters.CustomListAdapter;
import com.project.socialevening.ListAdapters.CustomListAdapterInterface;
import com.project.socialevening.R;
import com.project.socialevening.models.Challenge;
import com.project.socialevening.utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 01/11/15.
 */
public class SelectChallenge extends BaseActivity implements CustomListAdapterInterface, AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Challenge> challenges;
    private CustomListAdapter<Challenge> adapter;
    private Challenge selectedChallenge;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_challenge);
        done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(this);
        done.setVisibility(View.GONE);
        initChallenges();
        initToolBar(getResources().getColor(R.color.color_discvr_done), getString(R.string.select_challenge));
        listView = (ListView) findViewById(R.id.lv_challenge);
        adapter = new CustomListAdapter<>(this, R.layout.view_challenge, challenges, this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

    }

    private void initChallenges() {
        challenges = new ArrayList<>();
        challenges.add(Challenge.getChallenge(AppConstants.CHALLENGES.FACEBOOK));
        challenges.add(Challenge.getChallenge(AppConstants.CHALLENGES.INSTAGRAM));
        challenges.add(Challenge.getChallenge(AppConstants.CHALLENGES.WHATSAPP));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, int resourceID) {
        convertView = LayoutInflater.from(this).inflate(resourceID, null);
        Challenge c = challenges.get(position);
        TextView challengeView = (TextView) convertView.findViewById(R.id.tv_challenge);
        challengeView.setText(c.getChallengeName());
        challengeView.setBackgroundColor(getResources().getColor(c.getChallengeColorId()));
        if (c.isSelected()) {
            convertView.findViewById(R.id.frame_challenge).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.frame_challenge).setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Challenge ch : challenges) {
            ch.setIsSelected(false);
        }
        Challenge c = challenges.get(position);
        c.setIsSelected(!c.isSelected());
        selectedChallenge = c;
        done.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_done:
                Intent i = new Intent();
                i.putExtra(AppConstants.PARAMS.CHALLENGE_NAME, selectedChallenge.getChallengeName());
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }
}
