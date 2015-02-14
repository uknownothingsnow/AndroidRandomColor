package com.github.lzyzsd.androidrandomcolor;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;


public class RandomUseNameActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_use_name);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        RandomColor randomColor = new RandomColor();
//        for (RandomColor.Color color : RandomColor.Color.values()) {
//            if (color == RandomColor.Color.MONOCHROME) {
//                continue;
//            }
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText("randomColor.random(" + Color.RED + ", 15)");
            textView.setTextSize(18);
            textView.setPadding(15, 6, 15, 6);
            container.addView(textView);
            FlowLayout flowLayout = new FlowLayout(this);
            flowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int[] colors = randomColor.random(RandomColor.Color.RED, 15);
            for (int i = 0; i < colors.length; i++) {
                RoundView roundView = new RoundView(this);
                roundView.setLayoutParams(new FlowLayout.LayoutParams(180, 180));
                roundView.setPadding(20, 20, 20, 20);
                int c = colors[i];
                Log.d("RandomUseNameActivity", "color: " + c);
                roundView.setRoundColor(c);
                flowLayout.addView(roundView);
            }
            container.addView(flowLayout);
        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_random_use_name, menu);
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
}
