package com.project.socialevening.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.project.socialevening.R;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginScreen extends BaseActivity {

    private EditText passwordET, emailET, nameET;
    private Button proceed;

    private String gender;
    private ImageView male, female;
    private int genderType;
    private LinearLayout layout;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        layout = (LinearLayout) findViewById(R.id.parentView);

        if (Preferences.getData(Preferences.LOGIN_KEY, false)) {
            startNextActivity(HomeActivity.class);
            finish();
        }

        initToolBar(0, "Social Evening");
        setUpLoginViaFB();

        passwordET = (EditText) findViewById(R.id.et_password);
        emailET = (EditText) findViewById(R.id.et_email);
        nameET = (EditText) findViewById(R.id.et_name);
        proceed = (Button) findViewById(R.id.btn_proceed);
        setOnClickListener(R.id.btn_proceed, R.id.radio_male, R.id.radio_female, R.id.btn_login_fb);
        male = (ImageView) findViewById(R.id.radio_male);
        female = (ImageView) findViewById(R.id.radio_female);


    }

    private void setUpLoginViaFB() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.info(TAG, "onSuccess" + loginResult.getAccessToken().toString());
                loginResult.getAccessToken().toString();
                requestUserFBData(loginResult);
                Preferences.saveData("access_token", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Logger.info(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Logger.info(TAG, "onError");
            }
        });
    }

    public void initToolBar(int colorCode, String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_proceed:
                String password = passwordET.getText().toString();
                String email = emailET.getText().toString().trim();
                String name = nameET.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("Name can not be empty");
                    return;
                }
                if (TextUtils.isEmpty(email) || !Util.isValidEmail(email)) {
                    showToast("Please enter a valid email address");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("Please enter a password");
                    return;
                }
                loginUser(name, email, password, gender);
                break;
            case R.id.radio_female:
                if (genderType == 2) {
                    genderType = 0;
                } else {
                    genderType = 2;
                }
                setGender();
                break;
            case R.id.radio_male:
                if (genderType == 1) {
                    genderType = 0;
                } else {
                    genderType = 1;
                }
                setGender();
                break;
            case R.id.btn_login_fb:
                if (!Util.isConnectingToInternet(this)) {
                    showToast(getString(R.string.internet_error));
                    return;
                }
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
        }
    }

    private void loginUser(final String name, final String email, final String password, final String gender) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.put("gender", gender);

// other fields can be set just like with ParseObject
        showProgressDialog();
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                hideProgressBar();
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Preferences.saveData(Preferences.LOGIN_KEY, true);
                    startNextActivity(HomeActivity.class);
                    finish();
                } else {
                    showSnackBar(e.getMessage());
                    e.printStackTrace();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }

        });
    }

    private void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar
                .make(layout, msg, Snackbar.LENGTH_LONG)
                .setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        snackbar.show();
    }


    private void setGender() {
        switch (genderType) {
            case 1:
                gender = "Male";
                Preferences.saveData("gender", "Male");
                male.setImageResource(R.drawable.male_selected);
                female.setImageResource(R.drawable.female_unselected);
                break;
            case 2:
                gender = "Female";
                Preferences.saveData("gender", "Female");
                male.setImageResource(R.drawable.male_unselected);
                female.setImageResource(R.drawable.female_selected);
                break;
            case 0:
                gender = "";
                Preferences.saveData("gender", "");
                male.setImageResource(R.drawable.male_unselected);
                female.setImageResource(R.drawable.female_unselected);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void requestUserFBData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Logger.info(TAG, response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = Util.getFacebookData(object);
                        final JSONObject jsonObject = new JSONObject();
                        signUpViaFB(loginResult.getAccessToken().getToken());

                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void signUpViaFB(String password) {
        String name = Preferences.getData("name", "");
        String email = Preferences.getData("email", "");
        String gender = Preferences.getData("gender", "");
        loginUser(name, email, gender, password);
    }


}
