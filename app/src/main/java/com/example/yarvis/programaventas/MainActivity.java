package com.example.yarvis.programaventas;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText inputUser;
    EditText inputPassword;
    private ProgressDialog pDialog;
    private static StaticVariables staticVariables = new StaticVariables();

    JSONParser jParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    ArrayList<HashMap<String, String>> usersList;
    private static String url_validate_user= "http://"+staticVariables.getIpServer()+"/lab3/login.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        // Edit Text
        inputUser = (EditText) findViewById(R.id.inputUser);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Validating login
                new ValidateLogin().execute();

            }
        });
    }

    class ValidateLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Validando login..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            System.err.println("antes");
            String user = inputUser.getText().toString();
            String psswd = inputPassword.getText().toString();
            System.err.println("despues");
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user", user));
            params.add(new BasicNameValuePair("password", psswd));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_validate_user,"POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    int uid = json.getInt("uid");
                    if(uid==2){
                        // successfully validated login
                        Intent i = new Intent(getApplicationContext(), AdminOptionActivity.class);
                        i.putExtra("UserID", uid);
                        startActivity(i);
                    }
                    else {
                        // successfully validated login
                        Intent i = new Intent(getApplicationContext(), UserOptionActivity.class);
                        i.putExtra("UserID", uid);
                        startActivity(i);
                    }
                    // closing this screen
                    finish();
                } else {
                    // failed to login

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
