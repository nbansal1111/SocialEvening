package com.project.socialevening.asyncmanager;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.models.UserCart;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by nitin on 14/11/15.
 */
public class DeleteCart extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException {
        List<UserCart> cartList = (List<UserCart>) params[0];
        for (UserCart c : cartList) {
            ParseObject cart = c.getCart();
            cart.deleteEventually();
        }
        return true;
    }
}
