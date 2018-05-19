package thuannv.pageslider.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public class ImageSliderAdapter extends PagerAdapter {

    private List<ImageSource> mImages;

    private OnItemClickedListener mItemClickListener;

    public ImageSliderAdapter(List<ImageSource> sources) {
        if (sources == null) {
            mImages = new ArrayList<>();
        } else {
            mImages = new ArrayList<>(sources);
        }
    }

    public void setItemClickListener(OnItemClickedListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view,@NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Context context = container.getContext();
        final ImageSource image = mImages.get(position);
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onClicked(image);
            }
        });

        bind(context, imageView, image);
        container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(Context context, ImageView imageView, ImageSource source) {
        final RequestManager glide = Glide.with(context);
        if (source.isPath()) {
            glide.load(source.getPath()).into(imageView);
        } else if (source.isFile()) {
            glide.load(source.getFile()).into(imageView);
        } else if (source.isUri()) {
            glide.load(source.getUri()).into(imageView);
        } else if (source.isLocalResourceId()) {
            glide.load(source.getResourceId()).into(imageView);
        }
    }


    /**
     * {@link OnItemClickedListener}
     */
    public interface OnItemClickedListener {
        void onClicked(ImageSource source);
    }
}
