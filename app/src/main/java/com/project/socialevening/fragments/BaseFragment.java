package com.project.socialevening.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.socialevening.R;
import com.project.socialevening.activity.BaseActivity;
import com.project.socialevening.asyncmanager.Service;
import com.project.socialevening.asyncmanager.ServiceFactory;
import com.project.socialevening.exceptionhandler.ExceptionHandler;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Util;

import org.json.JSONException;

public abstract class BaseFragment extends Fragment implements
        View.OnClickListener,
        TaskFragment.AsyncTaskListener {

    View v;
    int layoutId;
    protected AppCompatActivity activity;
    boolean retainFlag = false;
    public static String LAST_UPDATED = "Last Updated: ";
    public static String LAST_UPDATED_ANALYTICS = "Last Updated Analytics: ";
    protected ProgressDialog dialog;
    protected String TAG = getClass().getSimpleName();
    protected Toolbar toolbar;

    public String getActionTitle() {
        return null;
    }


    public void refreshData() {
    }

    protected void setOnClickListener(int... viewIds) {
        for (int i : viewIds) {
            findView(i).setOnClickListener(this);
        }
    }

    protected void hideVisibility(int... viewIds) {
        for (int i : viewIds) {
            findView(i).setVisibility(View.GONE);
        }
    }

    protected void showVisibility(int... viewIds) {
        for (int i : viewIds) {
            findView(i).setVisibility(View.VISIBLE);
        }
    }

    protected void setOnClickListener(View v, int... viewIds) {
        for (int id : viewIds) {
            v.findViewById(id).setOnClickListener(this);
        }
    }

    public void openSortDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
//        this.setRetainInstance(true);
        retainFlag = true;
        Log.e("onCreate", "savedInstanceState:" + savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        v = inflater.inflate(getViewID(), null);
        Log.e("Retain Flag", retainFlag + "");
        initViews();
        return v;

    }

    public void scrollToBottom(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
                // scrollView.scrollTo(0, scrollView.getBottom());
            }
        });
    }

    protected AsyncTask executeTask(int taskCode, Object... params) {

        if (Util.isConnectingToInternet(getActivity())) {
            AsyncManager task = new AsyncManager(taskCode, this);
            task.execute(params);
            return task;
        } else {
            Logger.info("Base Activity", "Not Connected to internet");
//			Util.createAlertDialog(getActivity(), "Please Connect to Internet",
//					"Not Connected to internet", false, "OK", "Cancel",
//					(Util.DialogListener) BaseActivity.internetDialogListener).show();
            onInternetException();
        }
        return null;
    }

    protected void onInternetException() {
        FrameLayout noInternetLayout = (FrameLayout) findView(R.id.frame_noInternet);
        if (noInternetLayout != null) {
            noInternetLayout.setVisibility(View.VISIBLE);
            findView(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryClicked(v);
                }
            });
        }

    }

    protected void onServerError() {
        FrameLayout errorLayout = (FrameLayout) findView(R.id.frame_noInternet);
        if (errorLayout != null) {
            errorLayout.setVisibility(View.VISIBLE);
            ImageView errorImage = (ImageView) errorLayout.findViewById(R.id.iv_error);
            TextView errorMsg = (TextView) errorLayout.findViewById(R.id.tv_errorMsg);
            TextView errorInfo = (TextView) errorLayout.findViewById(R.id.tv_errorInfo);

//            errorImage.setImageResource(R.drawable.icon_server_error);
            errorMsg.setText(R.string.server_error);
            errorInfo.setText(R.string.msg_server_error);
            findView(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryClicked(v);
                }
            });
        }
    }

    public void onRetryClicked(View view) {
        if (Util.isConnectingToInternet(getActivity())) {
            findView(R.id.frame_noInternet).setVisibility(View.GONE);
        }
    }

    public void scrollToTop(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
                // scrollView.scrollTo(0, scrollView.getBottom());
            }
        });
    }

    protected void initActionBar(String text) {
        ((BaseActivity) getActivity()).initToolBar(getResources().getColor(R.color.color_discvr_done), text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    public void registerClickListeners(int... viewIds) {
        for (int id : viewIds) {
            v.findViewById(id).setOnClickListener(this);
        }
    }

    public void registerClickListeners(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    public View findView(int id) {
        return v.findViewById(id);
    }

    public abstract void initViews();

    public abstract int getViewID();

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    protected void setText(int textViewID, String text) {
        TextView textView = (TextView) findView(textViewID);
        textView.setText(text);
    }

    protected void setText(int textViewID, String text, View row) {
        TextView textView = (TextView) row.findViewById(textViewID);
        textView.setText(text);
    }

    public void hideKeyboard() {
        /*
         * getActivity().getWindow().setSoftInputMode(
		 * WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 */

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public class AsyncManager extends AsyncTask<Object, Object, Object> {

        public static final String TAG = "XebiaAsyncManage";

        private int taskCode;
        private Object[] params;
        private Exception e;
        private long startTime;
        TaskFragment.AsyncTaskListener asyncTaskListener;

        public AsyncManager(int taskCode, TaskFragment.AsyncTaskListener ref) {

            this.taskCode = taskCode;
            asyncTaskListener = ref;
        }

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
            Logger.info(TAG, "On Preexecute AsyncTask");
            if (asyncTaskListener != null) {
                asyncTaskListener.onPreExecute(this.taskCode);
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            Object response = null;
            Service service = ServiceFactory.getInstance(getActivity(),
                    taskCode);
            Logger.info(TAG, "DoinBackGround");
            try {
                this.params = params;
                response = service.getData(params);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof JSONException) {
                    String exceptionMessage = "APIMANAGEREXCEPTION : ";

                    exceptionMessage += ExceptionHandler.getStackString(e,
                            asyncTaskListener.getClass()
                                    .getName());
                    /*
                     * ServiceFactory.getDBInstance(context).putStackTrace(
					 * exceptionMessage);
					 */
                }
                this.e = e;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (getActivity() != null) {

                Logger.error(
                        "servicebenchmark",
                        asyncTaskListener.getClass().getName()
                                + " , time taken in ms: "
                                + (System.currentTimeMillis() - startTime));

                if (e != null) {

                    if (e instanceof RestException) {
                        asyncTaskListener.onBackgroundError((RestException) e,
                                null, this.taskCode, this.params);
                    } else {
                        asyncTaskListener.onBackgroundError(null, e,
                                this.taskCode, this.params);
                    }
                } else {
                    asyncTaskListener.onPostExecute(result, this.taskCode,
                            this.params);
                }
                super.onPostExecute(result);
            }
        }

        public int getCurrentTaskCode() {
            return this.taskCode;
        }

    }

    @Override
    public void onBackgroundError(RestException re, Exception e, int taskCode,
                                  Object... params) {
        hideProgressBar();
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        hideProgressBar();
    }

    @Override
    public void onPreExecute(int taskCode) {
        showProgressBar();
    }

    public void showProgressBar() {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();

        }
    }

    public void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected String getEditText(int editTextId) {
        return ((EditText) findView(editTextId)).getText().toString().trim();
    }

    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void initToolBar(String title) {
        toolbar = (Toolbar) findView(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(getHomeIcon());
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    protected int getHomeIcon() {
        return R.drawable.ic_action_back;
    }


}
