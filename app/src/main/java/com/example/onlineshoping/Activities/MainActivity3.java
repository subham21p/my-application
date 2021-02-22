package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onlineshoping.Adapters.SearchAdapter;
import com.example.onlineshoping.ModelClasses.SearchData;
import com.example.onlineshoping.ModelClasses.SuperCar;
import com.example.onlineshoping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.SearchManager.*;

public class MainActivity3 extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
  //  private SearchAdapter mAdapter;
    public static   List<SearchData> searchData;
    SearchView searchView = null;
    Context context;


 //  private ArrayAdapter<String> adapter;

    // Define array List for List View data
   private ArrayList<String> mylist;

 //   RecyclerView recyclerView;
    ArrayList<String> names;

    SearchAdapter adapter;


   private ListView listView;

    // Define array adapter for ListView
  private ArrayAdapter<String> adapter2;

    // Define array List for List View data
    ArrayList<String> mylist2;

Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        button = (Button)findViewById(R.id.buttonAM3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

    /*    recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity3.this));

        listView = findViewById(R.id.listView);
        mylist = new ArrayList<>();
        mylist.add("C");
        mylist.add("C++");
        mylist.add("C#");
        mylist.add("Java");
        mylist.add("Advanced java");
        mylist.add("Interview prep with c++");
        mylist.add("Interview prep with java");
        mylist.add("data structures with c");
        mylist.add("data structures with java");

        searchData = new ArrayList<>();
        names = new ArrayList<>();

       *//* adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter2);*//*

         // downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");


        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter2);*/





    }




  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_main, menu);
        Log.i("searchItem1", "["+menu+"]");
        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    { Log.i("searchItem2", "["+query+"]");


                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (mylist.contains(query)) {
                            Log.i("searchItem3", "["+query+"]");
                            adapter2.getFilter().filter(query);
                        }
                        else {
                            // Search query not found in List View
                            Toast
                                    .makeText(MainActivity3.this,
                                            "Not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        Log.i("searchItem4", "["+newText+"]");
                        adapter2.getFilter().filter(newText);
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }*/














   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_main, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.search_bar);
         searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (searchData.contains(query)) {
                        //    mAdapter.getFilter().filter(query);
                            downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");
                        }
                        else {
                            // Search query not found in List View
                            Toast
                                    .makeText(MainActivity3.this,
                                            "Not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                       // mAdapter.getFilter().filter(newText);
                        downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }*/




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);
        downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");
        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {

            searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
            Log.i("searchItem", "["+searchItem+"]");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                   // downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                  //  adapter.getFilter().filter(newText);
                  //  filter(newText);
                    return false;
                }
            });
        }
        if (searchView != null) {
            Log.i("searchItem1", "["+searchItem+"]");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity3.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }
*/







 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity3.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity3.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        downloadJSON("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");
        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            adapter.getFilter();
           // new AsyncFetch(query).execute();

        }
    }
*/








/*    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                  *//*  RequestHandler requestHandler = new RequestHandler();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);

                    //returing the response
                    return requestHandler.sendPostRequest(Constants.URL_LOGIN, params);*//*
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();

    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[jsonArray.length()];
       // stocks1 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
              stocks[i] = obj.getInt("id") + ")" + obj.getString("question") + "\n" + obj.getString("option_one") + "\n" + obj.getString("option_two") + "\n" + obj.getString("option_three") + "\n" + obj.getString("option_four") + "\n" + obj.getString("correct_option");
            //  cQuestions cQuestions = new cQuestions()

           // stocks1[i] = obj.getString("correct_option");
          *//*  textArray.append(stocks1[i]);
            textArray.append("\n");*//*
            //   textArray.getText().toString();

                names.add(obj.getString("name"));
            searchData.add(new SearchData(
                    obj.getString("name")
            ));

            //storing the user in shared preferences
            // Subjects.SharedPrefManager1.getInstance(getApplicationContext()).Question(cquestions);
            Log.i("tagconvertstr", "["+searchData+"]");

        }
      //  adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
       // SearchAdapter adapter1 = new SearchAdapter(MainActivity3.this, searchData);
       // recyclerView.setAdapter(adapter1);

      *//*  SearchAdapter adapter = new SearchAdapter(MainActivity3.this, searchData);

        recyclerView.setAdapter(adapter);*//*


        adapter = new SearchAdapter(names,MainActivity3.this);
        recyclerView.setAdapter(adapter);











    }*/













/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {

            searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
            ComponentName componentName = new ComponentName(this, MainActivity3.class);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
            Log.i("searchItem", "["+searchItem+"]");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
        }
        if (searchView != null) {
            Log.i("searchItem1", "["+searchItem+"]");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity3.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("searchItem2", "["+item+"]");

        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch

        Log.i("intent", "["+intent+"]");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(QUERY);

            if (searchView != null) {
                Log.i("intent2", "["+intent+"]");
                searchView.clearFocus();
            }
            new MainActivity3.AsyncFetch(query).execute();

        }
    }

    // Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(MainActivity3.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.1.6/onlineShoping/includes/Api.php?apicall=c");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Arrat", "["+result+"]");
            //this method will be running on UI thread
            pdLoading.dismiss();
            List<SearchData> data=new ArrayList<>();

            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(MainActivity3.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
            }else{

                try {

                    JSONArray jArray = new JSONArray(result);
                    Log.i("Arrat", "["+jArray+"]");
                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        SearchData fishData = new SearchData(json_data.getString("name"));
                    //    fishData.Name = json_data.getString("name");
                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFish = (RecyclerView) findViewById(R.id.recyclerView3);
                    mAdapter = new SearchAdapter(MainActivity3.this, data);
                    mRVFish.setAdapter(mAdapter);
                    mRVFish.setLayoutManager(new LinearLayoutManager(MainActivity3.this));

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(MainActivity3.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity3.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }

*/





}