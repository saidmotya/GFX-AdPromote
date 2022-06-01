package com.gfx.adPromote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.gfx.adPromote.Interfaces.OnYoutubeNativeListener;
import com.gfx.adPromote.Models.YoutubeModels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class NativeBannerYoutube extends FrameLayout {


    private final Context context;

    private String attrTitleButton = "Watch video"; //default title
    private int attrColorButton = Color.parseColor("#fb0c1a"); //default color
    //private int attrColorAd = Color.parseColor("#2196F3"); //default color
    private int attrColorBody = Color.WHITE;

    private ImageView videoPreview;
    private RelativeLayout playVideo, watchVideo;
    private TextView title, views, likeCounter, adColor;
    private AppCompatTextView watchVideo_txt;
    private RelativeLayout previewProgress;
    private LinearLayout bodyColor;


    private PromotePrefs promotePrefs;
    private ArrayList<YoutubeModels> youtubeList = new ArrayList<>();
    private OnYoutubeNativeListener onYoutubeNativeListener;

    public NativeBannerYoutube(Context context) {
        super(context);
        initView(null);
        this.context = context;
        initializeData();
    }

    public NativeBannerYoutube(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        this.context = context;
        initializeData();
    }

    public NativeBannerYoutube(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    public NativeBannerYoutube(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    private void initView(AttributeSet attrs) {

        //Load attributes
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NativeBannerYoutube, 0, 0);

        attrTitleButton = typedArray.getString(R.styleable.NativeBannerYoutube_nativeBanner_yt_titleButton);
                attrColorBody = typedArray.getColor(R.styleable.NativeBannerYoutube_nativeBanner_yt_bodyColor, attrColorBody);
        //can be color or shape from drawable :
        attrColorButton = typedArray.getColor(R.styleable.NativeBannerYoutube_nativeBanner_yt_buttonColor, attrColorButton);
        inflateBanner();

        typedArray.recycle();

    }

    private void inflateBanner() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_banner_native, this);
    }

    private void initializeData() {
        promotePrefs = new PromotePrefs(context);
        DatabaseYoutube databaseYoutube = new DatabaseYoutube(context);
        youtubeList = databaseYoutube.getAllYoutubeList();
    }

    private void applyUI() {

        if (watchVideo_txt != null && attrTitleButton != null) {
            watchVideo_txt.setText(attrTitleButton);
        }


        if (watchVideo != null) {
            watchVideo.setBackgroundColor(attrColorButton);

        }

        if (adColor != null) {
            //adColor.setBackgroundColor(attrColorAd);
            generateAdMark();
        }

        if (bodyColor != null) {
            bodyColor.setBackgroundColor(attrColorBody);
        }


    }

    private void generateAdMark() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(attrColorButton);

        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                0, 0, // Top Left
                0, 0, // Top Right
                0, 0, // Bottom Right
                15, 15});//Bottom left
        adColor.setBackground(gradientDrawable);
    }

    @SuppressLint("SetTextI18n")
    private void initializeInflateUI() {

        videoPreview = (ImageView) findViewById(R.id.video_preview);
        playVideo = (RelativeLayout) findViewById(R.id.play_video);
        watchVideo = (RelativeLayout) findViewById(R.id.watch_video);

        adColor = (TextView) findViewById(R.id.ad);

        title = (TextView) findViewById(R.id.title);
        views = (TextView) findViewById(R.id.views);
        likeCounter = (TextView) findViewById(R.id.like_counter);

        watchVideo_txt = (AppCompatTextView) findViewById(R.id.watch_video_txt);

        previewProgress = (RelativeLayout) findViewById(R.id.native_preview_progress);

        bodyColor = (LinearLayout) findViewById(R.id.body_color);


        applyUI();
    }



    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showPromoteNative();
    }

    public void showPromoteNative() {

        try {

            if (youtubeList != null && !youtubeList.isEmpty()) {
                int size = youtubeList.size();
                Random random = new Random();
                int nativeIndex = random.nextInt(size);
                buildNative(nativeIndex);
            } else {
                //the ad list is empty.
                if (onYoutubeNativeListener != null) {
                    onYoutubeNativeListener.onYoutubeNativeAdFailedToLoad("The YoutubeAd list is empty !");
                }
                setVisibility(GONE);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("SetTextI18n")
    private void buildNative(int index) {

        initializeInflateUI();

        String youtubeTitle = youtubeList.get(index).getTitle();
        String youtubeIcon = youtubeList.get(index).getIcon();

        String youtubePreviewFull = youtubeList.get(index).getPreviewFull();

        String youtubeVideoLink = youtubeList.get(index).getWatch();
        String youtubeSubscribeID = youtubeList.get(index).getChannelID();

        if (onYoutubeNativeListener != null) {
            onYoutubeNativeListener.onYoutubeNativeAdLoaded();
        }

        loadPreview(youtubePreviewFull);

        title.setText(youtubeTitle);

        //loaded from prefs :
        String viewsCounter = promotePrefs.getViewsCounter(index);
        String likesCounter = promotePrefs.getLikeCounter(index);

        views.setText(viewsCounter + " Views");
        likeCounter.setText(likesCounter);

        watchVideo_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onYoutubeNativeListener != null) {
                    onYoutubeNativeListener.onYoutubeNativeAdClicked("watching");
                }
                AppsConfig.youtubeWatchVideo(context, youtubeVideoLink);
            }
        });
        playVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYoutubeNativeListener != null) {
                    onYoutubeNativeListener.onYoutubeNativeAdClicked("play video");
                }
                AppsConfig.youtubeWatchVideo(context, youtubeVideoLink);
            }
        });


    }



    private void loadPreview(String previewFull) {

        previewProgress.setVisibility(VISIBLE);
        playVideo.setVisibility(GONE);

        Glide.with(context).load(previewFull)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewProgress.setVisibility(GONE);
                        playVideo.setVisibility(VISIBLE);
                        return false;
                    }
                })
                .into(videoPreview);


    }


    //Colors body :
    public void setButtonTitle(String title) {
        this.attrTitleButton = title;
        initializeInflateUI();
    }


    public void setButtonColor(int color) {
        try {
            this.attrColorButton = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setButtonColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrColorButton = Color.parseColor(color);
                initializeInflateUI();
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



    public void setColorBody(int color) {
        try {
            this.attrColorBody = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setColorBody(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrColorBody = Color.parseColor(color);
                initializeInflateUI();
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


    public void setOnYoutubeNativeListener(OnYoutubeNativeListener onYoutubeNativeListener) {
        this.onYoutubeNativeListener = onYoutubeNativeListener;
    }
}
