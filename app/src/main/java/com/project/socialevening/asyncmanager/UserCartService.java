package com.project.socialevening.asyncmanager;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.models.UserCart;
import com.project.socialevening.utility.AppConstants;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 14/11/15.
 */
public class UserCartService extends GenericService {
    @Override
    public Object getData(Object... params) throws JSONException, SQLException, NullPointerException, RestException, ClassCastException, ParseException {
        ParseQuery<ParseObject> cart = ParseQuery.getQuery(AppConstants.PARAMS.CART);
        cart.whereEqualTo("user", ParseUser.getCurrentUser());
        List<ParseObject> cartItems = cart.find();
        List<UserCart> cartList = new ArrayList<>();
        if (cartItems != null && cartItems.size() > 0) {
            for (ParseObject c : cartItems) {
                ParseQuery<ParseObject> p = ParseQuery.getQuery(c.getString(AppConstants.PARAMS.CLASS_NAME));
                p.whereEqualTo("objectId", c.getString(AppConstants.PARAMS.P_ID));
                ParseObject pr = p.getFirst();
                UserCart uc = new UserCart();
                uc.setProduct(pr);
                uc.setCart(c);
                uc.setQty(c.getLong(AppConstants.PARAMS.P_QTY));
                cartList.add(uc);
            }
        }
        return cartList;
    }
}
