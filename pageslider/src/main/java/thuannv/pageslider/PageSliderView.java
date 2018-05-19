package thuannv.pageslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public class PageSliderView extends FrameLayout {

    private static final long DEFAULT_SLIDE_DURATION = 4000;

    private float mAspectRatio;

    private ViewPager mViewPager;

    private PagerIndicator mPagerIndicator;

    private long mAutoSlideDuration = DEFAULT_SLIDE_DURATION;

    private boolean mIsAutoSlideEnabled = false;

    private AutoSlider mAutoSlider;

    public PageSliderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PageSliderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageSliderView);
        try {
            mAspectRatio = a.getFloat(R.styleable.PageSliderView_aspectRatio, 0.32f);
            mIsAutoSlideEnabled = a.getBoolean(R.styleable.PageSliderView_autoSlide, false);
            mAutoSlideDuration = a.getInt(R.styleable.PageSliderView_autoSlideDuration, (int) DEFAULT_SLIDE_DURATION);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = Math.round(mAspectRatio * measuredWidth);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoSlide();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoSlide();
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mPagerIndicator.setViewPager(mViewPager);

        if (adapter instanceof InfinitePagerAdapter && ((InfinitePagerAdapter) adapter).getActualCount() < 2) {
            mPagerIndicator.setVisibility(GONE);
        } else {
            mPagerIndicator.setVisibility(VISIBLE);
        }
    }

    public void startAutoSlide() {
        if (mIsAutoSlideEnabled) {
            if (mAutoSlider == null) {
                mAutoSlider = new AutoSlider(this);
            }
            postDelayed(mAutoSlider, mAutoSlideDuration);
        }
    }

    public void stopAutoSlide() {
        if (mAutoSlider != null) {
            removeCallbacks(mAutoSlider);
        }
    }

    public void setAutoSlideEnabled(boolean enable) {
        mIsAutoSlideEnabled = enable;
    }

    private void slide() {
        if (mViewPager != null) {
            int next = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(next, true);
            if (mIsAutoSlideEnabled && mAutoSlider != null) {
                postDelayed(mAutoSlider, mAutoSlideDuration);
            }
        }
    }

    /**
     * {@link AutoSlider}
     */
    private static final class AutoSlider implements Runnable {

        private final WeakReference<PageSliderView> mRef;

        AutoSlider(PageSliderView pageSliderView) {
            mRef = new WeakReference<>(pageSliderView);
        }

        @Override
        public void run() {
            final PageSliderView pageSliderView = mRef.get();
            if (pageSliderView != null) {
                pageSliderView.slide();
            }
        }
    }
}
