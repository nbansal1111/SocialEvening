package com.project.socialevening.listeners;

import com.parse.ParseObject;

/**
 * Created by nitin on 13/11/15.
 */
public interface CartUpdateListener {
    public void onCartUpdate(ParseObject product, ParseObject cart, long qty, boolean isRemoved);
}
