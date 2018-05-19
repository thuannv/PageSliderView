package thuannv.pageslider;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public class InfinitePagerAdapter extends PagerAdapter {

    private FrameLayout mEmptyItem = null;

    private PagerAdapter mOriginalAdapter;

    private InfinitePagerAdapter(PagerAdapter originalAdapter) {
        super();
        if (originalAdapter == null) {
            throw new IllegalArgumentException("originalAdapter must NOT be null");
        }
        mOriginalAdapter = originalAdapter;
    }

    private View emptyItem(@NonNull Context context) {
        if (mEmptyItem == null) {
            mEmptyItem = new FrameLayout(context);
        }
        return mEmptyItem;
    }

    private boolean validPosition(int position) {
        return POSITION_NONE != getActualPosition(position);
    }

    public int getActualCount() {
        return mOriginalAdapter == null ? 0 : mOriginalAdapter.getCount();
    }

    public boolean isEmpty() {
        return getActualCount() == 0;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        if (validPosition(position)) {
            mOriginalAdapter.destroyItem(container, getActualPosition(position), object);
        }
    }

    public int getActualPosition(int position) {
        return isEmpty() ? POSITION_NONE : (position % getActualCount());
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return validPosition(position)
                ? mOriginalAdapter.instantiateItem(container, getActualPosition(position))
                : emptyItem(container.getContext());
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (mOriginalAdapter == null) {
            super.restoreState(state, loader);
        } else {
            mOriginalAdapter.restoreState(state, loader);
        }
    }

    @Override
    public Parcelable saveState() {
        return mOriginalAdapter == null ? super.saveState() : mOriginalAdapter.saveState();
    }

    public PagerAdapter unwrap() {
        return mOriginalAdapter;
    }

    public static InfinitePagerAdapter wrap(PagerAdapter originalAdapter) {
        return new InfinitePagerAdapter(originalAdapter);
    }
}
