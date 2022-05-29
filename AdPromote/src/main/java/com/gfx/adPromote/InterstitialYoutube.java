package com.gfx.adPromote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gfx.adPromote.Config.AppsConfig;
import com.gfx.adPromote.Helper.DatabaseYoutube;
import com.gfx.adPromote.Helper.PromotePrefs;
import com.gfx.adPromote.Interfaces.OnAdClosed;
import com.gfx.adPromote.Interfaces.OnYoutubeInterstitialListener;
import com.gfx.adPromote.Models.YoutubeModels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class InterstitialYoutube extends Dialog {


    private int attrRadiusButton = 10; //default color

    private int openAdColorNormal = Color.parseColor("#F10101"); //default color
    private int openAdColorAdvance = Color.parseColor("#2196F3"); //default color

    private int openAdTitleColorAdvance = Color.WHITE; //default color
    private int openAdTitleColorNormal = Color.WHITE; //default color
    private String openAdTitle = "Open Ad";

    private int descriptionTitleColor = Color.parseColor("#C60000"); //default color
    private int descriptionColor = Color.parseColor("#252525"); //default color

    private String descriptionTitleText = getContext().getString(R.string.youtube_greeting);
    private String descriptionText = getContext().getString(R.string.youtube_long_description); //default color


    private final Activity activity;
    private RelativeLayout closeAd;
    private TextView closeTimer,adMark;
    private ImageView icClose;

    private TextView title, views, likeCounter;
    private AppCompatTextView subscribeText, descriptionTitle, subscribeButton;
    private TextView description;


    private LinearLayout openAd;
    private TextView openAdText;

    private ImageView icon, videoPreview;
    private RelativeLayout playVideo;
    private RelativeLayout iconProgress, previewProgress;


    private final int style;

    private CountDownTimer countDownTimer;
    private boolean isTimer;
    private int timer = 0;

    private int counter = 0;
    private final int waited = 100;
    private ImageView icPlay;
    private TextView txtCounter;
    private Thread thread;
    private final Handler handler = new Handler();
    private ProgressBar progressBar;


    private OnYoutubeInterstitialListener onYoutubeInterstitialListener;
    private OnAdClosed onAdClosed;

    private ArrayList<YoutubeModels> youtubeList = new ArrayList<>();
    private final PromotePrefs promotePrefs;


    public InterstitialYoutube(@NonNull Activity activity, int style) {
        super(activity, R.style.InterstitialStyle);
        this.activity = activity;
        this.style = style;
        applyStyle(style);
        promotePrefs = new PromotePrefs(activity.getApplicationContext());
        setCancelable(false);
        initializeData();
        initializeUI();
    }

    private void initializeUI() {

        closeAd = findViewById(R.id.closeAd);//
        closeTimer = findViewById(R.id.count_close);//
        icClose = findViewById(R.id.ic_close);//
        openAd = findViewById(R.id.openAd);//
        openAdText = findViewById(R.id.openAdText);//
        adMark = findViewById(R.id.ad);

        title = findViewById(R.id.title);
        views = findViewById(R.id.views);
        likeCounter = findViewById(R.id.like_counter);
        subscribeText = findViewById(R.id.subscribeText);

        descriptionTitle = findViewById(R.id.descriptionTitle);
        description = findViewById(R.id.description);
        subscribeButton = findViewById(R.id.subscribeButton);

        icPlay = findViewById(R.id.ic_play);
        txtCounter = findViewById(R.id.ic_play_counter);
        progressBar = findViewById(R.id.ic_play_progress);

        initializePlayer();
        applyUI();

    }


    private void initializePlayer() {
        icon = findViewById(R.id.icon);
        videoPreview = findViewById(R.id.preview);
        iconProgress = findViewById(R.id.icon_progress);
        previewProgress = findViewById(R.id.preview_progress);
        playVideo = findViewById(R.id.play_video);

        findViewById(R.id.seek_play).setEnabled(false);
        findViewById(R.id.seekVol).setEnabled(false);
    }

    private boolean isStyleNormal() {
        return style == YoutubeStyle.Normal;
    }

    private void applyUI() {

        if (isStyleNormal()) {
            //update Colors coming for this attrs...
            openAdText.setTextColor(openAdTitleColorNormal);
            generateOpenAdButton(openAdColorNormal);
            generateAdMark(openAdColorNormal);
        } else {
            openAdText.setTextColor(openAdTitleColorAdvance);
            generateOpenAdButton(openAdColorAdvance);
            generateAdMark(openAdColorAdvance);
            descriptionTitle.setTextColor(descriptionTitleColor);
        }
        openAdText.setText(openAdTitle);
        description.setTextColor(descriptionColor);
        descriptionTitle.setText(descriptionTitleText);
        description.setText(descriptionText);
    }


    private void generateOpenAdButton(int color) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);

        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        openAd.setBackground(gradientDrawable);
    }

    private void generateAdMark(int color) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);

        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                0, 0, // Top Left
                0, 0, // Top Right
                20, 20, // Bottom Right
                0, 0});//Bottom left
        adMark.setBackground(gradientDrawable);
    }

    private void initializeData() {
        DatabaseYoutube databaseYoutube = new DatabaseYoutube(activity.getApplicationContext());
        youtubeList = databaseYoutube.getAllYoutubeList();
        onLoadAdListener();
    }

    private void onLoadAdListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (youtubeList != null && !youtubeList.isEmpty()) {

                    if (onYoutubeInterstitialListener != null) {
                        onYoutubeInterstitialListener.onYoutubeInterstitialLoaded();
                    }
                } else {
                    if (onYoutubeInterstitialListener != null) {
                        onYoutubeInterstitialListener.onYoutubeInterstitialFailedToLoad("Ad Loaded, but data base of ad wrong ! please check your file.");
                    }
                }
            }
        }, 500);
    }


    private void startCountDown() {
        if (timer != 0) {


            closeAd.setClickable(false);
            closeTimer.setVisibility(View.VISIBLE);
            icClose.setVisibility(View.GONE);
            
            countDownTimer = new CountDownTimer(1000L * timer, 1000) {
                public void onTick(long j) {
                    String count = String.valueOf(j / 1000);
                    closeTimer.setText(count);
                    isTimer = true;
                }

                public void onFinish() {
                    isTimer = false;
                    closeTimer.setVisibility(View.GONE);
                    icClose.setVisibility(View.VISIBLE);
                    closeAd.setClickable(true);
                    timerCancel();
                }
            }.start();
        } else {
            closeTimer.setVisibility(View.GONE);
            icClose.setVisibility(View.VISIBLE);
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

    public void setTimer(int timer) {
        this.timer = timer;
    }


    private void applyStyle(int style) {
        switch (style) {
            case 1: //style 1 : Advances
                setContentView(R.layout.youtube_interstitial_normal);
                break;
            case 2: //style 2 : Standard
                setContentView(R.layout.youtube_interstitial_advance);
                break;
        }
    }

    //check the Ad List is not empty
    public boolean isAdLoaded() {
        if (youtubeList != null) {
            return !youtubeList.isEmpty();
        }
        //the ad list is empty.
        if (onYoutubeInterstitialListener != null) {
            onYoutubeInterstitialListener.onYoutubeInterstitialFailedToLoad("Failed to show : No Ad");
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
                    AppsConfig.setLog("The Interstitial was dismiss.");
                }
            }
        } catch (Exception e) {
            cancel();
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("Interstitial Dismiss methode caused a crush : " + e.getMessage());
            }
        }
    }


    private void buildInterstitial() {

        try {

            if (youtubeList != null && !youtubeList.isEmpty()) {
                int size = youtubeList.size();
                Random random = new Random();
                int interstitialIndex = random.nextInt(size);
                updateInterstitial(interstitialIndex);
                startCountDown();
                if (BuildConfig.DEBUG) {
                    AppsConfig.setLog("Build Interstitial...");
                }
            } else {
                //the ad list is empty.
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialFailedToLoad("The Ad list is empty ! please check your json file.");
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


        String youtubeTitle = youtubeList.get(index).getTitle();
        String youtubeIcon = youtubeList.get(index).getIcon();
        String youtubePreview = youtubeList.get(index).getPreview();

        String youtubeVideoLink = youtubeList.get(index).getWatch();
        String youtubeSubscribeID = youtubeList.get(index).getChannelID();

        if (onYoutubeInterstitialListener != null) {
            onYoutubeInterstitialListener.onYoutubeInterstitialLoaded();
        }

        loadIcon(youtubeIcon);
        loadPreview(youtubePreview);

        title.setText(youtubeTitle);

        //loaded from prefs :
        String viewsCounter = promotePrefs.getViewsCounter(index);
        String likesCounter = promotePrefs.getLikeCounter(index);

        views.setText(viewsCounter + " views");
        if (isStyleNormal()) {
            likeCounter.setText(likesCounter + " likes this video");
        } else {
            likeCounter.setText(likesCounter);
        }

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialClicked("play video");
                }
                AppsConfig.youtubeWatchVideo(activity, youtubeVideoLink);
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialClicked("subscribe button.");
                }
                AppsConfig.youtubeSubscribeChannel(activity, youtubeSubscribeID);
            }
        });


        subscribeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialClicked("subscribe Text.");
                }
                AppsConfig.youtubeSubscribeChannel(activity, youtubeSubscribeID);
            }
        });

        openAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialClicked("OpenAd");
                }
                AppsConfig.youtubeWatchVideo(activity, youtubeVideoLink);
            }
        });

        closeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYoutubeInterstitialListener != null) {
                    onYoutubeInterstitialListener.onYoutubeInterstitialClosed();
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
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        iconProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(icon);
    }

    private void loadPreview(String preview) {
        previewProgress.setVisibility(View.VISIBLE);
        Glide.with(activity.getApplicationContext()).load(preview)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewProgress.setVisibility(View.GONE);
                        preloadCounter();
                        return false;
                    }
                })
                .into(videoPreview);
    }


    //make simple preload counter :
    private void preloadCounter() {

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (counter < 100) {
                        counter += 8;
                        handler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                txtCounter.setText(counter + " %");
                                progressBar.setProgress(counter);
                            }
                        });
                        sleep(waited);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtCounter.setVisibility(View.GONE);
                                icPlay.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                playVideo.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.bounce));
                            }
                        });
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }

        };
        thread.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {

            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOpenAdTitle(String title) {
        this.openAdTitle = title;
        applyUI();
    }

    public void setOpenAdColor(int color) {
        try {
            if (isStyleNormal()) {
                this.openAdColorNormal = activity.getResources().getColor(color);
            } else {
                this.openAdColorAdvance = activity.getResources().getColor(color);
            }

            applyUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setOpenAdColor(String color) {
        try {
            if (color.startsWith("#")) {
                if (isStyleNormal()) {
                    this.openAdColorNormal = Color.parseColor(color);
                } else {
                    this.openAdColorAdvance = Color.parseColor(color);
                }

                applyUI();
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

    public void setOpenAdTitleColor(int color) {
        try {
            if (isStyleNormal()) {
                this.openAdTitleColorNormal = activity.getResources().getColor(color);
            } else {
                this.openAdTitleColorAdvance = activity.getResources().getColor(color);
            }
            applyUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setOpenAdTitleColor(String color) {
        try {
            if (color.startsWith("#")) {
                if (isStyleNormal()) {
                    this.openAdTitleColorNormal = Color.parseColor(color);
                } else {
                    this.openAdTitleColorAdvance = Color.parseColor(color);
                }

                applyUI();
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

    public void setRadiusButtonOpenAd(int radius) {
        this.attrRadiusButton = radius;
        if (isStyleNormal()) {
            generateOpenAdButton(openAdColorNormal);
        } else {
            generateOpenAdButton(openAdColorAdvance);
        }

    }

    public void setDescriptionTitle(String title) {
        this.descriptionTitleText = title;
        applyUI();
    }

    public void setDescriptionText(String title) {
        this.descriptionText = title;
        applyUI();
    }

    public void setDescriptionTitleColor(int color) {
        try {
            this.descriptionTitleColor = activity.getResources().getColor(color);
            applyUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setDescriptionTitleColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.descriptionTitleColor = Color.parseColor(color);
                applyUI();
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

    public void setDescriptionColor(int color) {
        try {
            this.descriptionColor = activity.getResources().getColor(color);
            applyUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setDescriptionColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.descriptionColor = Color.parseColor(color);
                applyUI();
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


    public void setOnYoutubeInterstitialListener(OnYoutubeInterstitialListener onYoutubeInterstitialListener) {
        this.onYoutubeInterstitialListener = onYoutubeInterstitialListener;
    }

    public void setOnAdClosed(OnAdClosed onAdClosed) {
        this.onAdClosed = onAdClosed;
    }
}
