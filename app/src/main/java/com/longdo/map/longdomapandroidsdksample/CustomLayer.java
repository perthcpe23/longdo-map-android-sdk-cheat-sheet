package com.longdo.map.longdomapandroidsdksample;

import android.content.Context;

import com.longdo.api.Layer;

import java.util.Hashtable;

/**
 * Created by perth on 1/4/2017.
 */

public class CustomLayer extends Layer{
    public CustomLayer(Context context) {
        super(context, "custom", LAYER_TYPE_CUSTOM, 0, "", 1, 20);
    }

    @Override
    public String url(String url, long x, long y, int zoomLevel, Hashtable GETParams, String apiKey) {
        return "http://webdev.longdo.com/~punyapat/dog.png";
    }
}
