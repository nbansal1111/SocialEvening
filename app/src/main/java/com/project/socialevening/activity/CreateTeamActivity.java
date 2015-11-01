package com.project.socialevening.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nitin on 30/10/15.
 */
public class CreateTeamActivity extends BaseActivity {

    private static final int TAKE_PHOTO_CODE = 0;
    private static final int REQ_CROP_PIC = 1;
    private ImageView imageView;
    private Uri imageUri;
    private EditText teamET;
    private String teamName;
    private Bitmap bMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_team);
        initToolBar(getResources().getColor(R.color.color_discvr_done), getString(R.string.take_a_selfie));
        imageView = (ImageView) findViewById(R.id.iv_selfie);
        teamET = (EditText) findViewById(R.id.et_teamName);
        setOnClickListener(R.id.iv_selfie, R.id.btn_createTeam, R.id.fab);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_selfie:
            case R.id.fab:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Util.getOutputMediaFileUri();

                Log.d(TAG, "Image File URI :" + imageUri);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                break;
            case R.id.btn_createTeam:
                if (null == bMap) {
                    showToast(getString(R.string.error_no_selfie));
                    return;
                }
                teamName = teamET.getText().toString().trim();
                if (TextUtils.isEmpty(teamName)) {
                    showToast(getString(R.string.error_no_team_name));
                    return;
                }
                createTeam(bMap);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case TAKE_PHOTO_CODE:
                Log.d(TAG, "Image URI :" + imageUri);
                Util.performCrop(this, imageUri, REQ_CROP_PIC);
                break;
            case REQ_CROP_PIC:
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                bMap = extras.getParcelable("data");
                if (null != bMap) {
                    imageView.setImageBitmap(bMap);
                    hideVisibility(R.id.fab);
                    getSupportActionBar().setTitle(R.string.write_team_name);
                    showVisibility(R.id.et_teamName, R.id.btn_createTeam);
                }
//                createTeam(thePic);
                break;
        }
    }

    private void createTeam(final String teamName, final Uri imageUri) {

        Bitmap bitmap = Util.getBitmapFromUri(this, imageUri);
// Make a new post
        if (null == bitmap) {
            showToast(getString(R.string.error_bitmap_retrieval));
            return;
        }
        uploadSelfie(bitmap);
    }

    private void createTeam(Bitmap bitmap) {
        uploadSelfie(bitmap);
    }

    private void saveTeamObject(ParseFile file) {
        if (file != null) {
            Log.d(TAG, "File is not null");
            ParseUser user = ParseUser.getCurrentUser();
            ParseObject post = new ParseObject(AppConstants.PARAMS.TEAM);
            post.put(AppConstants.PARAMS.TEAM_NAME, teamName + "");
            post.put(AppConstants.PARAMS.TEAM_IMAGE, file);
            post.put(AppConstants.PARAMS.USER, user);

            List<ParseUser> list = new ArrayList<>();
            list.add(ParseUser.getCurrentUser());
            post.put(AppConstants.PARAMS.MEMBERS, list);

            ParseGeoPoint geoPoint = Preferences.getGeoPoint();
            if (geoPoint != null)
                post.put(AppConstants.PARAMS.LOCATION, geoPoint);

            executeTask(AppConstants.TASK_CODES.SAVE_PARSE_OBJECT, post);
        } else {
            Log.d(TAG, "File is null");
        }
    }

    private void uploadSelfie(Bitmap bmap) {
        byte[] bytes = Util.getBytesFromBitmap(bmap);
        ParseFile file = new ParseFile("selfie.png", bytes);
        executeTask(AppConstants.TASK_CODES.SAVE_PARSE_FILE, file);
    }

    @Override
    public void onPreExecute(int taskCode) {
        super.onPreExecute(taskCode);
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        Log.d(TAG, "On Post Execute");
        switch (taskCode) {
            case AppConstants.TASK_CODES.SAVE_PARSE_OBJECT:
                Log.d(TAG, "Team Created Move to add a team mate");
                ParseObject team = (ParseObject) response;
                Bundle b = new Bundle();
                b.putString(AppConstants.BUNDLE_KEYS.TEAM_ID, team.getObjectId());
                FragmentContainer.startActivity(this, AppConstants.FRAGMENT_TYPE.ADD_TEAM_MATE, b);
                break;
            case AppConstants.TASK_CODES.SAVE_PARSE_FILE:
                ParseFile file = (ParseFile) params[0];
                saveTeamObject(file);
                break;
        }
    }

    @Override
    public void onBackgroundError(RestException re, Exception e, int taskCode, Object... params) {
        super.onBackgroundError(re, e, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.SAVE_PARSE_OBJECT:
                showToast(getString(R.string.error_bitmap_retrieval));
                break;
            case AppConstants.TASK_CODES.SAVE_PARSE_FILE:
                showToast(getString(R.string.error_creating_team));
                break;
        }
    }

    @Override
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        createTeam(teamName, imageUri);
    }
}
