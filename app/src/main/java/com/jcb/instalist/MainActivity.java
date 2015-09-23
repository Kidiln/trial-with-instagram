package com.jcb.instalist;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcb.instaapp.InstagramApp;
import com.jcb.instaapp.InstagramFetch;
import com.jcb.instalist.ui.AppPagerAdapter;

/**
 * MainActivity used to show the Main screen.
 * The Conncect button and pager is shown in Main screen.
 */
public class MainActivity extends FragmentActivity {

    private InstagramApp mApp;
    private InstagramFetch mFetch;
    private boolean isPageVideo = false;

    /**
     * Receiver for handling online data fetch.
     */
    BroadcastReceiver refreshObserver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            isPageVideo = intent.getBooleanExtra(ApplicationData.INTENT_ISVIDEO, false);
            if (InstagramUtils.isNetworkAvailable(MainActivity.this)) {
                mFetch.fetchFromInstagram(true);
            }

        }
    };
    private Button btnConnect;
    private TextView tvSummary;

    /**
     * Listener for handling Instagram Authentication status. If success, media fetch is called.
     */
    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
            if (InstagramUtils.isNetworkAvailable(MainActivity.this)) {
                mFetch.fetchFromInstagram(false);
            }

        }

        @Override
        public void onFail(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };
    private ViewPager vwPager;

    /**
     * Listener for handling media fetch. If success, viewpager is updated.
     */
    InstagramFetch.InstagramFetchListener fetchListener = new InstagramFetch.InstagramFetchListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            vwPager.setAdapter(new AppPagerAdapter(getSupportFragmentManager()));
            vwPager.setCurrentItem(isPageVideo ? 1 : 0);

        }

        @Override
        public void onFail(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mFetch = new InstagramFetch(this);

        mApp.setListener(listener);

        mFetch.setListener(fetchListener);

        tvSummary = (TextView) findViewById(R.id.tvSummary);
        final TextView txthelper = (TextView) findViewById(R.id.txtHelper);

        // Used to find the the length of the width of the screen. // Can be optimised.
        ViewTreeObserver observer = txthelper.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                InstagramUtils.showLog(txthelper.getWidth());
                ApplicationData.widthScreenInPixels = txthelper.getWidth();

                txthelper.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });

        btnConnect = (Button) findViewById(R.id.btnConnect);

        vwPager = (ViewPager) findViewById(R.id.viewPager);

        btnConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                doOnBtnConnectClick();
            }
        });

        if (mApp.hasAccessToken()) {
            tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
        }

        vwPager.setAdapter(new AppPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(refreshObserver, new IntentFilter(ApplicationData.INTENT_REFRESH));

    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(refreshObserver);
    }


    /**
     * Handle action for connect button click. fetch and clear Oauth token accordingly
     */
    private void doOnBtnConnectClick() {

        if (mApp.hasAccessToken()) {

            InstagramUtils.showLog("Has AccessToken: " + mApp.getId());
            InstagramUtils.showLog("Has AccessToken: " + mApp.getName());
            InstagramUtils.showLog("Has AccessToken: " + mApp.getUserName());
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    mApp.resetAccessToken();
                                    btnConnect.setText("Connect");
                                    tvSummary.setText(getResources().getString(R.string.press_connect));
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            InstagramUtils.showLog("Has No AccessToken");
            if (InstagramUtils.isNetworkAvailable(MainActivity.this)) {
                mApp.authorize();
            } else {
                InstagramUtils.showToast(MainActivity.this, getResources().getString(R.string.no_network_connection_toast));
            }
        }
    }

}