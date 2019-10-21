package com.asha.android.androidhandlerapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private BasicWorkerThread mBasicWorkerThread;
    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class UIHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public UIHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                // ...
            }
        }
    }

    private final UIHandler mUiHandler = new UIHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mBasicWorkerThread = new BasicWorkerThread("My worker thread");
        Runnable mRunnableTask = new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<4;i++){
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==2){
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,
                                        "I am at the middle of background task",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                }
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Background task is completed",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        };
        mBasicWorkerThread.start();
        mBasicWorkerThread.prepareHandler();
        mBasicWorkerThread.postTask(mRunnableTask);
        mBasicWorkerThread.postTask(mRunnableTask);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mBasicWorkerThread.quit();
        super.onDestroy();
    }
}
