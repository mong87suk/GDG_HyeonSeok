package com.example.android.sunshine.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String data[] = {
                    "Mon 6/23 - Sunny - 31/17",
                                        "Tue 6/24 - Foggy - 21/8",
                                        "Wed 6/25 - Cloudy - 22/17",
                                        "Thurs 6/26 - Rainy - 18/11",
                                        "Fri 6/27 - Foggy - 21/10",
                                        "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                                        "Sun 6/29 - Sunny - 20/7"
            };

            List<String> forecastEntry = new ArrayList<>(Arrays.asList(data));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview, forecastEntry);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ((ListView)rootView.findViewById(R.id.listview_forecast)).setAdapter(adapter);

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=41f7f39b504da10c71848d31dc3927b4");
                String value = url.getProtocol();
                Log.v(TAG, "protocol:" + value);
                value = url.getAuthority();
                Log.v(TAG, "Authority:" + value);
                value = url.getUserInfo();
                Log.v(TAG, "UserInfo:" + value);
                int port = url.getPort();
                Log.v(TAG, "Port:" + port);
                value = url.getFile();
                Log.v(TAG, "File:" + value);
                value = url.getPath();
                Log.v(TAG, "Path:" + value);
                value = url.getRef();
                Log.v(TAG, "Ref:" + value);
                value = url.getQuery();
                Log.v(TAG, "Query:" + value);
                urlConnection = (HttpURLConnection) url.openConnection();
                value = urlConnection.getRequestMethod();
                Log.v(TAG, "RequestMethod:" + value);
                urlConnection.setRequestMethod(value);
                HandlerThread handlerThread = new HandlerThread("network_connection_test");
                handlerThread.start();
                Handler handler = new Handler(handlerThread.getLooper());
                final HttpURLConnection finalUrlConnection = urlConnection;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader reader;
                        try {
                            finalUrlConnection.connect();
                            InputStream inputStream = finalUrlConnection.getInputStream();
                            StringBuffer buffer = new StringBuffer();
                            if (inputStream == null) {
                                return;
                            }
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                                // But it does make debugging a *lot* easier if you print out the completed
                                // buffer for debugging.
                                Log.v(TAG, "line:" + line);
                                Log.d(TAG, "test");
                                buffer.append(line + "\n");
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }
}
