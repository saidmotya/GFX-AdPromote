package com.gfx.adPromote.Adapters;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class PagerTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private final ViewPager viewPager;

    public PagerTransformer(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        try {

            float percentage = 1 - Math.abs(position);
            float scaleAmountPercent = 5f;
            float amount = ((100 - scaleAmountPercent) + (scaleAmountPercent * percentage)) / 100;
            float scaleFactor = Math.max(MIN_SCALE, percentage);

            int pageWidth = viewPager.getMeasuredWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight();
            int paddingLeft = viewPager.getPaddingLeft();
            float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;

            if (transformPos < -1) {

                setSize(page,scaleFactor);


            } else if (transformPos <= 1) {

                setSize(page,amount); //1.0f


            } else {
                setSize(page,scaleFactor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSize(View page, float size) {
        //page.setScaleX(size);
        //page.setScaleY(size);
        page.animate()
                .scaleY(size)
                .scaleX(size)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void setSize(View page, float position, float percentage) {
        page.setScaleX((position != 0 && position != 1) ? percentage : 1);
        page.setScaleY((position != 0 && position != 1) ? percentage : 1);
    }


}