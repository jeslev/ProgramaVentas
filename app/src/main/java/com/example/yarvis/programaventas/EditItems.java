package com.example.yarvis.programaventas;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditItems extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    HashMap<String,Integer> productsBuying;

    // url to get all products list
    private static String url_all_products = "http://192.168.0.3/lab3/get_all_my_buys.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "product";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_PRICE = "precio";
    private static final String TAG_QUANT = "cant";

    // products JSONArray
    JSONArray products = null;

    public Button btn1;
    public Button btn2;

    int uid, ridd;
    String rid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        Intent intent = getIntent();
        uid = intent.getIntExtra("UserID",0);
        rid = intent.getStringExtra("ReciboID");
        ridd = new Integer(rid).intValue();

        productsBuying = new HashMap<String,Integer>();
        // Loading products in Background Thread
        new listAllBuys().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),	BuyProductActibity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);
                in.putExtra("UserID",uid);

                startActivity(in);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class listAllBuys extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditItems.this);
            pDialog.setMessage("Cargando productos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid", ""+uid));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Buys: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, "Recibo "+id);
                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    // no products found
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            EditItems.this, productsList,
                            R.layout.list_item2, new String[] { TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
