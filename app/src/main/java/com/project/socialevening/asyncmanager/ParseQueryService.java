package com.project.socialevening.asyncmanager;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.socialevening.exceptionhandler.RestException;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * Created by nitin on 31/10/15.
 */
public class ParseQueryService extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException {
        ParseQuery<ParseObject> query = (ParseQuery<ParseObject>) params[0];
        return query.find();
    }
}
