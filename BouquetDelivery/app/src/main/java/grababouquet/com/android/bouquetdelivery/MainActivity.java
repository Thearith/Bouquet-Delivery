package grababouquet.com.android.bouquetdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grababouquet.com.android.http.HttpManager;
import grababouquet.com.android.http.RequestPackage;
import grababouquet.com.android.model.User;
import grababouquet.com.android.parser.UserParser;


public class MainActivity extends ActionBarActivity implements
        View.OnClickListener{

    private static final int ID_DIGITS = 9;

    private static final String URI_CHECKORDER = "http://www.grababouquet.com/delivery/check.json";
    private static final String CHECKORDER_PARAM_ID = "order_number";

    private EditText deliveryIDEditText;
    private Button startDeliveryButton;

    String deliveryId;

    List<MyTask> tasks;
    User user;

    private AlertDialog.Builder builder;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();

        deliveryIDEditText = (EditText) findViewById(R.id.deliveryIDEditText);
        startDeliveryButton = (Button) findViewById(R.id.startDeliveryButton);
        startDeliveryButton.setOnClickListener(this);
        deliveryIDEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == ID_DIGITS) {
                    startDeliveryButton.setEnabled(true);
                } else {
                    startDeliveryButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onClick(View v) {
        if(v.getId() == R.id.startDeliveryButton) {
            if (isOnline()) {
                deliveryId = deliveryIDEditText.getText().toString();
                requestUserFeed(deliveryId);
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestUserFeed (String deliveryID) {
        String uri = URI_CHECKORDER;
        RequestPackage p = new RequestPackage();
        p.setMethod("GET");
        p.setUri(uri);
        p.setParam(CHECKORDER_PARAM_ID, deliveryID);

        MyTask task = new MyTask();
        task.execute(p);
    }

    private void updateDisplay(User userInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        user = userInfo;
        if(user != null) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.confirm_dialog, null);

            ((TextView) view.findViewById(R.id.deliveryIdTextView)).setText(deliveryId);
            ((TextView) view.findViewById(R.id.receiverNameTextView)).setText(user.getName());
            ((TextView) view.findViewById(R.id.phoneTextView)).setText(user.getPhoneNumber());
            ((TextView) view.findViewById(R.id.address1TextView)).setText(user.getAddress());
            ((TextView) view.findViewById(R.id.zipCodeTextView)).setText(user.getZipCode());

            builder.setView(view);
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    user = null;
                }
            });

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    submitDeliveryId();
                    finish();
                }
            });
        } else {
            builder.setTitle(getResources().getString(R.string.delivery_id_not_found_title));
            builder.setMessage(getResources().getString(R.string.delivery_id_not_found));
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    user = null;
                }
            });
        }
        builder.create().show();
    }

    private void submitDeliveryId() {
        Intent intent = new Intent(MainActivity.this, GeolocationActivity.class);
        intent.putExtra(GeolocationActivity.TAG_ID, deliveryId);
        intent.putExtra(GeolocationActivity.TAG_NAME, user.getName());
        intent.putExtra(GeolocationActivity.TAG_PHONE, user.getPhoneNumber());
        intent.putExtra(GeolocationActivity.TAG_ZIPCODE, user.getZipCode());
        intent.putExtra(GeolocationActivity.TAG_ADDRESS, user.getAddress());
        startActivity(intent);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class MyTask extends AsyncTask<RequestPackage, String, User> {

        @Override
        protected void onPreExecute() {
            tasks.add(this);
        }

        @Override
        protected User doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            Log.d("MainActivity class", content);
            if(content.equals("{}"))
                return null;
            else {
                User user = UserParser.parseUserFeed(content);
                return user;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            tasks.remove(this);
            updateDisplay(user);
        }
    }
}
