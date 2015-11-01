package com.project.socialevening.holders;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.utility.Util;

/**
 * Created by nitin on 01/11/15.
 */
public class MemberHolder extends BaseRecycleHolder {
    private ParseUser user;

    private ImageView img;
    private TextView textView, firstCharacter;
    private FrameLayout frameLayout;

    public MemberHolder(View itemView) {
        super(itemView);
        img = findImage(R.id.avatar);
        textView = findTV(R.id.tv_category);
        firstCharacter = findTV(R.id.tv_first_character);
        frameLayout = (FrameLayout) findView(R.id.frame_bg);
    }

    @Override
    public void onBindViewHolder(Object object, int pos) {
        if (object instanceof ParseUser) {
            user = (ParseUser) object;
            textView.setText(user.getUsername());
            Util.setBackground(frameLayout, ctx.getResources().getColor(R.color.color_primary_dark));
            firstCharacter.setText(Util.getFirstCharacter(user.getUsername()));
        }
    }
}
