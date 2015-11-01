package com.project.socialevening.asyncmanager;


import com.parse.ParseException;
import com.project.socialevening.exceptionhandler.RestException;

import org.json.JSONException;

import java.sql.SQLException;

public interface Service {

    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException;


}
