package eyesec.eyesec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;



/*
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;*/

public class LoginActivity extends Activity {
    // User name
    private EditText et_Username;
    // Password
    private EditText et_Password;
    // Sign In
    private Button bt_SignIn;

    private Button bt_LogIn;
    // Message
    private TextView tv_Message;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        // Initialization
        et_Username = (EditText) findViewById(R.id.et_Username);
        et_Password = (EditText) findViewById(R.id.et_Password);
        bt_SignIn = (Button) findViewById(R.id.bt_SignIn);
        tv_Message = (TextView) findViewById(R.id.tv_Message);
        bt_LogIn=(Button) findViewById(R.id.bt_SignOn);
        String monadresse=null;


        bt_SignIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // Stores User name
                String username = String.valueOf(et_Username.getText());
                // Stores Password
                String password = String.valueOf(et_Password.getText());

                // Validates the User name and Password for admin, admin

                if (username.equals("root@eyesec.local") && password.equals("test")  ) {
                    tv_Message.setText("Access granted");

                    Intent I=new Intent(LoginActivity.this, Main_Activity.class);
                    startActivity(I);

                } else {
                    tv_Message.setText("Access denied");
                }

               if (username.equals("ledoux vincent") && password.equals("test"))
                {
                    tv_Message.setText("Access granted");

                    Intent I=new Intent(LoginActivity.this, Main_Activity_user.class);
                    startActivity(I);
                }
            }
        });



        bt_LogIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                String mac=GetMacAdress(getBaseContext());
                String x="02:00:00:00:00:00";
                if(mac.equals(x)) {
                    tv_Message.setText("Access grant");
                    Intent I=new Intent(LoginActivity.this, Main_Activity_user.class);
                    startActivity(I);
                }
                else {
                    try {
                        insert();
                        tv_Message.setText(mac);
                    } catch (Exception e) {
                        tv_Message.setText("Access Denied");
                    }
                }


               /* } catch (Exception e) {
                    e.printStackTrace();

                    Log.w("Android-system","system get connection");
                    tv_Message.setText(GetMacAdress(getApplicationContext()));
                    tv_Message.setText("Access denied");
                }*/

                // Validates the User name and Password for admin, admin


            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void insert()
    {
        String mac=GetMacAdress(getBaseContext());

        String username = et_Username.getText().toString();
        String password=et_Password.getText().toString();

        String link="http://ibo.labs.esilv.fr/~groupe86/Inscription.php";
        try{
       JSONParser jsonParser = new JSONParser();






        HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost("http://10.0.2.2/project/insert.php");


                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                    nameValuePairs.add(new BasicNameValuePair("mac_adress", mac));
                    JSONArray json = jsonParser.MakeHttpRequest(
                            link, "POST", nameValuePairs);


                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    tv_Message.setText("Access granted");
                } catch (Exception e) {
                    e.printStackTrace();
                    tv_Message.setText("Access denied");
                }
            }

    public void testDB(EditText login, EditText password) {



        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Log.i("Android", " MySQL Connection ok");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/eyesec_base", "root", "");
            //         System.out.println("Database connection success");
            Log.d("Android2","Line 2");
            String result = ("");
            Log.d("Android3", " Line 3");
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("select * from entreprise");

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                //result += rsmd.getColumnName(2) + ": " + rs.getInt(1) + "\n";
                result=rs.getString((3));
                result += " "+ rs.getString(4);
                result += " "+rs.getString(2);
                String pass=rs.getString(5);
                int id=rs.getInt(1);
                if(login.toString()==result && pass==password.toString()) {
                    Statement lm=con.createStatement();

                    ResultSet ls=st.executeQuery("insert into connection values("+id+","+rs.getString(4)+","+rs.getString(2)+","+rs.getString((3))+","+GetMacAdress(getBaseContext()));
                }
            }



        } catch (Exception e) {
            e.printStackTrace();

            Log.w("Android-system","system get connection");
        }
    }

    private String GetMacAdress(Context context) {
       // final WifiManager wifiManager = (WifiManager) getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //final String wifiMACaddress = wifiManager.getConnectionInfo().getMacAddress();
        WifiManager wimanager = (WifiManager)getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String macadress = wimanager.getConnectionInfo().getMacAddress();
        if (macadress == null) {
            return "vous n'êtes pas connecté au wifi";
        } else {
            return macadress;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /*@Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this)
                .addApi(LOCATION_SERVICE.API)
                .addScope(Loc.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://eyesec.eyesecentreprise/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://eyesec.eyesecentreprise/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
