package com.project.socialevening.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.fragments.dialogs.DateDialogFragment;
import com.project.socialevening.fragments.dialogs.TimePickerDialog;
import com.project.socialevening.models.Auction;
import com.project.socialevening.receivers.RefereshReceiver;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.DateTimeUtil;
import com.project.socialevening.utility.Preferences;

import java.util.Calendar;
import java.util.Date;

public class BidActivity extends BaseActivity implements TimePickerDialog.TimeChangeListener, DateDialogFragment.DateSetListener {

    private int hour, min;
    private Date expiryDate;
    private String timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        initToolBar(R.color.color_discvr_done, "Create your auction");
        setOnClickListener(R.id.rl_date_view, R.id.rl_time_view, R.id.btn_proceed);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_date_view:
                DateDialogFragment dateDialogFragment = new DateDialogFragment();
                dateDialogFragment.setListener(this);
                dateDialogFragment.setMinDate(new Date());
                dateDialogFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.rl_time_view:
                TimePickerDialog f = new TimePickerDialog();
                f.setTimeChangeListener(this);
                f.show(getSupportFragmentManager(), "TimePicker");
                break;
            case R.id.btn_proceed:
                String auctionName = getEditText(R.id.et_bidName);
                String auctioDesc = getEditText(R.id.et_auction_desc);
                String auctionMinPrice = getEditText(R.id.et_auction_min_price);

                if (TextUtils.isEmpty(auctionName)) {
                    showToast(getString(R.string.empty_auction_name));
                    return;
                }
                if (TextUtils.isEmpty(auctioDesc)) {
                    showToast(getString(R.string.empty_auction_desc));
                    return;
                }
                if (TextUtils.isEmpty(auctionMinPrice)) {
                    showToast(getString(R.string.empty_auction_price));
                    return;
                }

                if (expiryDate == null) {
                    showToast(getString(R.string.empty_auction_time));
                    return;
                }

                if (TextUtils.isEmpty(timeString)) {
                    showToast(getString(R.string.invalid_auction_time));
                    return;
                }

                long auctionTime = getAuctionTime();
                double auctionPrice = Double.parseDouble(auctionMinPrice);

                ParseObject auction = new ParseObject(AppConstants.PARAMS.AUCTIONS);
                auction.put(AppConstants.PARAMS.AUCTION_NAME, auctionName);
                auction.put(AppConstants.PARAMS.AUCTION_DESC, auctioDesc);
                auction.put(AppConstants.PARAMS.AUCTION_PRICE, auctionPrice);
                auction.put(AppConstants.PARAMS.AUCTION_TIME, auctionTime);
                auction.put(AppConstants.PARAMS.USER, ParseUser.getCurrentUser());
                executeTask(AppConstants.TASK_CODES.SAVE_PARSE_OBJECT, auction);
                break;
        }
    }
    public static void sendRefreshBroadcast(Context ctx){
        Intent i = new Intent();
        i.setAction(RefereshReceiver.ACTION_REFRESH);
        ctx.sendBroadcast(i);
    }
    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.SAVE_PARSE_OBJECT:
                sendRefreshBroadcast(this);
                showToast(getString(R.string.success_auction_creation));
                finish();

                break;
        }
    }

    private long getAuctionTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(expiryDate);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }


    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int min) {
        this.hour = hour;
        this.min = min;
        timeString = DateTimeUtil.getTimeString(DateTimeUtil.getTime(hour, min));
        setText(timeString, R.id.tv_time);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        expiryDate = DateTimeUtil.getDate(year, month, day);
        Date d = new Date();
        if (d.after(expiryDate)) {
            showToast(getString(R.string.select_valid_bid_date));
            expiryDate = null;
            return;
        }
        setText(DateTimeUtil.getDateString(expiryDate), R.id.tv_date);
    }
}
