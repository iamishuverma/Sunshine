package com.example.android.sunshine.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.sunshine.MainActivity;
import com.example.android.sunshine.data.SunshinePreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.openweathermap.org";
    private static final String API_KEY = "968f9d38fa95270331bc5688692e0c9a";
    //private static final String API_KEY = "********************************";                       // Not displaying actual API key on Github.


    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    private static final int numDays = 7;

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param cityIDQuery The ID of city that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String cityIDQuery) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("onecall")

                .appendQueryParameter("id", String.valueOf(cityIDQuery))
                .appendQueryParameter("cnt", String.valueOf(numDays))
                .appendQueryParameter("appid", API_KEY);

        String myUrl = builder.build().toString();

        URL url = null;
        try
        {
            url = new URL(myUrl);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static URL buildUrl(Double lat, Double lon){
        /** This will be implemented in a future lesson **/

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("onecall")
                .appendQueryParameter("lat", String.valueOf(lat))
                .appendQueryParameter("lon", String.valueOf(lon))
                .appendQueryParameter("exclude", "hourly")
                .appendQueryParameter("appid", API_KEY);

        String myUrl = builder.build().toString();

        URL url = null;
        try
        {
            url = new URL(myUrl);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}