package com.gfx.adPromote.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gfx.adPromote.Interfaces.OnPagerScreenListener;
import com.gfx.adPromote.R;

import java.util.ArrayList;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class AdapterPager extends PagerAdapter {

    private final Context context;
    private final ArrayList<String> screenList;
    private OnPagerScreenListener onPagerScreenListener ;

    public AdapterPager(Context context, ArrayList<String> screenList) {
        this.context = context;
        this.screenList = screenList;
    }

    @Override
    public int getCount() {
        return screenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.items_screen, null);
        ImageView screen = view.findViewById(R.id.screen);
        RelativeLayout loading = view.findViewById(R.id.inter_screen_progress);
        loading.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(screenList.get(position))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (onPagerScreenListener != null){
                            if (e != null){
                                onPagerScreenListener.onPagerScreenListenerFailed(e.getMessage());
                            }else {
                                onPagerScreenListener.onPagerScreenListenerFailed("Failed to get and load screen : "+position);
                            }

                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        if (onPagerScreenListener != null){
                            onPagerScreenListener.onPagerScreenListenerLoaded();
                        }
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(screen);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void setOnPagerScreenListener(OnPagerScreenListener onPagerScreenListener) {
        this.onPagerScreenListener = onPagerScreenListener;
    }
}
