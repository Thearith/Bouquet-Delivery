package grababouquet.com.android.bouquetdelivery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import grababouquet.com.android.gpstracker.GPSTracker;

/**
 * Created by kaka on 1/28/2015.
 */
public class GPSPostService extends IntentService {

    private static final String REPORTING_URL = "https://dweet.io/dweet/for/";
    private static final int REPORT_PERIOD = 15000;
    private RequestQueue mRequestQueue;
    private GPSTracker mGpsTracker;
    private static final Timer timer = new Timer();
    private final TimerTask pushTask = new TimerTask() {
        @Override
        public void run() {
            if(mGpsTracker.canGetLocation()){
                pushLocation(mGpsTracker.getLatitude(), mGpsTracker.getLongitude());
            } else {
                mGpsTracker.getLocation();
            }
        }
    };
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPSPostService(String name) {
        super(name);
    }
    public GPSPostService(){
        super("GPSPostService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent){
        Log.d("GPS Service Intent", "start");
        mRequestQueue = Volley.newRequestQueue(this);
        mGpsTracker = new GPSTracker(this);
        timer.scheduleAtFixedRate(pushTask, 0, REPORT_PERIOD);
    }

    private void pushLocation(double lng, double lat){
        JsonObjectRequest request = new JsonObjectRequest(
            REPORTING_URL + "ORDERSTRING" + "?lat=" + lat + "&lng=" + lng,
            null,
            new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Log.i(GPSPostService.class.toString(), "RESPONDING!!!!");
                    Log.i(GPSPostService.class.toString(), response.toString());
                }
            },
            null
        );
        Log.d(GPSPostService.class.toString(), "ADDING REQUEST");
        mRequestQueue.add(request);
    }
}

