package thuannv.pageslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public class PagerIndicator extends View {

    private int mRadius;

    private int mMargin;

    private int mSelectedColor;

    private int mUnselectedColor;

    private Paint mPaint;

    private int mCount = 0;

    private int mSelectedPosition = 0;

    private ViewPager mViewPager;

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (mViewPager == null) {
                return;
            }

            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null) {
                int selectedPos;
                if (adapter instanceof InfinitePagerAdapter) {
                    selectedPos = ((InfinitePagerAdapter) adapter).getActualPosition(position);
                } else {
                    selectedPos = position;
                }
                if (mSelectedPosition != selectedPos) {
                    mSelectedPosition = selectedPos;
                    invalidate();
                }
            }
        }
    };

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            updateCount();
        }

        @Override
        public void onInvalidated() {
        }
    };


    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        try {
            mMargin = a.getDimensionPixelSize(R.styleable.PagerIndicator_margin, (int) (4 * density));
            mRadius = a.getDimensionPixelSize(R.styleable.PagerIndicator_radius, (int) (3 * density));
            mSelectedColor = a.getColor(R.styleable.PagerIndicator_selectedColor, Color.WHITE);
            mUnselectedColor = a.getColor(R.styleable.PagerIndicator_unselectedColor, Color.rgb(128, 128, 128));
        } finally {
            a.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            if (adapter instanceof InfinitePagerAdapter) {
                InfinitePagerAdapter infiniteAdapter = (InfinitePagerAdapter) adapter;
                mCount = infiniteAdapter.getActualCount();
                PagerAdapter originalAdapter = infiniteAdapter.unwrap();
                if (originalAdapter != null) {
                    originalAdapter.registerDataSetObserver(mDataSetObserver);
                }
            } else {
                mCount = adapter.getCount();
                adapter.registerDataSetObserver(mDataSetObserver);
            }
            requestLayout();
        }
    }

    private void updateCount() {
        if (mViewPager == null) {
            mCount = 0;
        } else {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null) {
                if (adapter instanceof InfinitePagerAdapter) {
                    mCount = ((InfinitePagerAdapter) adapter).getActualCount();
                } else {
                    mCount = adapter.getCount();
                }
            } else {
                mCount = 0;
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();
        if (mCount > 0) {
            int diameter = 2 * mRadius;
            width += diameter;
            height += diameter;
            for (int i = 1; i < mCount; i++) {
                width += mMargin + diameter;
            }
        }
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCount > 0) {
            int startX = getPaddingStart();
            int startY = getPaddingTop();
            int diameter = 2 * mRadius;
            for (int i = 0; i < mCount - 1; i++) {
                mPaint.setColor(i == mSelectedPosition ? mSelectedColor : mUnselectedColor);
                canvas.drawCircle(startX + mRadius, startY + mRadius, mRadius, mPaint);
                startX += diameter + mMargin;
            }
            mPaint.setColor(mCount - 1 == mSelectedPosition ? mSelectedColor : mUnselectedColor);
            canvas.drawCircle(startX + mRadius, startY + mRadius, mRadius, mPaint);
        }
    }
}
