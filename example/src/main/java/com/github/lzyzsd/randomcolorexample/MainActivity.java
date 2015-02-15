package com.github.lzyzsd.randomcolorexample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lzyzsd.randomcolor.RandomColor;

import org.apmem.tools.layouts.FlowLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout container = (FlowLayout) findViewById(R.id.container);
        RandomColor randomColor = new RandomColor();
        int[] colors = randomColor.randomColor(100);
        for (int i = 0; i < colors.length; i++) {
            RoundView roundView = new RoundView(this);
            roundView.setLayoutParams(new FlowLayout.LayoutParams(180, 180));
            roundView.setPadding(20, 20, 20, 20);
            int color = colors[i];
            Log.d("RandomColor", "color: " + color);
            roundView.setRoundColor(color);
            container.addView(roundView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_random_name) {
            startActivity(new Intent(this, RandomUseNameActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
