package cn.imtianx.databindingdemo;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by imtianx on 2016-9-18.
 */
public class DemoBindingAdapter {

    /**
     * 添加自定属性
     *
     * @param imageView
     * @param url
     * @param drawable
     */
    @BindingAdapter({"app:imageUri", "app:placeholder"})
    public static void loadImageFromUrl(final ImageView imageView,
                                        String url,
                                        Drawable drawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(drawable)
                .into(imageView);
    }

    /**
     * 使View 的background 可以使用int 值的 color
     *
     * @param color
     * @return
     */
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
