package grababouquet.com.android.bouquetdelivery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class GeolocationActivity extends FragmentActivity
        implements View.OnClickListener, OnMapReadyCallback {

    public final static String TAG_ID = "grababouquet.com.android.bouquetdelivery.geolocation.id";
    public final static String TAG_ZIPCODE = "grababouquet.com.android.bouquetdelivery.geolocation.zipcode";
    public final static String TAG_NAME = "grababouquet.com.android.bouquetdelivery.geolocation.name";
    public final static String TAG_PHONE = "grababouquet.com.android.bouquetdelivery.geolocation.phone";
    public final static String TAG_ADDRESS = "grababouquet.com.android.bouquetdelivery.geolocation.address";

    private ImageView markerImageView;
    private Button reachDestButton;
    private String zipcode;

    private static final String LOGTAG = "Maps";

    MapFragment mMapFragment;
    GoogleMap mMap;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        Intent intent = getIntent();

        TextView deliveryIdTextView = (TextView) findViewById(R.id.deliveryIdTextView);
        deliveryIdTextView.setText("ID: " + intent.getStringExtra(TAG_ID));

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(intent.getStringExtra(TAG_NAME));

        TextView phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        phoneTextView.setText(intent.getStringExtra(TAG_PHONE));
        phoneTextView.setAutoLinkMask(Linkify.PHONE_NUMBERS);

        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        addressTextView.setText(intent.getStringExtra(TAG_ADDRESS));

        zipcode = intent.getStringExtra(TAG_ZIPCODE);

        markerImageView = (ImageView) findViewById(R.id.markerImageView);
        reachDestButton = (Button) findViewById(R.id.arriveDestButton);
        reachDestButton.setOnClickListener(this);

        if (!initMap())
            Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
    }

    private LatLng getLatLng(String zipcode) {
        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                Log.d("Geolocation", message);
                return new LatLng(address.getLatitude(), address.getLongitude());
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // handle exception
        }

        return null;
    }

    private boolean initMap() {
        if (mMap == null) {
            mMapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mMapFragment.getMapAsync(this);
            mMap = mMapFragment.getMap();
        }
        return (mMap != null);
    }

    public void onClick(View v) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geolocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng userLocation = getLatLng(zipcode);
        try {
            mMap.addMarker(new MarkerOptions().position(userLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }catch(IllegalArgumentException e) {
            Log.e("GeolocationActivity", "zip code is not valid");
        }
    }
}
