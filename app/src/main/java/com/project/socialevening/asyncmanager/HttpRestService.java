package com.project.socialevening.asyncmanager;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.project.socialevening.Network.ClientURLConnection;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Preferences;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

public class HttpRestService extends GenericService {

    private static final String TAG = "RestService";

    public HttpRestService() {
    }

    ;

    @Override
    public Object getData(Object... params) throws JSONException, SQLException,
            NullPointerException, RestException, ClassCastException {
        if (params != null && params.length > 0) {
            HttpParamObject paramObject = (HttpParamObject) params[0];
            String url = paramObject.getUrl();
            List<NameValuePair> postParameters = paramObject.getPostParams();

            ClientURLConnection conn = new ClientURLConnection(url, "",
                    paramObject.getMethod());
            conn.setContentType(paramObject.getContentType());
            for (NameValuePair pair : postParameters) {
                conn.addkeyValuePairToBeSend(pair.getName(), pair.getValue());
                Logger.info(TAG, "params Key :" + pair.getName() + ", value:" + pair.getValue());
            }

            for (Entry<String, String> entry : paramObject.getHeaders().entrySet()) {
                conn.addRequestHeader(entry.getKey(),
                        entry.getValue());
                Logger.info(TAG, "Header Key :" + entry.getKey() + ", value:" + entry.getValue());
            }
            String authHeader = Preferences.getData(AppConstants.PREF_KEYS.ACCESS_CODE, "");
            if (!TextUtils.isEmpty(authHeader)) {
                authHeader = "bearer " + authHeader;
                conn.addRequestHeader("authorization",
                        authHeader);
            }
            conn.addDataToSend(paramObject.getJson());


            String jsonString = conn.getData().getData();
            Logger.info(TAG, url + "..... json String" + jsonString);
            return parseJson(jsonString, paramObject);
        }
        return null;
    }

    protected Object parseJson(String jsonString, HttpParamObject postObject) {
        if (postObject.getClassType() == null) {
            return jsonString;
        }
        try {
            Class classType = postObject.getClassType();
            Method m = classType.getDeclaredMethod("parseJson", String.class);
            Object o = m.invoke(null, jsonString);

            return o;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Parse with Gson
        return new Gson().fromJson(jsonString, postObject.getClassType());

    }

}
