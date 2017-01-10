package com.longdo.map.longdomapandroidsdksample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.longdo.api.ICacheListener;
import com.longdo.api.IClickListener;
import com.longdo.api.ILineListener;
import com.longdo.api.ILocationListener;
import com.longdo.api.IMapListener;
import com.longdo.api.IMarker;
import com.longdo.api.IPinListener;
import com.longdo.api.IZoomListener;
import com.longdo.api.Layer;
import com.longdo.api.Line;
import com.longdo.api.LongdoLayer;
import com.longdo.api.Map;
import com.longdo.api.MapGLSurfaceView;
import com.longdo.api.Pin;
import com.longdo.api.Polygon;
import com.longdo.api.type.MapLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements IMapListener{

    //UI references
    private Map map;

    private Pin lastPushedPin;
    private Line lastPushedLine;
    private Polygon lastPushedPolygon;

    //Control variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapGLSurfaceView mapGLSurfaceView = (MapGLSurfaceView) findViewById(R.id.map);
        mapGLSurfaceView.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Map
        if(item.getItemId() == R.id.action_map_bg){
            map.setMapBackground(BitmapFactory.decodeResource(getResources(),R.drawable.map_bg));
        }else if(item.getItemId() == R.id.action_toggle_cross_sign){
            map.setDrawCrossSign(!map.isDrawCrossSign());
        }else if(item.getItemId() == R.id.action_toggle_tile_animation){
            map.setDoTileAnimation(!map.isDoTileAnimation());
        }else if(item.getItemId() == R.id.action_zoom_in){
            map.zoomIn(true);
        }else if(item.getItemId() == R.id.action_zoom_out){
            map.zoomOut(true);
        }else if(item.getItemId() == R.id.action_set_zoom){
            map.setZoom(15,true);
        }else if(item.getItemId() == R.id.action_toggle_touch){
            map.setEnableTouch(!map.isEnableTouch());
        }else if(item.getItemId() == R.id.action_toggle_scale_bar){
            map.setDrawScaleBar(!map.isDrawScaleBar());
        }else if(item.getItemId() == R.id.action_change_scale_bar_ui){
            map.setScaleBarColor(Color.RED);
            map.setScaleBarLineWidth(3);
            map.setScaleBarTextSize(20);
            map.setDrawScaleBar(true);
        }else if(item.getItemId() == R.id.action_map_listener){
            map.setClickListener(new IClickListener() {
                @Override
                public void onClick(MapLocation mapLocation, int i) {
                    Toast.makeText(MainActivity.this,"map click",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDoubleClick(MapLocation mapLocation, int i) {
                    Toast.makeText(MainActivity.this,"map long click",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(Bundle bundle) {
                    Toast.makeText(MainActivity.this,"map double click",Toast.LENGTH_SHORT).show();
                }
            });

            map.setZoomListener(new IZoomListener() {
                @Override
                public void preZoom(int i) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"zoom in",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void postZoom(int i) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"zoom out",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void finishZoomAnimation(int i) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"finish animation",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        // Pin
        else if(item.getItemId() == R.id.action_push_pin){
            lastPushedPin = createPin();
            map.pushPin(lastPushedPin);
        }else if(item.getItemId() == R.id.action_move_pin){
            if(!hasLastPin()) return true;
            lastPushedPin.setLocation(map.getLocation(),true,100);
        }else if(item.getItemId() == R.id.action_change_pin_icon){
            if(!hasLastPin()) return true;
            lastPushedPin.setIcon(this,R.drawable.pin2);
        }else if(item.getItemId() == R.id.action_bounce_pin){
            if(!hasLastPin()) return true;
            lastPushedPin.setAnimation(Pin.ANIMATION_BOUNCE);
        }else if(item.getItemId() == R.id.action_drop_pin){
            lastPushedPin = createPin();
            lastPushedPin.setAnimation(Pin.ANIMATION_DROP);
            map.pushPin(lastPushedPin);
        }else if(item.getItemId() == R.id.action_pin_listener){
            if(!hasLastPin()) return true;
            lastPushedPin.setListener(new IPinListener() {
                @Override
                public boolean onPinClick(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,"pin click",Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onLongClickPin(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,"pin long click",Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onPinDoubleClick(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,"pin double click",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        // Line
        else if(item.getItemId() == R.id.action_push_line){
            lastPushedLine = createLine();
            map.addLine(lastPushedLine);
        }else if(item.getItemId() == R.id.action_line_listener){
            if(!hasLastLine()) return true;
            lastPushedLine.setListener(new ILineListener() {
                @Override
                public boolean onLineClick(Line line, Line[] lines) {
                    Toast.makeText(MainActivity.this,"line click",Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onLineLongClick(Line line, Line[] lines) {
                    Toast.makeText(MainActivity.this,"line long click",Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onLineDoubleClick(Line line, Line[] lines) {
                    Toast.makeText(MainActivity.this,"line double click",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        // Polygon
        else if(item.getItemId() == R.id.action_push_polygon){
            lastPushedPolygon = createPolygon();
            map.pushPolygon(lastPushedPolygon);
        }
        /*
        else if(item.getItemId() == R.id.action_polygon_listener){
            if(!hasLastPolygon()) return true;
        }
        */

        // Layer
        else if(item.getItemId() == R.id.action_add_layer){
            // WMS Layer
            Layer wmsLayer = new Layer(this,"bluemarble_terrain",Layer.LAYER_TYPE_WMS,0,"https://ms.longdo.com/mapproxy/service",1,20);

            /* TMS Layer
            Layer tmsLayer = new Layer(this,"bluemarble_terrain",Layer.LAYER_TYPE_TMS,0,"https://ms.longdo.com/mapproxy/tms/1.0.0/bluemarble_terrain/EPSG3857",1,20);
            Hashtable<String,String> options = new Hashtable<>();
            options.put("zoom-offset","-1");
            tmsLayer.setOptions(options);
             */

            /* WMTS Layer
            Layer wmtsLayer = new Layer(this,"bluemarble_terrain",Layer.LAYER_TYPE_WMTS_REST,0,"https://ms.longdo.com/mapproxy/wmts",1,20);
             */

            map.addLayer(wmsLayer);
        }
        else if(item.getItemId() == R.id.action_add_longdo_layer){
            LongdoLayer layer = new LongdoLayer(this,"gray",0,1,20);
            layer.setTransparent(true);
            map.setBase(layer);
        }
        else if(item.getItemId() == R.id.action_add_custom_layer){
            map.addLayer(new CustomLayer(this));
        }

        // Tag
        else if(item.getItemId() == R.id.action_show_gas_station){
            map.addTag("gas_station");
            map.setTagListener(new IPinListener() {
                @Override
                public boolean onPinClick(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,pin.getTag(),Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onLongClickPin(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,pin.getTag(),Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onPinDoubleClick(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,pin.getTag(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        // Location
        else if(item.getItemId() == R.id.action_get_current_location) {
            map.setLocationListener(new ILocationListener() {
                @Override
                public void onGetCurrentLocation(MapLocation mapLocation, Location location) {
                    map.setLocation(mapLocation);
                    Toast.makeText(MainActivity.this, "speed: " + location.getSpeed() + "meters/second", Toast.LENGTH_SHORT).show();
                }
            });

            map.getCurrentLocation();
        }
        else if(item.getItemId() == R.id.action_update_location) {
            map.setLocationListener(new ILocationListener() {
                @Override
                public void onGetCurrentLocation(MapLocation mapLocation, Location location) {
                    map.setLocation(mapLocation,true);
                    Toast.makeText(MainActivity.this, "speed: " + location.getSpeed() + "meters/second", Toast.LENGTH_SHORT).show();
                }
            });

            map.setMode((short) (map.getMode() | Map.MODE_UPDATING_LOCATION));
        }else if(item.getItemId() == R.id.action_lock_center) {
            map.setMode((short) (map.getMode() | Map.MODE_TRACK_USER));
        }

        // Camera
        else if(item.getItemId() == R.id.action_camera) {
            IPinListener listener = new IPinListener() {
                @Override
                public boolean onPinClick(Pin pin, Pin[] pins) {
                    try {
                        JSONObject data = new JSONObject(pin.getTag());
                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        intent.putExtra(CameraActivity.VDO_URL,data.getString("vdourl"));
                        intent.putExtra(CameraActivity.CAMERA_NAME,data.getString("title"));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }

                @Override
                public boolean onLongClickPin(Pin pin, Pin[] pins) {
                    return false;
                }

                @Override
                public boolean onPinDoubleClick(Pin pin, Pin[] pins) {
                    return false;
                }
            };

            map.overLaysLoad(Map.OVERLAYS.CAMERA,listener,8,Map.MAX_ZOOM_LEVEL);
        }

        // Event
        else if(item.getItemId() == R.id.action_event) {
            IPinListener listener = new IPinListener() {
                @Override
                public boolean onPinClick(Pin pin, Pin[] pins) {
                    Toast.makeText(MainActivity.this,pin.getTag(),Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onLongClickPin(Pin pin, Pin[] pins) {
                    return false;
                }

                @Override
                public boolean onPinDoubleClick(Pin pin, Pin[] pins) {
                    return false;
                }
            };

            map.overLaysLoad(Map.OVERLAYS.EVENT,listener,8,Map.MAX_ZOOM_LEVEL);
        }

        // Cache
        else if(item.getItemId() == R.id.action_clear_cache) {
            map.setCacheListener(new ICacheListener() {
                @Override
                public void onFinishClearCache(final long l) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"cache size: " + (l/1000000) + "MB",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFinishCheckCacheSize(long l) {

                }
            });

            map.clearCache(true);
        }

        // Advance
        else if(item.getItemId() == R.id.action_bound) {
            ArrayList<IMarker> pins = new ArrayList<>();
            pins.addAll(Arrays.asList(map.getAllPin()));
            map.bound(pins);
        }else if(item.getItemId() == R.id.action_compass) {
            map.setMode((short) (map.getMode() | Map.MODE_COMPASS));
        }


        else if(item.getItemId() == R.id.action_about) {
            Toast.makeText(MainActivity.this,"SDK version: " + Map.getVersion(),Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean hasLastPin() {
        if(lastPushedPin == null){
            Toast.makeText(this,"Push pin first",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean hasLastLine() {
        if(lastPushedLine == null){
            Toast.makeText(this,"Add line first",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /*
    private boolean hasLastPolygon() {
        if(lastPushedPolygon == null){
            Toast.makeText(this,"Add polygon first",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    */

    private Pin createPin() {
        return new Pin(map.getLocation(),this, R.drawable.pin,new PointF(0f,-0.5f));
    }

    private Line createLine() {
        MapLocation loc = map.getLocation();
        Line line = new Line(new MapLocation[]{loc,new MapLocation(loc.lon+0.05,loc.lat+0.05),new MapLocation(loc.lon+0.1,loc.lat-0.05)});
        line.setWidth(10);
        line.setColor(new float[]{1,0,0,1});
        return line;
    }

    private Polygon createPolygon(){
        MapLocation loc = map.getLocation();
        Polygon polygon = new Polygon(new MapLocation[]{loc,new MapLocation(loc.lon+0.05,loc.lat+0.05),new MapLocation(loc.lon+0.1,loc.lat+0.05),new MapLocation(loc.lon+0.1,loc.lat)});
        polygon.setColor(new float[]{0,1f,0,0.5f});
        return polygon;
    }

    @Override
    public void onMapCreated(Map map) {
        this.map = map;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Map.REQUEST_LOCATION_PERMISSION_REQUEST_CODE && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            map.getCurrentLocation();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if(map != null){
            map.setMode((short) (map.getMode() & ~Map.MODE_UPDATING_LOCATION));
            map.cancelGetCurrentLocation();
        }
        super.onPause();
    }
}
