package com.project.socialevening.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.socialevening.activity.CreateTeamActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Util {

    public static float randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static final String JAVA_DATE_PATTERN = "E MMM dd HH:mm:ss Z yyyy";
    public static final String REQUIRE_DATE_PATTERN = "MMM dd, HH:mm";
    public static final String PARSE_CREATED_AT_DATE_PATTERN = "MMM dd, yyyy, HH:mm";
    public static final String DISCVER_SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISCVER_UI_ORDER_STATUS_DATE_PATTERN = "HH:mm a,\ndd MMM";
    public static final String DISCVER_DELIVERY_TIME_DATE_PATTERN = "HH:mm a, dd MMM";

    public static String getParseRangeQuery(String startDate, String endDate) {
        return String
                .format("where={'createdAt':{'$gte':{'__type':'DateFragment','iso':'%s'},'$lte':{'__type':'DateFragment','iso':'%s'}}}",
                        startDate, endDate);
    }

    public static Bitmap getBitmapFromUri(Context ctx, Uri imageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static byte[] getBytesFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public interface DialogListener {
        public void onOKPressed(DialogInterface dialog, int which);

        public void onCancelPressed(DialogInterface dialog, int which);
    }

    public static int getQuantityFromEditText(EditText etQty) {
        int qty = 0;
        try {
            qty = Integer.parseInt(etQty.getText().toString());

        } catch (Exception e) {

        }
        return qty;
    }

    public static AlertDialog createAlertDialog(Context context,
                                                String message, String title, boolean isCancelable, String okText,
                                                String cancelText, final DialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(isCancelable);
        builder.setPositiveButton(okText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        listener.onOKPressed(dialog, which);
                    }
                });
        builder.setNegativeButton(cancelText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        listener.onCancelPressed(dialog, which);
                    }
                });

        return builder.create();
    }

    public static boolean isConnectingToInternet(Context ctx) {

        boolean NetConnected = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Logger.info("tag", "couldn't get connectivity manager");
                NetConnected = false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            NetConnected = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Logger.error("Connectivity Exception",
                    "Exception AT isInternetConnection");
            NetConnected = false;
        }
        return NetConnected;

    }

    public static String getStringFromInputStream(InputStream is) {
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader buReader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 50000);

            String line;

            while ((line = buReader.readLine()) != null) {
                response.append(line);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response.toString();

    }

    public static void startItemActivity(Context ctx, Class activityClass) {
        Intent i = new Intent(ctx, activityClass);
        ctx.startActivity(i);
    }

    public static String getStringFromHTMLContent(String s) {
        String str = s.replaceAll("<br />", "<br /><br />").replaceAll(
                "&nbsp;", "<br /><br />");
        Log.e("String After", str);
        return str;
    }

    public static String convertDateFormat(String currentDate,
                                           String reqDateFormat) {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(
                JAVA_DATE_PATTERN);
        SimpleDateFormat format = new SimpleDateFormat(reqDateFormat);
        try {
            Date d = currentDateFormat.parse(currentDate);
            return format.format(d);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDateFormat(String currentDate,
                                           String currentDateFormatString, String reqDateFormat) {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(
                currentDateFormatString);
        SimpleDateFormat format = new SimpleDateFormat(reqDateFormat);
        try {
            Date d = currentDateFormat.parse(currentDate);
            return format.format(d);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static Object getColumnObject(Cursor c, String columnName) {
        int colIndex = c.getColumnIndex(columnName);
        switch (c.getType(colIndex)) {

            case Cursor.FIELD_TYPE_BLOB:
                return c.getBlob(colIndex);
            case Cursor.FIELD_TYPE_STRING:
                return c.getString(colIndex);
            case Cursor.FIELD_TYPE_FLOAT:
                return c.getFloat(colIndex);
            case Cursor.FIELD_TYPE_INTEGER:
                return c.getInt(colIndex);
            case Cursor.FIELD_TYPE_NULL:
                return null;
        }
        return null;

    }

    public static String getCombinedString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static String DBL_FMT = "%.2f";

    public static Date convertStringToDate(String dateString, String dateFormat)
            throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.parse(dateString);
    }

    public static Date convertDateFormat(Date date, String reqDateFormat,
                                         String currentDateFormat) throws Exception {
        String formattedDateString = convertDateFormat(date.toString(),
                currentDateFormat, reqDateFormat);
        SimpleDateFormat format = new SimpleDateFormat(reqDateFormat);
        return format.parse(formattedDateString);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static SpannableString getSppnnableString(String wholeText,
                                                     String spannedText, int colorId) {
        SpannableString spanString = new SpannableString(wholeText);
        try {
            int index = wholeText.indexOf(spannedText);
            if (index == -1) {
                return spanString;
            }
            spanString.setSpan(new ForegroundColorSpan(colorId), index, index
                    + spannedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
        return spanString;
    }

    public static String getServiceURL(String pageUrl) {
        return Util.getCombinedString(AppConstants.API_PREFIX2, pageUrl);
    }

    public static String getAndroidId(Context ctx) {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Secure.getString(ctx.getContentResolver(),
                    Secure.ANDROID_ID);
        return identifier;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void loadImage(Context ctx, String image, ImageView imgView, int placeHolder) {
        loadImage(Picasso.with(ctx), image, imgView, placeHolder);
    }

    public static void loadImage(Picasso picasso, String image, ImageView imgView, int placeHolder) {
        if (!TextUtils.isEmpty(image)) {
            if (0 == placeHolder) {
                picasso.load(image).into(imgView);
            } else {
                picasso.load(image).placeholder(placeHolder).into(imgView);
            }
        }
    }

    public static void loadImage(Picasso picasso, String image, ImageView imgView, final TextView textView) {
        if (!TextUtils.isEmpty(image)) {
            picasso.load(image).into(imgView, new Callback() {
                @Override
                public void onSuccess() {
                    if (textView != null)
                        textView.setText("");
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public static void loadImage(Activity ctx, String image, int imgView, int placeHolder) {
        loadImage(Picasso.with(ctx), image, (ImageView) ctx.findViewById(imgView), placeHolder);
    }


    public static void setBackground(View view, String color) {
        if (TextUtils.isEmpty(color)) {
            setBackground(view, Color.RED);
        } else {
            setBackground(view, Color.parseColor(color));
        }
    }

    public static void setBackground(View view, int color) {
//        Log.d(TAG, "H:" + view.getHeight() + ", W:" + view.getWidth());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
//        view.setBackground(shape);
    }

    public static String getFirstCharacter(String title) {
        if (TextUtils.isEmpty(title)) return "";
        return title.trim().charAt(0) + "";
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SocialEvening");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".png");

        return mediaFile;
    }

    public static void performCrop(Activity ctx, Uri picUri, int reqCode) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/png");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate <span id="IL_AD11" class="IL_AD">output</span> X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // <span id="IL_AD5" class="IL_AD">retrieve data</span> on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            ctx.startActivityForResult(cropIntent, reqCode);
        }
        // respond to users whose devices do <span id="IL_AD4" class="IL_AD">not support</span> the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(ctx, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

//            try {
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            }

            bundle.putString("idFacebook", id);
            String firstName = "", lastName = "";
            if (object.has("first_name")) {
                Preferences.saveData("first_name", object.getString("first_name"));
                bundle.putString("name", object.getString("first_name"));
                firstName = object.getString("first_name");
            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
                Preferences.saveData("last_name", object.getString("last_name"));
                lastName = object.getString("last_name");
            }

            Preferences.saveData("name", firstName + " " + lastName);

            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                Preferences.saveData("email", object.getString("email"));
            }
            if (object.has("gender")) {
                bundle.putString("gender", object.getString("gender"));
                String gender = object.getString("gender");
                if ("male".equalsIgnoreCase(gender)) {
                    Preferences.saveData("gender", "M");
                } else if ("female".equalsIgnoreCase("gender")) {
                    Preferences.saveData("gender", "F");
                }
            }
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (Exception e) {

        }
        return null;
    }

    public static void saveAppLink() {
        if (!Preferences.getData(AppConstants.PREF_KEYS.IS_APP_LINK_SAVED, false)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("AppLink");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) return;
                    if (objects != null && objects.size() > 0) {
                        String appLink = objects.get(0).getString(AppConstants.PREF_KEYS.APP_LINK);
                        if (!TextUtils.isEmpty(appLink)) {
                            Preferences.saveData(AppConstants.PREF_KEYS.APP_LINK, appLink);
                            Preferences.saveData(AppConstants.PREF_KEYS.IS_APP_LINK_SAVED, true);
                        }
                    }
                }

            });
        }
    }


}
