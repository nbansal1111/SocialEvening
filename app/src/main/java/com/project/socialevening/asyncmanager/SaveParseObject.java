package com.project.socialevening.asyncmanager;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.project.socialevening.exceptionhandler.RestException;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by nitin on 30/10/15.
 */
public class SaveParseObject extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException {
        ParseObject obj = (ParseObject) params[0];
        obj.save();
        return obj;
    }
}
