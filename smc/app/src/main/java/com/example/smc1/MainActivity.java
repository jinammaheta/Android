package com.example.smc1;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import  java.util.ArrayList;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    String DistanceResult = "";
    private RecyclerView mGridView;
    private ProgressBar mProgressBar;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    String DurationResult = "";
    String ADD;
    List<String> targets;
    public static int count=1;
    String Goal="S.M.C Multi Level Parking,Chowk Bazar, Surat, Gujarat 395003";
    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    ProgressDialog pd;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 100000 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 10000*1000; /* 2 sec */
    private LinkedHashMap<String,Integer> slots=new LinkedHashMap<String ,Integer>();
    private String FEED_URL ="";
//    protected Handler handler;
//    public String storeResponse;
//    JSONArray mtJsonArray;
    GridLayoutManager gridLayoutManager;

    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (RecyclerView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading data....");
        pd.setCancelable(false);
        pd.show();
//        targets= Arrays.asList("S.M.C Multi Level Parking,Chowk Bazar, Surat, Gujarat 395003",
//                "S.M.C Multilevel Parking-Chauta Bazzar, Lal Gate, Shahpore, Surat, Gujarat 395003",
//                "Surat Mahanagar Palika Pay and Park,Near Gandhi Smurti, Timiliyawad Rd, Opp. Gandhi Smruti Bhavan, Nanpura, Godha, Gujarat 395001",
//                "SMC Parking Lot, 4/771, Station Rd, Zampa Bazaar, Begampura, Surat, Gujarat 395003",
//                "SMC Pay & Park, Gaurav Path, Near Sunil Collection, SVNIT Campus, Piplod, Surat, Gujarat 395007",
//                "S.M.C Pay And Parking, Krishna Complex, Hazira - Adajan Rd, Near Gujarat Gas Circle, Premjinagar Society-1, Gita Nagar, Adajan, Surat, Gujarat 395009",
//                "S.M.C Multi Level Parking Kadarshani Nal Road, Kadarshani Nal Rd, Amina Wadi, Kailash Nagar, Nanpura, Surat, Gujarat 395001",
//                "S.M.C Pay & Park, Falsawadi Main Rd, Falsawadi, Begampura, Surat, Gujarat 395003",
//                "SMC Parking Ground, Mughal Sarai, Surat, Gujarat 395003");
        targets= Arrays.asList("S.M.C Multi Level Parking,Chowk Bazar, Surat, Gujarat 395003",
                "S.M.C Multilevel Parking-Chauta Bazzar, Lal Gate, Shahpore, Surat, Gujarat 395003",
                "S.M.C Multi Level Parking Kadarshani Nal Road, Kadarshani Nal Rd, Amina Wadi, Kailash Nagar, Nanpura, Surat, Gujarat 395001",
                "SMC Parking Ground, Mughal Sarai, Surat, Gujarat 395003",
                "S.M.C Pay & Park, Falsawadi Main Rd, Falsawadi, Begampura, Surat, Gujarat 395003",
                "S.M.C Pay And Parking, Krishna Complex, Hazira - Adajan Rd, Near Gujarat Gas Circle, Premjinagar Society-1, Gita Nagar, Adajan, Surat, Gujarat 395009"
               );
        System.out.println("SIXE"+targets.size());
        mGridData = new ArrayList<>();
//        mLatitudeTextView = (TextView) findViewById((R.id.latitude));
//        mLongitudeTextView = (TextView) findViewById((R.id.longitude));
//        TextView Distance_Output = (TextView) findViewById(R.id.latitude);
//        TextView Duration_Output = (TextView) findViewById(R.id.longitude);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

        ;
//        mGridView.setLayoutManager(linearLayoutManager);
//        mGridView.setAdapter(mGridAdapter);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager=new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
            mGridView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager=new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
            mGridView.setLayoutManager(gridLayoutManager);
        }
        mGridAdapter = new RecyclerViewAdapter(this, R.layout.grid_item, mGridData ,mGridView);
        mGridView.setItemAnimator(new DefaultItemAnimator());
        mGridView.setAdapter(mGridAdapter);
//        Toast.makeText(this,"in oncreated",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Toast.makeText(this,"In Connected",Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
//        Toast.makeText(this,"in onconnsuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
//        Toast.makeText(this,"in onconnfailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        pd.setMessage("Loading Data....");
        pd.setCancelable(false);
        pd.show();
//        Toast.makeText(this,"in start",Toast.LENGTH_SHORT).show();
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    public Activity getActivity(){
//        Toast.makeText(this,"in getactivity",Toast.LENGTH_SHORT).show();
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }
    @Override
    protected void onStop() {
//        Toast.makeText(this,"in onstop",Toast.LENGTH_SHORT).show();
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
//        Toast.makeText(this,"in Start Location",Toast.LENGTH_SHORT).show();
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        EditText Start_Location = (EditText) findViewById(R.id.StartLocation);
//        EditText Goal_Location = (EditText) findViewById(R.id.GoalLocation);
//        Reset();
//        Toast.makeText(this,"IN On location",Toast.LENGTH_SHORT).show();

        ADD=location.getLatitude()+" "+location.getLongitude();
        System.out.println("gg "+ADD);

//        Toast.makeText(this,ADD,Toast.LENGTH_SHORT).show();
//            new AsyncTaskParseJson().execute();
//        for(int i=0;i<targets.size();i++) {
//            String FormattedStartLocation = ADD.replaceAll(" ", "+");
//            String FormattedGoalLocation = targets.get(i).replaceAll(" ", "+");

//            FEED_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=km&origins=" + FormattedStartLocation + "&destinations=" + FormattedGoalLocation
//                    + "&key=AIzaSyA609lXN9_qcwJiHpo02CTuB0oXGxDDumo";
        new AsyncHttpData().execute();
        new AsyncHttpTask().execute();


//        mLatitudeTextView.setText("Distance: " + DistanceResult);
//        mLongitudeTextView.setText("Duration: " + DurationResult);
        System.out.println(location.getLatitude()+" "+location.getLongitude());

//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

            String FormattedStartLocation = ADD.replaceAll(" ", "+");
//            String FormattedGoalLocation = targets.get(i).replaceAll(" ", "+");

        @Override
        protected void onPreExecute() {
            System.out.println("PRE");
            SharedPreferences sd = getSharedPreferences("session", 0);
            SharedPreferences.Editor ed = sd.edit();
            ed.putString("src",FormattedStartLocation);
            ed.commit();
        }

        @Override
        protected Integer doInBackground(String... params) {

            mGridData.clear();
//            mGridAdapter.notifyItemRangeRemoved(0,size);
            Integer result = 1;
            try {
                // Create Apache HttpClient

                for(int i=0;i<targets.size();i++) {
                    FEED_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=km&origins=" + FormattedStartLocation + "&destinations=" + URLEncoder.encode(targets.get(i))
                            + "&key=AIzaSyA609lXN9_qcwJiHpo02CTuB0oXGxDDumo";
//                    Toast.makeText(MainActivity.this,FEED_URL,Toast.LENGTH_SHORT).show();
                    httpConnect jParser = new httpConnect();
//                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                int status = httpConnect.restConnection.getResponseCode();
                    String json = jParser.getJSONFromUrl(FEED_URL);
                    System.out.println(FEED_URL);
                    JSONObject object = new JSONObject(json);
                    GridItem item = new GridItem();
                    //contains ALL routes
                    JSONArray array = object.getJSONArray("rows");
                    // Get the first route
                    JSONObject route = array.getJSONObject(0);
                    // Take all elements
                    JSONArray elements = route.getJSONArray("elements");
                    //Tale First Element
                    JSONObject element = elements.getJSONObject(0);

                    // Get Duration
                    item.setName(targets.get(i));

                    // Get Distance
                    JSONObject distanceObject = element.getJSONObject("distance");
                    String distance = distanceObject.getString("text");
                    System.out.println("distance" + distance);
                    item.setDistance(distance);
//                    mGridData.add(item);
                    JSONObject durationObject = element.getJSONObject("duration");
                    String duration = durationObject.getString("text");
                    System.out.println("duration" + duration);
                    item.setDuration(duration);
                    item.setSlot(slots.get(targets.get(i)));
                mGridData.add(item);
//                    mGridAdapter.notifyItemInserted(mGridData.size());
                }
                System.out.println("dss");




                // sorting using Collections.sort(al, comparator);
                Collections.sort(mGridData,
                        new GridDataSortingComparator());

                // after Sorting: iterate using enhanced for-loop
                System.out.println("\n\nAfter Sorting: iterate using"
                        + " enhanced for-loop\n");
                for(GridItem data : mGridData) {
                    System.out.println(data.getDistance());
                }
//                Collections.sort(targets, new FirstNameSorter()
//                        .thenComparing(new LastNameSorter())
//                        .thenComparing(new AgeSorter()));
                System.out.println("Array:"+mGridData.get(1).getDistance());
                System.out.println("Array:"+mGridData.get(2).getDistance());
                System.out.println("Array:"+mGridData.get(3).getDistance());
                System.out.println("Array:"+mGridData.get(0).getDistance());
            } catch (Exception e) {
                Log.d("HHHHH", e.getLocalizedMessage());
            }

            return result;
        }

                public void clear(){
            int size = mGridData.size();
//            pd.setMessage("Loading data....");
//            pd.setIndeterminate(true);
//            pd.setCancelable(false);
//            pd.show();
                    mGridData.clear();
//           mGridAdapter.notifyItemRangeRemoved(0,size);
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(result==1) {
                mGridAdapter.notifyDataSetChanged();
                //Hide progressbar
//            mProgressBar.setVisibility(View.GONE);
                pd.dismiss();
            }
        }
    }

    public class AsyncHttpData extends AsyncTask<String, Void, String> {

        String FormattedStartLocation = ADD.replaceAll(" ", "+");



//            String FormattedGoalLocation = targets.get(i).replaceAll(" ", "+");

        @Override
        protected void onPreExecute() {
            System.out.println("PRE");
        }

        @Override
        protected String doInBackground(String... params) {

           String res="success";
             for(int i=0;i<targets.size();i++)
             {
                 String slot="";
                 String regurl = "https://smarttutorial.000webhostapp.com/fetchslot.php";
                 try {
                     URL url = new URL(regurl);
                     HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                     huc.setRequestMethod("POST");
                     huc.connect();
                     String data=URLEncoder.encode("area")+"="+URLEncoder.encode(targets.get(i));
                     OutputStream os=huc.getOutputStream();
                     BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                     bw.write(data);
                     bw.flush();
                     bw.close();
                     os.close();
                     InputStream is = huc.getInputStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is));
                     slot = br.readLine();
                     slots.put(targets.get(i),Integer.parseInt(slot));

                 } catch (MalformedURLException e) {
                     res ="1"+ e.getMessage();

                 } catch (FileNotFoundException e) {
                     res = "2"+e.getMessage();
                 } catch (Exception e) {
                     res = "3"+e.getMessage();
                 }

             }
            System.out.println("slots"+slots);
            return res;
        }

        public void clear(){
            int size = mGridData.size();
//            pd.setMessage("Loading data....");
//            pd.setIndeterminate(true);
//            pd.setCancelable(false);
//            pd.show();
            mGridData.clear();
//           mGridAdapter.notifyItemRangeRemoved(0,size);
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }
//    String streamToString(InputStream stream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
//        String line;
//        String result = "";
//        while ((line = bufferedReader.readLine()) != null) {
//            result += line;
//        }
//
//        // Close stream
//        if (null != stream) {
//            stream.close();
//        }
//        return result;
//    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    }


//    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
//
////        String Start_Location = (EditText) findViewById(R.id.StartLocation);
////        String Goal_Location = (EditText) findViewById(R.id.GoalLocation);
//
//        String FormattedStartLocation = ADD.replaceAll(" ", "+");
//        String FormattedGoalLocation = Goal.replaceAll(" ", "+");
//
//        String yourServiceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=km&origins=" + FormattedStartLocation + "&destinations=" + FormattedGoalLocation
//                + "&key=AIzaSyA609lXN9_qcwJiHpo02CTuB0oXGxDDumo";
//
//        @Override
//        protected void onPreExecute() {
//            ProgressBar spinner;
//            spinner = (ProgressBar) findViewById(R.id.progressBar1);
//            spinner.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            try {
//
//                httpConnect jParser = new httpConnect();
//                String json = jParser.getJSONFromUrl(yourServiceUrl);
//                JSONObject object = new JSONObject(json);
//
//                //contains ALL routes
//                JSONArray array = object.getJSONArray("rows");
//                // Get the first route
//                JSONObject route = array.getJSONObject(0);
//                // Take all elements
//                JSONArray elements = route.getJSONArray("elements");
//                //Tale First Element
//                JSONObject element = elements.getJSONObject(0);
//
//                // Get Duration
//                JSONObject durationObject = element.getJSONObject("duration");
//                String duration = durationObject.getString("text");
//                System.out.println("duration" +duration);
//                DurationResult = duration;
//
//                // Get Distance
//                JSONObject distanceObject = element.getJSONObject("distance");
//                String distance = distanceObject.getString("text");
//                System.out.println("distance" +distance);
//                DistanceResult = distance;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String strFromDoInBg) {
//
//            if(DistanceResult == null || DurationResult == null){
//                Toast ResultErrorHandle = Toast.makeText(getApplicationContext(), "We could not find any results! Sorry!", Toast.LENGTH_SHORT);
//                ResultErrorHandle.show();
//            }
//
//            ProgressBar spinner;
//            spinner = (ProgressBar) findViewById(R.id.progressBar1);
//            spinner.setVisibility(View.INVISIBLE);
//
////            mLatitudeTextView.setText("Distance: " + DistanceResult);
////
////            mLongitudeTextView.setText("Duration: " + DurationResult);
//        }
//    }
//    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
//            } else {
//                Log.w("MyCurrentloctionaddress", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w("MyCurrentloctionaddress", "Canont get Address!");
//        }
//        return strAdd;
//    }



//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Criteria;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
////import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.List;
//import java.util.Locale;
//
//public class MainActivity extends FragmentActivity implements LocationListener {
//    private GPSTracker gpsTracker;
//
////    GPSTracker gps = new GPSTracker(this);
//
////    GoogleMap googleMap;
//TextView dist;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //show error dialog if GoolglePlayServices not available
//        if (!isGooglePlayServicesAvailable()) {
//            finish();
//        }
//        setContentView(R.layout.activity_main);
////        SupportMapFragment supportMapFragment =
////                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
////        googleMap = supportMapFragment.getMap();
////        googleMap.setMyLocationEnabled(true);
//        dist =(TextView)findViewById(R.id.dist);
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }
//        }
//        Location location = locationManager.getLastKnownLocation(bestProvider);
//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        gpsTracker = new GPSTracker(MainActivity.this);
//        if (gpsTracker.canGetLocation()) {
//            double latitude = gpsTracker.getLatitude();
//            double longitude = gpsTracker.getLongitude();
//
//            System.out.println(getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
////            onLocationChanged(location);
//            System.out.println(gpsTracker.getLatitude()+ " " +gpsTracker.getLongitude());
////            tvLatitude.setText(String.valueOf(latitude));
////            tvLongitude.setText(String.valueOf(longitude));
//            dist.setText(getCompleteAddressString(latitude, longitude));
////            dist.setText(String.valueOf(getMesureLatLang(21.198445, 72.832188)));
//        } else {
//            gpsTracker.showSettingsAlert();
//        }
////        if (location != null) {
////            onLocationChanged(location);
////            System.out.println(getCompleteAddressString(gps.getLatitude(), gps.getLongitude()));
////        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        LatLng latLng = new LatLng(latitude, longitude);
//
////        googleMap.addMarker(new MarkerOptions().position(latLng));
////        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//    }
//
//    private boolean isGooglePlayServicesAvailable() {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (ConnectionResult.SUCCESS == status) {
//            return true;
//        } else {
//            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
//            return false;
//        }
//    }
//
//    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
//            } else {
//                Log.w("MyCurrentloctionaddress", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w("MyCurrentloctionaddress", "Canont get Address!");
//        }
//        return strAdd;
//    }
//}

//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.pm.PackageManager;
//import android.location.Location;
////import android.support.v4.app.ActivityCompat;
////import android.support.v4.content.ContextCompat;
////import android.support.v7.app.AppCompatActivity;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
////import com.google.android.gms.common.ConnectionResult;
////import com.google.android.gms.common.api.GoogleApiClient;
////import com.google.android.gms.common.api.PendingResult;
////import com.google.android.gms.common.api.ResultCallback;
////import com.google.android.gms.common.api.Status;
////import com.google.android.gms.location.LocationListener;
////import com.google.android.gms.location.LocationRequest;
////import com.google.android.gms.location.LocationServices;
////import com.google.android.gms.location.LocationSettingsRequest;
////import com.google.android.gms.location.LocationSettingsResult;
////import com.google.android.gms.location.LocationSettingsStatusCodes;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//    private TextView latitudeTextView,longitudeTextView;
//    private Location mylocation;
//    private GoogleApiClient googleApiClient;
//    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
//    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
//        longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);
//        setUpGClient();
//    }
//
//    private synchronized void setUpGClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, 0, this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        googleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mylocation = location;
//        if (mylocation != null) {
//            Double latitude=mylocation.getLatitude();
//            Double longitude=mylocation.getLongitude();
//            latitudeTextView.setText("Latitude : "+latitude);
//            longitudeTextView.setText("Longitude : "+longitude);
//            //Or Do whatever you want with your location
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        checkPermissions();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        //Do whatever you need
//        //You can display a message here
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        //You can display a message here
//    }
//
//    private void getMyLocation(){
//        if(googleApiClient!=null) {
//            if (googleApiClient.isConnected()) {
//                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//                    mylocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                    LocationRequest locationRequest = new LocationRequest();
//                    locationRequest.setInterval(3000);
//                    locationRequest.setFastestInterval(3000);
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                            .addLocationRequest(locationRequest);
//                    builder.setAlwaysShow(true);
//                    LocationServices.FusedLocationApi
//                            .requestLocationUpdates(googleApiClient, locationRequest, this);
//                    PendingResult<LocationSettingsResult> result =
//                            LocationServices.SettingsApi
//                                    .checkLocationSettings(googleApiClient, builder.build());
//                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//
//                        @Override
//                        public void onResult(LocationSettingsResult result) {
//                            final Status status = result.getStatus();
//                            switch (status.getStatusCode()) {
//                                case LocationSettingsStatusCodes.SUCCESS:
//                                    // All location settings are satisfied.
//                                    // You can initialize location requests here.
//                                    int permissionLocation = ContextCompat
//                                            .checkSelfPermission(MainActivity.this,
//                                                    Manifest.permission.ACCESS_FINE_LOCATION);
//                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//                                        mylocation = LocationServices.FusedLocationApi
//                                                .getLastLocation(googleApiClient);
//                                    }
//                                    break;
//                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                    // Location settings are not satisfied.
//                                    // But could be fixed by showing the user a dialog.
//                                    try {
//                                        // Show the dialog by calling startResolutionForResult(),
//                                        // and check the result in onActivityResult().
//                                        // Ask to turn on GPS automatically
//                                        status.startResolutionForResult(MainActivity.this,
//                                                REQUEST_CHECK_SETTINGS_GPS);
//                                    } catch (IntentSender.SendIntentException e) {
//                                        // Ignore the error.
//                                    }
//                                    break;
//                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                    // Location settings are not satisfied.
//                                    // However, we have no way
//                                    // to fix the
//                                    // settings so we won't show the dialog.
//                                    // finish();
//                                    break;
//                            }
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CHECK_SETTINGS_GPS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        getMyLocation();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        finish();
//                        break;
//                }
//                break;
//        }
//    }
//
//    private void checkPermissions(){
//        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//            if (!listPermissionsNeeded.isEmpty()) {
//                ActivityCompat.requestPermissions(this,
//                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
//            }
//        }else{
//            getMyLocation();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//            getMyLocation();
//        }
//    }
//}



//import android.content.pm.PackageManager;
////import android.support.v4.app.ActivityCompat;
////import android.support.v4.content.ContextCompat;
////import android.support.v7.app.AppCompatActivity;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.List;
//import java.util.Locale;
//
//public class MainActivity extends AppCompatActivity {
//    private GpsTracker gpsTracker;
//    private TextView tvLatitude, tvLongitude, tvadd,dist;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        tvLatitude = (TextView) findViewById(R.id.latitude);
//        tvLongitude = (TextView) findViewById(R.id.longitude);
//        tvadd = (TextView) findViewById(R.id.add);
//        dist =(TextView)findViewById(R.id.dist);
//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getLocation(View view) {
//        gpsTracker = new GpsTracker(MainActivity.this);
//        if (gpsTracker.canGetLocation()) {
//            double latitude = gpsTracker.getLatitude();
//            double longitude = gpsTracker.getLongitude();
//            tvLatitude.setText(String.valueOf(latitude));
//            tvLongitude.setText(String.valueOf(longitude));
//            tvadd.setText(getCompleteAddressString(latitude, longitude));
//            dist.setText(String.valueOf(getMesureLatLang(21.198445, 72.832188)));
//        } else {
//            gpsTracker.showSettingsAlert();
//        }
//    }
//
//    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
//            } else {
//                Log.w("MyCurrentloctionaddress", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w("MyCurrentloctionaddress", "Canont get Address!");
//        }
//        return strAdd;
//    }
//
////    private double getMesureLatLang(double lat2, double lon2) {
////        double lat1 = gpsTracker.getLatitude();
////        double lon1 = gpsTracker.getLongitude();
////        double theta = lon1 - lon2;
////        double dist = Math.sin(deg2rad(lat1))
////                * Math.sin(deg2rad(lat2))
////                + Math.cos(deg2rad(lat1))
////                * Math.cos(deg2rad(lat2))
////                * Math.cos(deg2rad(theta));
////        dist = Math.acos(dist);
////        dist = rad2deg(dist);
////        dist = dist * 60 * 1.1515;
////        return (dist);
////    }
////
////    private double deg2rad(double deg) {
////        return (deg * Math.PI / 180.0);
////    }
////
////    private double rad2deg(double rad) {
////        return (rad * 180.0 / Math.PI);
////    }
//    public float getMesureLatLang(double lat,double lang) {
//
//        Location loc1 = new Location("");
//        loc1.setLatitude(gpsTracker.getLatitude());// current latitude
//        loc1.setLongitude(gpsTracker.getLongitude());//current  Longitude
//
//        Location loc2 = new Location("");
//        loc2.setLatitude(lat);
//        loc2.setLongitude(lang);
//
//        return loc1.distanceTo(loc2);
//        //  return distance(getLatitute(),getLangitute(),lat,lang);
//    }
//}