package eyesec.eyesec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Main_Activity extends Activity {
    Thread thread=new Thread();
    TextView Longitude_result;
    TextView Lattitude_result;
    EditText rayon_submit;
    EditText area_submit;
    Button bt_ajout_lieu;
    TextView confirm;
    Intent locatorService = null;
    AlertDialog alertDialog = null;
    ProgressDialog progDailog = null;
    /**
     * Called when the activity is first created.
     */


    LocationManager myLocationManager;
    String PROVIDER = LocationManager.NETWORK_PROVIDER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mainactivity);
        Longitude_result = (TextView) findViewById(R.id.Longitude_result);
        Lattitude_result = (TextView) findViewById(R.id.Lattitude_result);
        area_submit=(EditText) findViewById((R.id.area_submit));
        rayon_submit=(EditText) findViewById(R.id.rayon_submit);
        bt_ajout_lieu=(Button) findViewById((R.id.bt_ajout_lieu));
        confirm=(TextView) findViewById(R.id.confirm);





bt_ajout_lieu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        area_submit.getText();
        if(area_submit.toString().equals("L107"))
        {


            confirm.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirm.setText("Already done");
                }
            }, 1000);


        }
else if(area_submit.length()>0 && rayon_submit.length()>0)
{
    confirm.postDelayed(new Runnable() {
        @Override
        public void run() {
            confirm.setText("Done");
        }
    }, 1000);
}
        else{
            confirm.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirm.setText("Error");
                }
            }, 1000);
        }
    }
});


        FetchCordinates fetch=new FetchCordinates();

        if (!startService()) {
            CreateAlert("Error!", "Service Cannot be started");
        } else {
            Toast.makeText(Main_Activity.this, "Service Started",
                    Toast.LENGTH_LONG).show();
        }
        /*

            Random rand = new Random();
            Double n = rand.nextDouble();
            Random Rand2 = new Random();
            Double n2 = rand.nextDouble();
            n = 25 + n * 10;
            n2 = 36 + n2 * 10;


            Longitude_result.setText(Double.toString(n));
            Lattitude_result.setText(Double.toString(n2));
*/

    }




    public boolean stopService() {
        if (this.locatorService != null) {
            this.locatorService = null;
        }
        return true;
    }

    public boolean startService() {
        try {
             //this.locatorService= new
            // Intent(FastMainActivity.this,LocatorService.class);
            // startService(this.locatorService);

            FetchCordinates fetchCordinates = new FetchCordinates();
            fetchCordinates.execute();
            return true;
        } catch (Exception error) {
            return false;
        }

    }

    public AlertDialog CreateAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle(title);

        alert.setMessage(message);

        return alert;

    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;
        Context context=getBaseContext();
        public double lati = 0.0;
        public double longi = 0.0;
        int lat;
        LocationListener locationlist=new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                lat = (int) location.getLatitude(); // * 1E6);

                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());
                lati = location.getLatitude();
                longi = location.getLongitude();
                String info = location.getProvider();
                    try {


                        lati = location.getLatitude();
                        longi = location.getLongitude();

                    } catch (Exception e) {
                        // progDailog.dismiss();
                        // Toast.makeText(getApplicationContext(),"Unable to get Location"
                        // , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;

            /*Context cont=getApplicationcontext();

            Activity mCurrentActivity = getApplicationActivity(cont);

            // Activity mCurrentActivity2= Activity.class;
            ActivityCompat.requestPermissions(mCurrentActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            ActivityCompat.requestPermissions(mCurrentActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);*/
            mLocationManager.requestLocationUpdates(
                        locationProvider, 100, 0,
                        locationlist);
              // locationlist.onLocationChanged(mLocationManager.getLastKnownLocation(locationProvider.toString()));

            progDailog = new ProgressDialog(Main_Activity.this);
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();

            String stringdouble=String.valueOf(lat);
            Lattitude_result.setText(stringdouble);

        }
        public Activity getApplicationActivity(Context contex)
        {
            Activity act=(Activity) contex;
            return act;
        }
        public Context getApplicationcontext()
        {
            Context cont=getApplicationcontext();
            return cont;
        }
        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
            progDailog.dismiss();
            // mLocationManager.removeUpdates(mVeggsterLocationListener);
        }

        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();

            Toast.makeText(Main_Activity.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (this.lati == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {

                    // LocatorService.myLatitude=location.getLatitude();

                    // LocatorService.myLongitude=location.getLongitude();

                    lati = location.getLatitude();
                    longi = location.getLongitude();

                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }

}