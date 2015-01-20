package grababouquet.com.android.parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import grababouquet.com.android.model.User;

/**
 * Created by Sivly on 1/20/2015.
 */
public class UserParser {

    private final static String TAG_ORDER = "order";
    private final static String TAG_SHIPADDRESS = "ship_address";
    private final static String TAG_FIRSTNAME = "firstname";
    private final static String TAG_LASTNAME = "lastname";
    private final static String TAG_ADDRESS1 = "address1";
    private final static String TAG_ADDRESS2 = "address2";
    private final static String TAG_ZIPCODE = "zipcode";
    private final static String TAG_PHONE = "phone";

    public static User parseUserFeed(String content) {
        User user = null;

        try {
            JSONObject feedJSON = new JSONObject(content);
            JSONObject orderJSON = feedJSON.getJSONObject(TAG_ORDER);
            JSONObject shipAddrJSON = orderJSON.getJSONObject(TAG_SHIPADDRESS);

            user = new User();

            user.setFirstName(shipAddrJSON.getString(TAG_FIRSTNAME));
            user.setLastName(shipAddrJSON.getString(TAG_LASTNAME));
            user.setAddressLine1(shipAddrJSON.getString(TAG_ADDRESS1));
            user.setAddressLine2(shipAddrJSON.getString(TAG_ADDRESS2));
            user.setZipCode(shipAddrJSON.getString(TAG_ZIPCODE));
            user.setPhoneNumber(shipAddrJSON.getString(TAG_PHONE));

        } catch(JSONException e) {
            Log.e("UserParser", "cannot make json object");
        }

        return user;
    }
}
