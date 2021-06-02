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
    private URL mqueryUrl = null;
    public FetchWeatherTaskLoader(Context context, URL queryUrl)
    {
        super(context);
        mqueryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String[] loadInBackground()
    {
        if(mqueryUrl == null)
            return null;

        try {
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(mqueryUrl);
            String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(getContext(), jsonWeatherResponse);
            return simpleJsonWeatherData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
