package com.project.socialevening.asyncmanager;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.project.socialevening.exceptionhandler.RestException;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by nitin on 30/10/15.
 */
public class SaveParseFile extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException {
        ParseFile file = (ParseFile) params[0];
        file.save();
        return file;
    }
}
