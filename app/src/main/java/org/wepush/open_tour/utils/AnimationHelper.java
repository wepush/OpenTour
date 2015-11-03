package org.wepush.open_tour.utils;

/**
 * Created by Antonio on 16/04/2015.
 */


import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import org.wepush.open_tour.R;


public class AnimationHelper {

    public static synchronized void growShrink(final View view) {
        final Animation grow = AnimationUtils.loadAnimation(view.getContext(), R.anim.grow_scale);
        final Animation shrink = AnimationUtils.loadAnimation(view.getContext(), R.anim.shrink_scale);

        grow.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {

            }

            @Override public void onAnimationEnd(Animation animation) {
                view.startAnimation(shrink);
            }

            @Override public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(grow);
    }

    public static float convertDensityToPixel(Context context, float densityPixel) {
        Resources resources = context.getResources();
        return densityPixel * (resources.getDisplayMetrics().densityDpi / 160f);
    }

    public synchronized static void alphaSlideUp(View view) {
        slide(view, 0, (int) convertDensityToPixel(view.getContext(), view.getLayoutParams().height),
                0, false, new DecelerateInterpolator());
    }

    public synchronized static void alphaSlideUp(View view, float initialPosition) {
        slide(view, 10, convertDensityToPixel(view.getContext(), initialPosition),
                0, false, new DecelerateInterpolator());
    }

    public synchronized static void alphaSlideUp(View view, int startDelay, float initialPosition,
                                                 TimeInterpolator interpolator) {
        slide(view, startDelay, convertDensityToPixel(view.getContext(), initialPosition), 0, false,
                interpolator);
    }

    public synchronized static void alphaSlideDown(View view) {
        slide(view, 0, 0,
                (int) convertDensityToPixel(view.getContext(), view.getLayoutParams().height),
                true, new DecelerateInterpolator());
    }

    public synchronized static void alphaSlideDown(View view, int startDelay, float finalPosition) {
        slide(view, startDelay, 0,
                (int) convertDensityToPixel(view.getContext(), finalPosition),
                true, new DecelerateInterpolator());
    }

    private static void slide(final View view,
                              final int startDelay,
                              final float initialPosition,
                              final int translation,
                              final boolean isHiding,
                              final TimeInterpolator interpolator) {
        final float initialAlpha = isHiding ? 1 : 0;
        final float multiplier = isHiding ? -1 : 1;

        ValueAnimator animation = ValueAnimator.ofInt((int) initialPosition, translation);
        animation.setStartDelay(startDelay);
        animation.setDuration(300);
        animation.setInterpolator(interpolator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                view.setClickable(false);
                view.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animator animation) {
                view.setClickable(true);

                if (isHiding) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override public void onAnimationCancel(Animator animation) {}

            @Override public void onAnimationRepeat(Animator animation) {}
        });
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                float alpha = initialAlpha + (multiplier * animation.getAnimatedFraction());
                view.setTranslationY(value);
                view.setAlpha(alpha);
            }
        });

        view.setAlpha(isHiding ? 1 : 0);

        animation.start();
    }

    public static ValueAnimator getFloatAnimator(float start, float end,
                                                 ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animatorUpdateListener);
        return valueAnimator;
    }

    public static void alphaIn(View view) {
        view.animate().alpha(1).setDuration(400).start();
    }

    public static void alphaOut(View view) {
        view.animate().alpha(0).setDuration(400).start();
    }
}

