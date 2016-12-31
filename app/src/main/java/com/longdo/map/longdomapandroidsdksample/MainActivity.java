package com.longdo.map.longdomapandroidsdksample;

import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.longdo.api.IMapListener;
import com.longdo.api.Map;
import com.longdo.api.MapGLSurfaceView;
import com.longdo.api.Pin;

public class MainActivity extends AppCompatActivity implements IMapListener{

    //UI references
    private MapGLSurfaceView mapGlSurfaceView;
    private Map map;

    private Pin lastPushedPin;

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
        }

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

    private Pin createPin() {
        return new Pin(map.getLocation(),this, R.drawable.pin,new PointF(0f,-0.5f));
    }

    @Override
    public void onMapCreated(Map map) {
        this.map = map;
    }
}
