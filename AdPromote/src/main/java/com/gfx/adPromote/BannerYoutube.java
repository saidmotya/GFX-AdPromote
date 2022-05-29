package com.gfx.adPromote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.gfx.adPromote.Interfaces.OnYoutubeBannerListener;
import com.gfx.adPromote.Models.YoutubeModels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class BannerYoutube extends FrameLayout {


    private final Context context;

    private final int ANIMATION_SPEED = 3; //time on second to change  Text banner.
    private String attrTitleButton = getContext().getString(R.string.subscribe); //default title
    private String attrDescriptionText = getContext().getString(R.string.youtube_description); //default description
    private int attrColorButton = Color.parseColor("#A30101"); //default color
    private int attrColorTitle = Color.WHITE; //default color
    private int contentColor = Color.WHITE; //default color
    private int attrBodyColor = Color.parseColor("#f30202"); //default color ff0000
    private int attrIconsColor = Color.parseColor("#FFF1F1F1"); //default color


    private FrameLayout bannerBody;
    private ImageView icons;
    private RelativeLayout iconProgress;
    private TextView videoName;
    private TextView videoViews, videoLikes;
    private ImageView icEye, icLike;
    private View line;

    private RelativeLayout subscribe;
    private TextView subscribeText;
    private TextView description, onYoutubeText;
    private ImageView icYoutube;
    private AppCompatTextView icYoutubeText;
    private TextView adText;

    private LinearLayout content1, content2;

    private final Handler handler = new Handler();
    private ArrayList<YoutubeModels> youtubeList = new ArrayList<>();
    private PromotePrefs promotePrefs;
    private OnYoutubeBannerListener onYoutubeBannerListener;

    public BannerYoutube(Context context) {
        super(context);
        initView(null);
        this.context = context;
        initializeData();
    }

    public BannerYoutube(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        this.context = context;
        initializeData();
    }

    public BannerYoutube(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    public BannerYoutube(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
        this.context = context;
        initializeData();

    }


    private void initView(AttributeSet attrs) {

        //Load attributes
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BannerYoutube, 0, 0);

        attrTitleButton = typedArray.getString(R.styleable.BannerYoutube_banner_yt_titleButton);
        attrDescriptionText = typedArray.getString(R.styleable.BannerYoutube_banner_yt_description);
        attrColorButton = typedArray.getColor(R.styleable.BannerYoutube_banner_yt_buttonColor, attrColorButton);
        attrColorTitle = typedArray.getColor(R.styleable.BannerYoutube_banner_yt_titleColor, attrColorTitle);
        contentColor = typedArray.getColor(R.styleable.BannerYoutube_banner_yt_contentColor, contentColor);
        attrBodyColor = typedArray.getColor(R.styleable.BannerYoutube_banner_yt_bodyColor, attrBodyColor);
        attrIconsColor = typedArray.getColor(R.styleable.BannerYoutube_banner_yt_IconsColor, attrIconsColor);

        inflateBanner();

        typedArray.recycle();

    }

    private void inflateBanner() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_banner, this);
    }

    @SuppressLint("SetTextI18n")
    private void initializeInflateUI() {

        bannerBody = (FrameLayout) findViewById(R.id.banner_body);
        icons = (ImageView) findViewById(R.id.icons);
        iconProgress = (RelativeLayout) findViewById(R.id.banner_progress);

        videoName = (TextView) findViewById(R.id.video_name);
        videoViews = (TextView) findViewById(R.id.views);
        videoLikes = (TextView) findViewById(R.id.like_counter);

        icEye = (ImageView) findViewById(R.id.ic_eye);
        icLike = (ImageView) findViewById(R.id.ic_like);
        line = (View) findViewById(R.id.line);

        icYoutubeText = (AppCompatTextView) findViewById(R.id.ic_youtubeText);

        subscribe = (RelativeLayout) findViewById(R.id.subscribe);
        subscribeText = (TextView) findViewById(R.id.subscribe_txt);

        adText = (TextView) findViewById(R.id.ad);

        content1 = (LinearLayout) findViewById(R.id.content_1);
        content2 = (LinearLayout) findViewById(R.id.content_2);

        description = (TextView) findViewById(R.id.description);
        onYoutubeText = (TextView) findViewById(R.id.onYoutube);
        icYoutube = (ImageView) findViewById(R.id.ic_youtube);

        applyUI();
    }


    private void initializeData() {
        promotePrefs = new PromotePrefs(context);
        DatabaseYoutube databaseYoutube = new DatabaseYoutube(context);
        youtubeList = databaseYoutube.getAllYoutubeList();
    }


    private void applyUI() {
        if (subscribeText != null && attrTitleButton != null) {
            subscribeText.setText(attrTitleButton);
        }
        if (description != null && attrDescriptionText != null) {
            description.setText(attrDescriptionText);
        }

        if (subscribe != null) {
            subscribe.setBackgroundColor(attrColorButton);
            //adText.setBackgroundColor(attrColorButton);
            generateAdMark();

        }

        if (subscribeText != null) {
            subscribeText.setTextColor(attrColorTitle);
            adText.setTextColor(attrColorTitle);
        }


        if (videoName != null) {
            videoName.setTextColor(contentColor);
            onYoutubeText.setTextColor(contentColor);
        }
        if (description != null) {
            description.setTextColor(contentColor);

        }


        if (videoLikes != null && videoViews != null) {
            videoViews.setTextColor(contentColor);
            videoLikes.setTextColor(contentColor);
        }

        if (icEye != null && icLike != null && icYoutubeText != null) {

            ColorStateList colorStateList = new ColorStateList(new int[][]{{}}, new int[]{attrIconsColor});
            icEye.setBackgroundTintList(colorStateList);
            icLike.setBackgroundTintList(colorStateList);
            //update youtube icon color with text :
            icYoutubeText.setTextColor(attrIconsColor);
            line.setBackgroundColor(attrIconsColor);
            icYoutube.setBackgroundTintList(colorStateList);

        }

        if (bannerBody != null) {
            bannerBody.setBackgroundColor(attrBodyColor);

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
                10, 10, // Bottom Right
                0, 0});//Bottom left
        adText.setBackground(gradientDrawable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showPromoteBanner();
    }


    public void showPromoteBanner() {

        try {

            if (youtubeList != null && !youtubeList.isEmpty()) {
                int size = youtubeList.size();
                Random random = new Random();
                int bannerIndex = random.nextInt(size);
                buildBanner(bannerIndex);
            } else {
                //the ad list is empty.
                if (onYoutubeBannerListener != null) {
                    onYoutubeBannerListener.onYoutubeBannerAdFailedToLoad("The YoutubeAd list is empty !");
                }
                setVisibility(GONE);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("SetTextI18n")
    private void buildBanner(int index) {

        initializeInflateUI();

        String youtubeTitle = youtubeList.get(index).getTitle();
        String youtubeIcon = youtubeList.get(index).getIcon();


        String youtubeVideoLink = youtubeList.get(index).getWatch();
        String youtubeSubscribeID = youtubeList.get(index).getChannelID();


        if (onYoutubeBannerListener != null) {
            onYoutubeBannerListener.onYoutubeBannerAdLoaded();
        }


        loadIcon(youtubeIcon);
        videoName.setText(youtubeTitle);

        //loaded from prefs :
        String viewsCounter = promotePrefs.getViewsCounter(index);
        String likesCounter = promotePrefs.getLikeCounter(index);

        videoViews.setText(viewsCounter + " Views");
        videoLikes.setText(likesCounter);

        //make animation between two content :
        animation();
        //subscribeText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));

        bannerBody.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onYoutubeBannerListener != null) {
                    onYoutubeBannerListener.onYoutubeBannerAdClicked();
                }
                AppsConfig.youtubeWatchVideo(context, youtubeVideoLink);
            }
        });

        subscribe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onYoutubeBannerListener != null) {
                    onYoutubeBannerListener.onYoutubeBannerAdClicked();
                }
                if (subscribeText.getText().toString().equalsIgnoreCase("subscribe")) {
                    AppsConfig.youtubeSubscribeChannel(context, youtubeSubscribeID);
                } else {
                    AppsConfig.youtubeWatchVideo(context, youtubeVideoLink);
                }

            }
        });


    }

    private void loadIcon(String iconLink) {
        iconProgress.setVisibility(VISIBLE);
        Glide.with(context)
                .load(iconLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        iconProgress.setVisibility(GONE);
                        return false;
                    }
                })
                .into(icons);
    }

    private void animation() {

        for (int i = 1; i < 3; i++) {

            int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationIndexX(finalI);
                    if (finalI == 3 - 1) {
                        animation();
                    }
                }
            }, 1000L * i * ANIMATION_SPEED);
        }

    }

    private void animationIndexX(int index) {
        switch (index) {
            case 1:
                setAnimationGone(content1);
                setAnimationVisible(content2);
                break;
            case 2:
                setAnimationGone(content2);
                setAnimationVisible(content1);
                break;
            case 3:
                setAnimationGone(content1);
                setAnimationVisible(content2);
                break;

        }
    }


    private void setAnimationVisible(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1f);
        view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_up));
    }

    private void setAnimationGone(View view) {
        view.setVisibility(View.GONE);
        view.setAlpha(0.3f);
        view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_down));
    }


    public void setSubscribeTitle(String title) {
        this.attrTitleButton = title;
        initializeInflateUI();
    }

    public void setDescription(String description) {
        this.attrDescriptionText = description;
        initializeInflateUI();
    }

    public void setSubscribeTitleColor(int color) {
        try {
            this.attrColorTitle = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setSubscribeTitleColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrColorTitle = Color.parseColor(color);
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


    public void setSubscribeColor(int color) {
        try {
            this.attrColorButton = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setSubscribeColor(String color) {
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

    public void setViewsColor(int color) {
        try {
            this.contentColor = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setViewsColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.contentColor = Color.parseColor(color);
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


    public void setBodyColor(int color) {
        try {
            this.attrBodyColor = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setBodyColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.attrBodyColor = Color.parseColor(color);
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


    public void setOnYoutubeBannerListener(OnYoutubeBannerListener onYoutubeBannerListener) {
        this.onYoutubeBannerListener = onYoutubeBannerListener;
    }
}
