package com.project.socialevening.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nitin on 26/01/16.
 */
@Table(name = "BidInfo", id = BaseColumns._ID)
public class BidInfo extends Model {
    @Column
    private String userName;
    @Column
    private String auctionId;
    @Column
    private double bidPrice;
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long bidInfoId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public long getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(long bidInfoId) {
        this.bidInfoId = bidInfoId;
    }
}

