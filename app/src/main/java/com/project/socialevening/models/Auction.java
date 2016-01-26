package com.project.socialevening.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nitin on 26/01/16.
 */
@Table(name = "Auction", id = BaseColumns._ID)
public class Auction extends Model {
    @Column
    private long auctionTime;
    @Column
    private double auctionPrice;
    @Column
    private String auctionName;
    @Column
    private String auctionDetails;
    @Column
    private long auctionId;
    @Column
    private String userName;
    @Column
    private int bidCount = 0;

    private String myBidPrice;

    public int getBidCount() {
        return bidCount;
    }

    public void setBidCount(int bidCount) {
        this.bidCount = bidCount;
    }

    public String getMyBidPrice() {
        return myBidPrice;
    }

    public void setMyBidPrice(String myBidPrice) {
        this.myBidPrice = myBidPrice;
    }

    public long getAuctionTime() {
        return auctionTime;
    }

    public void setAuctionTime(long auctionTime) {
        this.auctionTime = auctionTime;
    }

    public double getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(double auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getAuctionDetails() {
        return auctionDetails;
    }

    public void setAuctionDetails(String auctionDetails) {
        this.auctionDetails = auctionDetails;
    }

    public long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(long auctionId) {
        this.auctionId = auctionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
