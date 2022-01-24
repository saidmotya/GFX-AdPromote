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
import com.gfx.adPromote.Interfaces.OnNativeListener;
import com.gfx.adPromote.Models.AppModels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class NativePromote extends FrameLayout {


    private final Context context;

    private String attrInstallTitle = "Install"; //default title
    private int attrInstallColor = Color.parseColor("#2196F3"); //default color
    private int attrContentColor = Color.parseColor("#2E2E2E"); //default color
    private int attrBodyColor = Color.WHITE; //default color
    private int attrRadiusButton = 20;


    private TextView adColor, installNativeTitle;
    private RelativeLayout installNativeBody;
    private LinearLayout nativeBody;
    private RelativeLayout previewProgress, iconProgress;

    private ImageView icon, native_preview;
    private TextView name, downloads;

    private AppCompatRatingBar ratingBar;
    private TextView ratingCount;

    private ArrayList<AppModels> adList = new ArrayList<>();
    private PromotePrefs promotePrefs;
    private OnNativeListener onNativeListener;

    public NativePromote(Context context) {
        super(context);
        initView(null);
        this.context = context;
        initializeData();
    }

    public NativePromote(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        this.context = context;
        initializeData();
    }

    public NativePromote(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    public NativePromote(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
        this.context = context;
        initializeData();
    }


    private void initView(AttributeSet attrs) {

        //Load attributes
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NativeView, 0, 0);

        attrInstallTitle = typedArray.getString(R.styleable.NativeView_native_installTitle);
        attrContentColor = typedArray.getColor(R.styleable.NativeView_native_contentColor, attrContentColor);
        attrBodyColor = typedArray.getColor(R.styleable.NativeView_native_bodyColor, attrBodyColor);
        attrRadiusButton = typedArray.getInteger(R.styleable.NativeView_native_installRadius, attrRadiusButton);

        //can be color or shape from drawable :
        attrInstallColor = typedArray.getColor(R.styleable.NativeView_native_installColor, attrInstallColor);
        inflateBanner();

        typedArray.recycle();

    }

    private void inflateBanner() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.promote_native_banner, this);
    }

    private void initializeData() {
        promotePrefs = new PromotePrefs(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        adList = databaseHelper.getAllApps();
    }

    private void applyUI() {

        if (installNativeTitle != null && attrInstallTitle != null) {
            installNativeTitle.setText(attrInstallTitle);
        }

        if (installNativeBody != null) {
            //generate button shape programmatically.
            generateInstallNativeBody();

        }

        if (adColor != null) {
            adColor.setBackgroundColor(attrInstallColor);
        }

        if (name != null) {
            name.setTextColor(attrContentColor);
        }

        if (nativeBody != null) {
            nativeBody.setBackgroundColor(attrBodyColor);
        }
    }

    private void generateInstallNativeBody() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(attrInstallColor);

        //corner of button shape
        gradientDrawable.setCornerRadii(new float[]{
                attrRadiusButton, attrRadiusButton, // Top Left
                attrRadiusButton, attrRadiusButton, // Top Right
                attrRadiusButton, attrRadiusButton, // Bottom Right
                attrRadiusButton, attrRadiusButton});//Bottom left
        installNativeBody.setBackground(gradientDrawable);
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //handler.removeCallbacksAndMessages(null);
    }


    @SuppressLint("SetTextI18n")
    private void initializeInflateUI() {
        //standard content
        installNativeBody = (RelativeLayout) findViewById(R.id.native_install);
        installNativeTitle = (TextView) findViewById(R.id.native_install_txt);
        nativeBody = (LinearLayout) findViewById(R.id.native_body);

        adColor = (TextView) findViewById(R.id.native_ad_text);

        previewProgress = (RelativeLayout) findViewById(R.id.native_preview_progress);
        iconProgress = (RelativeLayout) findViewById(R.id.native_icon_progress);


        ratingBar = (AppCompatRatingBar) findViewById(R.id.native_ratingbar);
        ratingCount = (TextView) findViewById(R.id.native_ratingCount);


        //initialize changeable UI :
        icon = (ImageView) findViewById(R.id.native_icons);
        native_preview = (ImageView) findViewById(R.id.native_preview);
        name = (TextView) findViewById(R.id.native_app_name);
        downloads = (TextView) findViewById(R.id.native_app_download);

        applyUI();
    }


    public void showPromoteNative() {

        try {

            if (adList != null && !adList.isEmpty()) {
                int size = adList.size();
                Random random = new Random();
                int nativeIndex = random.nextInt(size);
                buildNative(nativeIndex);
            } else {
                //the ad list is empty.
                if (onNativeListener != null) {
                    onNativeListener.onNativeAdFailedToLoad("The Ad list is empty ! please check your json file.");
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
        String data_name = adList.get(index).getName();
        String data_icons = adList.get(index).getIcons();
        String preview = adList.get(index).getAppPreview();
        String data_packageName = adList.get(index).getPackageName();

        if (onNativeListener != null) {
            onNativeListener.onNativeAdLoaded();
        }

        loadIcon(data_icons);
        loadPreview(preview);
        name.setText(data_name);

        //loaded from prefs :
        String downloadsCounter = promotePrefs.getDownloadCounter(index);
        float ratCount = promotePrefs.getRateCounter(index);
        downloads.setText("+ " + downloadsCounter + " Downloads");
        ratingBar.setRating(ratCount);
        ratingCount.setText("(" + ratCount + ")");

        installNativeBody.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //user clicked own banner :
                if (onNativeListener != null) {
                    onNativeListener.onNativeAdClicked();
                }
                AppsConfig.openAdLink(context, data_packageName);
            }
        });

    }

    private void loadIcon(String icons) {
        iconProgress.setVisibility(VISIBLE);
        Glide.with(context).load(icons)
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
                .into(icon);
    }

    private void loadPreview(String preview) {
        previewProgress.setVisibility(VISIBLE);
        Glide.with(context).load(preview)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewProgress.setVisibility(GONE);
                        return false;
                    }
                })
                .into(native_preview);
    }


    //Colors body :
    public void setButtonTitle(String title) {
        this.attrInstallTitle = title;
        initializeInflateUI();
    }


    public void setButtonColor(int color) {
        try {
            this.attrInstallColor = context.getResources().getColor(color);
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
                this.attrInstallColor = Color.parseColor(color);
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

    public void setRadiusButton(int radius) {
        this.attrRadiusButton = radius;
        initializeInflateUI();
    }

    public void setOnNativeListener(OnNativeListener onNativeListener) {
        this.onNativeListener = onNativeListener;
    }
}
