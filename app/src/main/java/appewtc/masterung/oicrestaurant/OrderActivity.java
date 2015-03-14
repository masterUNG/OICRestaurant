package appewtc.masterung.oicrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class OrderActivity extends ActionBarActivity {

    private FoodTABLE objFoodTABLE;
    private TextView txtShowOfficer;
    private Spinner mySpinner;
    private ListView myListView;
    private String strOfficer, strDesk, strFood, strItem, strDeskSpinner[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        bindWidget();

        objFoodTABLE = new FoodTABLE(this);

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

        //Show officer
        showOfficer();

        //Create Spinner
        createSpinner();

        //Create ListView
        createListView();

    }   // onCreate

    private void createListView() {

        //Setup Array
        int[] intMyImageFood = {R.drawable.food1, R.drawable.food2,
                R.drawable.food3, R.drawable.food4, R.drawable.food5,
                R.drawable.food6, R.drawable.food7, R.drawable.food8,
                R.drawable.food9, R.drawable.food10, R.drawable.food11,
                R.drawable.food12, R.drawable.food13, R.drawable.food14,
                R.drawable.food15, R.drawable.food16, R.drawable.food17,
                R.drawable.food18, R.drawable.food19, R.drawable.food20 };

        final String[] strListFood = objFoodTABLE.readAllFood();
        String[] strListPrice = objFoodTABLE.readAllPrice();

        MyAdapter objMyAdapter = new MyAdapter(OrderActivity.this, strListFood, strListPrice, intMyImageFood);
        myListView.setAdapter(objMyAdapter);

        //Click Active
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strFood = strListFood[position];
                chooseItem();

            }   // event
        });

    }   // createListView

    private void chooseItem() {
        CharSequence[] charItem = {"1 จาน", "2 จาน", "3 จาน", "3 จาน", "5 จาน",};

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Choose Item ?");
        objBuilder.setCancelable(false);
        objBuilder.setSingleChoiceItems(charItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        strItem = "1";
                        break;
                    case 1:
                        strItem = "2";
                        break;
                    case 2:
                        strItem = "3";
                        break;
                    case 3:
                        strItem = "4";
                        break;
                    case 4:
                        strItem = "5";
                        break;

                }   // switch

                dialog.dismiss();

                //Confirm Order
                confirmOrder();

            }   // event
        });

        AlertDialog objAlertDialog = objBuilder.create();
        objAlertDialog.show();


    }   // chooseItem

    private void confirmOrder() {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Confirm Order Please");
        objBuilder.setCancelable(false);
        objBuilder.setMessage(strOfficer + "\n" + "Desk = " + strDesk + "\n" + strFood + "\n" + "item = " + strItem);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upOrderToMySQL();
                dialog.dismiss();
            }
        });
        objBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        objBuilder.show();

    }   // confirmOrder

    private void upOrderToMySQL() {
        try {

            ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
            objNameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            objNameValuePairs.add(new BasicNameValuePair("Officer", strOfficer));
            objNameValuePairs.add(new BasicNameValuePair("Desk", strDesk));
            objNameValuePairs.add(new BasicNameValuePair("Food", strFood));
            objNameValuePairs.add(new BasicNameValuePair("Item", strItem));

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/olc/add_data_restaurant.php");
            objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
            objHttpClient.execute(objHttpPost);
            Toast.makeText(OrderActivity.this, "Up Value Finish", 5000).show();

        } catch (Exception e) {
            Log.d("oic", "upOrder ==> " + e.toString());
        }
    }


    private void createSpinner() {

        //Setup Array Spinner
        strDeskSpinner = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

        ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strDeskSpinner);
        mySpinner.setAdapter(objAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDesk = strDeskSpinner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strDesk = strDeskSpinner[0];
            }
        });

    }   // createSpinner

    private void showOfficer() {
        strOfficer = getIntent().getExtras().getString("Officer");
        txtShowOfficer.setText(strOfficer);
    }

    private void bindWidget() {
        txtShowOfficer = (TextView) findViewById(R.id.txtShowOfficer);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myListView = (ListView) findViewById(R.id.listView);
    }

    private void synJSONtoSQLite() {

        //Setup New Policy
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy objPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(objPolicy);
        }


        //Create InputStream
        InputStream objInputStream = null;
        String strJSON = "";

        try {

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/olc/php_get_data_food.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {
            Log.d("oic", "InputStream ==> " + e.toString());
        }


        //Create strJSON
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objBufferedReader.readLine()) != null) {
                objStringBuilder.append(strLine);
            }   // while

            objInputStream.close();
            strJSON = objStringBuilder.toString();


        } catch (Exception e) {
            Log.d("oic", "strJSON ==> " + e.toString());
        }


        //UpData SQLite
        try {

            final JSONArray objJsonArray = new JSONArray(strJSON);
            for (int i = 0; i < objJsonArray.length(); i++) {

                JSONObject objJSONObject = objJsonArray.getJSONObject(i);

                String strFood = objJSONObject.getString("Food");
                String strPrice = objJSONObject.getString("Price");

                long addValue = objFoodTABLE.addValueToFood(strFood, strPrice);

            }   // for

        } catch (Exception e) {
            Log.d("oic", "Update ==> " + e.toString());

        }


    }   // synJSONtoSQLite


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
}   // Main Class
