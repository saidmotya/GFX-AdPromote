package com.gfx.adPromote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gfx.adPromote.Adapters.AdapterPager;
import com.gfx.adPromote.Adapters.PagerTransformer;
import com.gfx.adPromote.Config.AppsConfig;
import com.gfx.adPromote.Helper.DataScreen;
import com.gfx.adPromote.Helper.DatabaseHelper;
import com.gfx.adPromote.Helper.PromotePrefs;
import com.gfx.adPromote.Interfaces.OnAdClosed;
import com.gfx.adPromote.Interfaces.OnInterstitialAdListener;
import com.gfx.adPromote.Interfaces.OnPagerScreenListener;
import com.gfx.adPromote.Models.AppModels;
import com.gfx.adPromote.Models.AppScreen;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class InterstitialPromote extends Dialog {


    private int attrRadiusButton = 20; //default color
    private int attrInstallColor = Color.parseColor("#2196F3"); //default color
    private String attrInstallTitle = "Install"; //default title


    private final Activity activity;
    private RelativeLayout close;
    private TextView closeCount, closeText;


    private RelativeLayout install;
    private TextView installTitle, ad;
    private TextView downloadAd, rateAd, rateCounter;
    private RelativeLayout previewProgress, iconProgress;
    private ViewPager viewPager;
    private SpringDotsIndicator springDotsIndicator;


    private CountDownTimer countDownTimer;
    private boolean isTimer;
    private int timer = 0;

    private ImageView preview, icon;
    private TextView name, description;
    private OnInterstitialAdListener onInterstitialAdListener;
    private OnAdClosed onAdClosed;

    private ArrayList<AppModels> adList = new ArrayList<>();
    private ArrayList<AppScreen> adScreen = new ArrayList<>();
    private PromotePrefs promotePrefs;

    //default style :
    public InterstitialPromote(@NonNull Activity activity) {
        super(activity, R.style.InterstitialStyle);
        this.activity = activity;
        setContentView(R.layout.promote_interstitial);
        promotePrefs = new PromotePrefs(activity.getApplicationContext());
        setCancelable(false);
        initializeData();
        initializeUI();
    }

    private void initializeData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        adList = databaseHelper.getAllApps();
        //initialize the screen shot
        DataScreen dataScreen = new DataScreen(activity.getApplicationContext());
        adScreen = dataScreen.getAllScreen();
        onLoadAdListener();
    }

    private void onLoadAdListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adList != null && !adList.isEmpty()) {

                    if (onInterstitialAdListener != null) {
                        onInterstitialAdListener.onInterstitialAdLoaded();
                    }
                } else {
                    if (onInterstitialAdListener != null) {
                        onInterstitialAdListener.onInterstitialAdFailedToLoad("Ad Loaded, but data base of ad wrong ! please check your file.");
                    }
                }
            }
        }, 500);
    }


    private void initializeUI() {
        close = findViewById(R.id.interstitial_close);
        closeCount = findViewById(R.id.closeCount);
        closeText = findViewById(R.id.closeText);
        install = findViewById(R.id.interstitial_install);
        installTitle = findViewById(R.id.interstitial_install_txt);
        ad = findViewById(R.id.ad);

        downloadAd = findViewById(R.id.downloadAd);
        rateAd = findViewById(R.id.rateAd);
        rateCounter = findViewById(R.id.rate_counter);

        viewPager = findViewById(R.id.viewpager);
        springDotsIndicator = findViewById(R.id.dots_indicator);

        previewProgress = (RelativeLayout) findViewById(R.id.interstitial_preview_progress);
        iconProgress = (RelativeLayout) findViewById(R.id.inter_icon_progress);

        //change data :
        preview = findViewById(R.id.interstitial_preview);
        icon = findViewById(R.id.inter_icons);
        name = findViewById(R.id.interstitial_app_name);
        description = findViewById(R.id.interstitial_short_description);

        generateInstallButton();
        updateInstallTitle();
    }

    private void startCountDown() {
        if (timer != 0) {
            close.setClickable(false);
            closeText.setAlpha(0.5f);
            closeCount.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(1000L * timer, 1000) {
                public void onTick(long j) {
                    String count = String.valueOf(j / 1000);
                    closeCount.setText(count);
                    isTimer = true;
                }

                public void onFinish() {
                    isTimer = false;
                    //closeCount.setText(" ");
                    closeCount.setVisibility(View.INVISIBLE);
                    closeText.setAlpha(1f);
                    close.setClickable(true);
                    timerCancel();
                }
            }.start();
        }
    }

    private void applyStyle(int style) {
        switch (style) {
            case 1: //style 1 : Advances
                setContentView(R.layout.promote_interstitial_2);
                break;
            case 2: //style 2 : Standard
                setContentView(R.layout.promote_interstitial);
                break;

            default:
                setContentView(R.layout.promote_interstitial);
                break;

        }
    }


    private void timerCancel() {
        try {
            if (isTimer) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //check the Ad List is not empty
    public boolean isAdLoaded() {
        if (adList != null) {
            return !adList.isEmpty();
        }
        //the ad list is empty.
        if (onInterstitialAdListener != null) {
            onInterstitialAdListener.onInterstitialAdFailedToLoad("Failed to show : No Ad");
        }
        return false;

    }

    @Override
    public void show() {
        super.show();
        buildInterstitial();
    }

    private void dismissAd() {
        try {
            if (isShowing()) {
                if (onAdClosed != null) {
                    onAdClosed.onAdClosed();
                }
                dismiss();
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("The Interstitial was dismiss because something wrong !");
                }
            }
        } catch (Exception e) {
            cancel();
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Interstitial Dismiss methode caused a crush : " + e.getMessage());
            }
        }
    }

    private void updateInstallTitle() {
        installTitle.setText(attrInstallTitle);
    }


    private void initializePager(int index) {

        try {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int paddingLeftRight = width / 4;
            viewPager.setPadding(paddingLeftRight, 0, paddingLeftRight, 0);
            viewPager.setPageTransformer(false, new PagerTransformer());

            AdapterPager adapterPager = new AdapterPager(activity.getApplicationContext(), screenShot(index));
            viewPager.setAdapter(adapterPager);
            springDotsIndicator.setViewPager(viewPager);
            adapterPager.setOnPagerScreenListener(new OnPagerScreenListener() {
                @Override
                public void onPagerScreenListenerLoaded() {
                    if (BuildConfig.DEBUG) {
                        AppsConfig.setLog("Screen loaded.");
                    }
                }

                @Override
                public void onPagerScreenListenerFailed(String error) {
                    if (BuildConfig.DEBUG) {
                        AppsConfig.setLog("Screen failed to load : " + error);
                    }
                }
            });

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Failed to initialize ViewPager of screen.");
            }
        }


    }


    private ArrayList<String> screenShot(int index) {
        ArrayList<String> screenList = new ArrayList<>();
        if (adScreen != null && !adScreen.isEmpty()) {
            String array = adScreen.get(index).getScreen();
            List<String> arrayList = new ArrayList<String>(Arrays.asList(array.split(",")));
            screenList.addAll(arrayList);
            /*
            for (int i = 0; i < arrayList.size(); i++){
                //screenList.add(arrayList.get(i));
            }
             */

        } else {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("dataBase of screen is empty : return to 0.");
            }
        }
        return screenList;
    }


    private void buildInterstitial() {

        try {

            if (adList != null && !adList.isEmpty()) {
                int size = adList.size();
                Random random = new Random();
                int interstitialIndex = random.nextInt(size);
                updateInterstitial(interstitialIndex);
                initializePager(interstitialIndex);
                startCountDown();
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Build Interstitial...");
                }
            } else {
                //the ad list is empty.
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdFailedToLoad("The Ad list is empty ! please check your json file.");
                }
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Failed to build Interstitial cause : the List of ad is empty, please check your connection first, than check your file json.");
                }

                dismissAd();
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Failed to build Interstitial cause : " + e.getMessage());
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateInterstitial(int index) {

        String data_name = adList.get(index).getName();
        String data_icons = adList.get(index).getIcons();
        String data_preview = adList.get(index).getAppPreview();
        String data_packageName = adList.get(index).getPackageName();
        String data_description = adList.get(index).getShortDescription();

        loadIcon(data_icons);
        loadPreview(data_preview);
        name.setText(data_name);
        description.setText(data_description);


        //loaded from prefs :
        downloadAd.setText("+ " + promotePrefs.getDownloadCounter(index) + " Installs");
        rateAd.setText(String.valueOf(promotePrefs.getRateCounter(index)));
        rateCounter.setText(promotePrefs.getRatePeople(index));

        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdClicked();
                }
                dismiss();
                AppsConfig.openAdLink(activity.getApplicationContext(), data_packageName);
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Interstitial Clicked.");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInterstitialAdListener != null) {
                    onInterstitialAdListener.onInterstitialAdClosed();
                }
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Interstitial Closed.");
                }
                dismissAd();


            }
        });

    }

    private void loadIcon(String icons) {
        iconProgress.setVisibility(View.VISIBLE);
        Glide.with(activity.getApplicationContext()).load(icons)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial icon failed to load : " + e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        iconProgress.setVisibility(View.GONE);
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial icon loaded.");
                        }
                        return false;
                    }
                })
                .into(icon);
    }

    private void loadPreview(String previewIcon) {
        previewProgress.setVisibility(View.VISIBLE);
        Glide.with(activity.getApplicationContext()).load(previewIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial preview failed to load : " + e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewProgress.setVisibility(View.GONE);
                        if (BuildConfig.DEBUG) {
                            AppsConfig.setLog("Interstitial preview loaded.");
                        }
                        return false;
                    }
                })
                .into(preview);
    }


    private void generateInstallButton() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(attrInstallColor);
        ad.setBackgroundColor(attrInstallColor);
        springDotsIndicator.setDotIndicatorColor(attrInstallColor);
        springDotsIndicator.setStrokeDotsIndicatorColor(attrInstallColor);


        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        install.setBackground(gradientDrawable);
        generateInstallClose();
    }

    private void generateInstallClose() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#E4E4E4"));
        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        close.setBackground(gradientDrawable);
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void setStyle(int style) {
        applyStyle(style);
        initializeUI();
    }

    public void setRadiusButton(int radius) {
        this.attrRadiusButton = radius;
        generateInstallButton();
    }

    public void setInstallTitle(String title) {
        this.attrInstallTitle = title;
        updateInstallTitle();
    }


    public void setInstallColor(int color) {
        try {
            this.attrInstallColor = activity.getResources().getColor(color);
            generateInstallButton();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setInstallColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrInstallColor = Color.parseColor(color);
                generateInstallButton();
            } else {
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("The value of color wrong : forget the symbol #");
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from string");
            }
        }

    }


    public void setOnInterstitialAdListener(OnInterstitialAdListener onInterstitialAdListener) {
        this.onInterstitialAdListener = onInterstitialAdListener;
    }

    public void setOnAdClosed(OnAdClosed onAdClosed) {
        this.onAdClosed = onAdClosed;
    }
}
