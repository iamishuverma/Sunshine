
package com.example.android.sunshine;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
//import androidx.loader.content.Loader;
import android.content.Loader;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.ForecastAdapter.ForecastAdapterOnClickHandler;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.android.sunshine.FetchWeatherTaskLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ForecastAdapterOnClickHandler, LoaderCallbacks<String[]> {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private URL weatherRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        // Passing this(MainActivity.java) so that when user clicks on list item, overridden onClick() method inside MainActivity is called.
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        loadWeatherData();
    }

    // Overriding onClick() method of ForecastAdapterOnClickHandler interface.
    @Override
    public void onClick(String weatherForDay) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);
        startActivity(intentToStartDetailActivity);
    }

    private void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String[]> onCreateLoader(int i, Bundle bundle) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return new FetchWeatherTaskLoader(MainActivity.this, weatherRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] strings) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if(strings != null) {
            showWeatherDataView();
            mForecastAdapter.setWeatherData(strings);
        }
        else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    private void loadWeatherData() {
        showWeatherDataView();

        double[] coordinates = SunshinePreferences.getLocationCoordinates(this);
        Double[] locationCoordinates = new Double[2];
        locationCoordinates[0] = Double.parseDouble(String.valueOf(coordinates[0]));
        locationCoordinates[1] = Double.parseDouble(String.valueOf(coordinates[1]));

        weatherRequestUrl = NetworkUtils.buildUrl(locationCoordinates[0], locationCoordinates[1]);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;                                                                    // return true to display the menu.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mForecastAdapter.setWeatherData(null);
            getLoaderManager().restartLoader(1, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}