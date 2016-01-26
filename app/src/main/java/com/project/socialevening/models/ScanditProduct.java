package com.project.socialevening.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nitin on 20/12/15.
 */
@Table(name = "product", id = BaseColumns._ID)
public class ScanditProduct extends Model {
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String content;
    @Column
    private String name;
    @Column
    private String barCodeId;
    @Column
    private long qty;

    public String getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
}
