package com.project.socialevening.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.receivers.RefereshReceiver;
import com.project.socialevening.utility.AppConstants;

/**
 * Created by nitin on 26/01/16.
 */
public class BidDialog extends DialogFragment {
    ParseObject auction;

    public static BidDialog getInstance(ParseObject auction) {
        BidDialog f = new BidDialog();
        f.auction = auction;
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bid, null);
        setText(v, R.id.tv_auction_name, auction.getString(AppConstants.PARAMS.AUCTION_NAME));
        setText(v, R.id.tv_auction_desc, auction.getString(AppConstants.PARAMS.AUCTION_DESC));

        final double minBid = auction.getDouble(AppConstants.PARAMS.AUCTION_PRICE);
        String minBidPrice = "Minimum Bid Price : $ " + minBid;

        setText(v, R.id.tv_auction_time, minBidPrice);
        builder.setView(v);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText et = (EditText) v.findViewById(R.id.et_bid_price);
                if (et.getText().length() > 0) {
                    double bidPrice = Double.parseDouble(et.getText().toString());
                    if (bidPrice > minBid) {
                        createBid(auction, bidPrice);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.min_bid_amount), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private void createBid(ParseObject auction, double bidPrice) {
        ParseObject bid = new ParseObject(AppConstants.PARAMS.BID);
        bid.put(AppConstants.PARAMS.AUCTION, auction);
        bid.put(AppConstants.PARAMS.BID_PRICE, bidPrice);
        bid.put(AppConstants.PARAMS.USER, ParseUser.getCurrentUser());
        auction.increment(AppConstants.PARAMS.AUCTION_BID_COUNT, 1);

        bid.saveEventually();
        sendRefreshBroadcast(getActivity());

    }

    public static void sendRefreshBroadcast(Context ctx){
        Intent i = new Intent();
        i.setAction(RefereshReceiver.ACTION_REFRESH);
        ctx.sendBroadcast(i);
    }


    private void setText(View v, int id, String text) {
        TextView tv = (TextView) v.findViewById(id);
        tv.setText(text);
    }
}
