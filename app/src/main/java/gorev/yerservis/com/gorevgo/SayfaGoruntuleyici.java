package gorev.yerservis.com.gorevgo;

import android.support.v4.view.ViewPager;
import android.view.View;


public class SayfaGoruntuleyici implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 1f;
    private static final float MIN_ALPHA = 0.7f;


    public SayfaGoruntuleyici(boolean isZoomEnable) {
        if (isZoomEnable) {
            MIN_SCALE = 0.85f;
        } else {
            MIN_SCALE = 1f;
        }
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        float vertMargin = pageHeight * (1 - MIN_SCALE) / 2;
        float horzMargin = pageWidth * (1 - MIN_SCALE) / 2;
        view.setScaleX(MIN_SCALE);
        view.setScaleY(MIN_SCALE);
        if (position < -1) {
            view.setAlpha(MIN_ALPHA);
            view.setTranslationX(horzMargin - vertMargin / 2);


        } else if (position <= 1) { // [-1,1]
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            vertMargin = pageHeight * (1 - scaleFactor) / 2;
            horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {
            view.setAlpha(MIN_ALPHA);
            view.setTranslationX(-horzMargin + vertMargin / 2);

        }
    }
}