package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vku.lethanhan.utcshop.R;

import java.util.List;

public class SlideAdapter extends PagerAdapter {

    private Context context;
    private List<String> slideImages;

    public SlideAdapter(Context context, List<String> slideImages) {
        this.context = context;
        this.slideImages = slideImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_slide, container, false);
        ImageView imageView = view.findViewById(R.id.img_item_slide);
        String slideImage = slideImages.get(position);
        Glide.with(context).load(slideImage).apply(new RequestOptions().transform(new CenterCrop()).transform(new RoundedCorners(12)))
                .into(imageView);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return slideImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
