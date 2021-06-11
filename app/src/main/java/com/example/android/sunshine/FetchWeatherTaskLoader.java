package com.example.android.sunshine;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import android.content.AsyncTaskLoader;

import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import java.net.URL;

public class FetchWeatherTaskLoader extends AsyncTaskLoader<String[]>
{
    private Context mContext;
    private URL mQueryUrl = null;
    public FetchWeatherTaskLoader(Context context, URL queryUrl)
    {
        super(context);
        mContext = context;
        mQueryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String[] loadInBackground()
    {
        Log.i("loadInBackground", "LOADING !!");
        if(mQueryUrl == null)
            return null;

        try {
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(mQueryUrl);
            String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(mContext, jsonWeatherResponse);
            return simpleJsonWeatherData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
