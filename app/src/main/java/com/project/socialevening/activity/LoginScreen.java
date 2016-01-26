package com.project.socialevening.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activeandroid.query.Select;
import com.facebook.CallbackManager;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.models.User;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

public class LoginScreen extends BaseActivity {

    private EditText passwordET, userNameEt, nameET;
    private Button proceed;
    private LinearLayout layout;
    private String usernameString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        layout = (LinearLayout) findViewById(R.id.parentView);

        if (Preferences.getData(Preferences.LOGIN_KEY, false)) {
            moveToHome();
            return;
        }
        passwordET = (EditText) findViewById(R.id.et_password);
        userNameEt = (EditText) findViewById(R.id.et_username);
        proceed = (Button) findViewById(R.id.btn_proceed);
        setOnClickListener(R.id.btn_proceed, R.id.btn_signup);


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
                passwordString = passwordET.getText().toString();
                usernameString = userNameEt.getText().toString().trim();

                if (TextUtils.isEmpty(usernameString)) {
                    showToast("Username can not be empty");
                    return;
                }
                if (TextUtils.isEmpty(passwordString)) {
                    showToast("Please enter a password");
                    return;
                }
                loginUser();
                break;
            case R.id.btn_signup:
                startNextActivity(null, RegisterActivity.class);
                finish();
                break;
        }
    }

    private void loginUser() {
        showProgressDialog();
        ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                hideProgressBar();
                if (e == null) {
                    Preferences.saveData(Preferences.LOGIN_KEY, true);
                    moveToHome();
                } else {
                    showSnackBar(e.getMessage());
                }
            }
        });
    }


    private void moveToHome() {
        startNextActivity(new Bundle(), HomeActivity.class);
        finish();
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


}
