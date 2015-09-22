package com.jcb.instalist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcb.instaapp.InstagramApp;
import com.jcb.instaapp.InstagramFetch;
import com.jcb.instalist.cache.ImageCache;
import com.jcb.instalist.cache.ImageFetcher;
import com.jcb.instalist.ui.AppPagerAdapter;


public class MainActivity extends FragmentActivity {

    private static final String IMAGE_CACHE_DIR = "instagram_thumbs";
    private InstagramApp mApp;
    private InstagramFetch mFetch;
    private ImageFetcher mImageFetcher;
    private int mImageThumbSize;
    private Button btnConnect;
    private Button btnFetch;
    private TextView tvSummary;
    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
        }

        @Override
        public void onFail(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };
    private ViewPager vwPager;
    InstagramFetch.InstagramFetchListener fetchListener = new InstagramFetch.InstagramFetchListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

            showViewPagerDetails();
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

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnFetch = (Button) findViewById(R.id.btnFetch);

        vwPager = (ViewPager) findViewById(R.id.viewPager);

        btnConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                doOnBtnConnectClick();
            }
        });

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFetch.fetchFromInstagram();

            }
        });

        if (mApp.hasAccessToken()) {
            tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
        }


    }

    private void doOnBtnConnectClick() {

        if (mApp.hasAccessToken()) {

            ApplicationData.printLog("Has AccessToken: " + mApp.getId());
            ApplicationData.printLog("Has AccessToken: " + mApp.getName());
            ApplicationData.printLog("Has AccessToken: " + mApp.getUserName());
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
                                    tvSummary.setText("Not connected");
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
            ApplicationData.printLog("Has No AccessToken");

            mApp.authorize();
        }
    }

    private void showViewPagerDetails() {

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(MainActivity.this, IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory


//        RelativeLayout headerLayout = (RelativeLayout) findviewbyid(R.id.headerLayout);
        ViewTreeObserver observer = btnConnect.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                InstagramUtils.showLog(btnConnect.getWidth());
                setWidthValue(btnConnect.getWidth());
                btnConnect.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });


        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(MainActivity.this, getWidthValue());
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(MainActivity.this.getSupportFragmentManager(), cacheParams);

        vwPager.setAdapter(new AppPagerAdapter(getSupportFragmentManager(), mImageFetcher));
    }

    public int getWidthValue() {
        return mImageThumbSize;
    }

    public void setWidthValue(int value) {
        mImageThumbSize = value;
    }


    @Override
    public void onPause() {
        super.onPause();

        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();

    }
}