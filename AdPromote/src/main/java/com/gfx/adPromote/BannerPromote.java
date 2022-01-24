package com.gfx.adPromote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.widget.AppCompatRatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gfx.adPromote.Config.AppsConfig;
import com.gfx.adPromote.Helper.DatabaseHelper;
import com.gfx.adPromote.Helper.PromotePrefs;
import com.gfx.adPromote.Interfaces.OnBannerListener;
import com.gfx.adPromote.Models.AppModels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class BannerPromote extends FrameLayout {


    private final int ANIMATION_SPEED = 3; //time on second to change  Text banner.

    private final Context context;
    private String installTitle = "Install"; //default title
    private int installColor = Color.parseColor("#2196F3"); //default color
    private int contentColor = Color.parseColor("#2196F3"); //default color
    private int attrBodyColor = Color.WHITE; //default color


    private TextView installText, adColor;
    private RelativeLayout installButton;
    private RelativeLayout bannerBody;
    private RelativeLayout bannerProgress;

    private ImageView icon;
    private TextView name, downloads, description;
    private LinearLayout content1, content2;

    private AppCompatRatingBar ratingBar;
    private TextView ratingCount;

    private ArrayList<AppModels> adList = new ArrayList<>();
    private OnBannerListener onBannerListener;
    private final Handler handler = new Handler();
    private PromotePrefs promotePrefs;

    public BannerPromote(Context context) {
        super(context);
        initView(null);
        this.context = context;
        initializeData();
    }

    public BannerPromote(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        this.context = context;
        initializeData();
    }

    public BannerPromote(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    public BannerPromote(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
        this.context = context;
        initializeData();

    }


    private void initView(AttributeSet attrs) {

        //Load attributes
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);

        installTitle = typedArray.getString(R.styleable.BannerView_banner_installTitle);
        installColor = typedArray.getColor(R.styleable.BannerView_banner_installColor, installColor);
        contentColor = typedArray.getColor(R.styleable.BannerView_banner_contentColor, contentColor);
        attrBodyColor = typedArray.getColor(R.styleable.BannerView_banner_bodyColor, attrBodyColor);
        inflateBanner();

        typedArray.recycle();

    }

    private void inflateBanner() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.promote_banner, this);
    }

    private void initializeData() {
        promotePrefs = new PromotePrefs(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        adList = databaseHelper.getAllApps();
    }


    private void applyUI() {
        if (installText != null && installTitle != null) {
            installText.setText(installTitle);
        }

        if (installButton != null) {
            installButton.setBackgroundColor(installColor);
            adColor.setBackgroundColor(installColor);
        }

        if (name != null && description != null) {
            name.setTextColor(contentColor);
            description.setTextColor(contentColor);
        }
        if (bannerBody != null) {
            bannerBody.setBackgroundColor(attrBodyColor);
        }
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showPromoteBanner();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("SetTextI18n")
    private void initializeInflateUI() {
        //standard content
        installButton = (RelativeLayout) findViewById(R.id.install);
        installText = (TextView) findViewById(R.id.install_txt);
        adColor = (TextView) findViewById(R.id.ad);

        bannerBody = (RelativeLayout) findViewById(R.id.banner_body);
        bannerProgress = (RelativeLayout) findViewById(R.id.banner_progress);

        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingbar);
        ratingCount = (TextView) findViewById(R.id.ratingCount);


        //initialize changeable UI :
        icon = (ImageView) findViewById(R.id.icons);
        name = (TextView) findViewById(R.id.app_name);
        downloads = (TextView) findViewById(R.id.app_download);
        description = (TextView) findViewById(R.id.app_description);

        content1 = (LinearLayout) findViewById(R.id.content_1);
        content2 = (LinearLayout) findViewById(R.id.content_2);

        applyUI();
    }


    public void showPromoteBanner() {

        try {

            if (adList != null && !adList.isEmpty()) {
                int size = adList.size();
                Random random = new Random();
                int bannerIndex = random.nextInt(size);
                buildBanner(bannerIndex);
            } else {
                //the ad list is empty.
                if (onBannerListener != null) {
                    onBannerListener.onBannerAdFailedToLoad("The Ad list is empty ! please check your json file.");
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
        String data_name = adList.get(index).getName();
        String data_icons = adList.get(index).getIcons();
        String short_description = adList.get(index).getShortDescription();
        String data_packageName = adList.get(index).getPackageName();


        if (onBannerListener != null) {
            onBannerListener.onBannerAdLoaded();
        }


        loadIcon(data_icons);
        name.setText(data_name);
        description.setText(short_description);

        //loaded from prefs :
        String downloadsCounter = promotePrefs.getDownloadCounter(index);
        float ratCount = promotePrefs.getRateCounter(index);
        downloads.setText("+ " + downloadsCounter + " Downloads");
        ratingBar.setRating(ratCount);
        ratingCount.setText("(" + ratCount + ")");


        //make animation between two content :
        animation();

        installButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onBannerListener != null) {
                    onBannerListener.onBannerAdClicked();
                }
                AppsConfig.openAdLink(context, data_packageName);
            }
        });

    }

    private void loadIcon(String icons) {
        bannerProgress.setVisibility(VISIBLE);
        Glide.with(context)
                .load(icons)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        bannerProgress.setVisibility(GONE);
                        return false;
                    }
                })
                .into(icon);
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
                setAnimationGone(content2);
                setAnimationVisible(content1);
                break;
            case 2:
                setAnimationGone(content1);
                setAnimationVisible(content2);
                break;
            case 3:
                setAnimationGone(content2);
                setAnimationVisible(content1);
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


    public void setInstallTitle(String installTitle) {
        this.installTitle = installTitle;
        initializeInflateUI();
    }


    public void setInstallColor(int color) {
        try {
            this.installColor = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setInstallColor(String color) {
        try {
            if (color.startsWith("#")) {
                this.installColor = Color.parseColor(color);
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

    public void setDescriptionColor(int color) {
        try {
            this.contentColor = context.getResources().getColor(color);
            initializeInflateUI();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                AppsConfig.setLog("setInstallColor : Color value wrong, failed to get color from resource");
            }
        }
    }

    public void setDescriptionColor(String color) {
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

    public void setOnBannerListener(OnBannerListener onBannerListener) {
        this.onBannerListener = onBannerListener;
    }
}
