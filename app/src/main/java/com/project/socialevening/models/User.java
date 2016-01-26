package com.project.socialevening.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nitin on 26/01/16.
 */
@Table(name = "User", id = BaseColumns._ID)
public class User extends Model {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    private String userName;
    @Column
    private String password;
    @Column
    private String fullName;

}
