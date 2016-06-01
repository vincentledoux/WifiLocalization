package eyesec.eyesec;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


//import org.w3c.dom.Text;

public class Main_Activity_user extends Activity implements LocationListener{



    private LocationManager lm;

    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;
     TextView Latitude_result;
     TextView Longitude_result;
     TextView confirm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mainactivity_user);
        Latitude_result=(TextView) findViewById(R.id.Lattitude_result);
        Longitude_result=(TextView)findViewById(R.id.Longitude_result);
        confirm=(TextView)findViewById(R.id.Salle);

        loop();

    }

    @Override
    public void onResume() {
        super.onResume();



        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0,
                    this);
        Location location;
        if (lm != null) {
            location = lm
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                altitude=location.getAccuracy();
                Longitude_result.setText(Double.toString(longitude));
                Latitude_result.setText(Double.toString(latitude));
                confirm.setText("Accuracy: "+Double.toString(altitude));

            } else {
                Longitude_result.setText("location  null");
            }
            //onLocationChanged(lm.getLastKnownLocation(lm.NETWORK_PROVIDER.toString()));

        }else{
            Longitude_result.setText("location manager null");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        lm.removeUpdates(this);
    }


    public boolean isRouteDisplayed() {
        return false;
    }


    public void loop()
    {

        Thread timer = new Thread() { //new thread
            public void run() {
                boolean b = true;
                try {
                    do {
                       // counter++;

                        sleep(1000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                onResume();
                                //title.clearComposingText();//not useful

                            }
                        });


                    }
                    while (b == true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                }
            };
        };
        timer.start();


    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        accuracy = location.getAccuracy();

        String msg = String.format(
                getResources().getString(R.string.confirm), latitude,
                longitude, altitude, accuracy);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.Longitude_result), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.confirm), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        String msg = String.format(
                getResources().getString(R.string.confirm), provider,
                newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}