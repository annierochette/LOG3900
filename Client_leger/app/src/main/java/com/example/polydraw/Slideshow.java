package com.example.polydraw;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


//source: https://codinginflow.com/tutorials/android/imageslider

public class Slideshow extends PagerAdapter {

    private Context context;

    public int[] images = new int[]{
            R.drawable.tuto_menu,
            R.drawable.tuto_play_menu,
            R.drawable.tuto_libre,
            R.drawable.tuto_devineur_solo,
            R.drawable.tuto_profil
    };

    Slideshow(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(images[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);

    }
}
