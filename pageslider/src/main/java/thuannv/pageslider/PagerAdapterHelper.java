package thuannv.pageslider;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

/**
 * @author thuannv
 * @since 21/05/2018
 */
public final class PagerAdapterHelper {

    private static final String TAG = "PagerAdapterHelper";

    private PagerAdapterHelper() {
        throw new UnsupportedOperationException("Disallowed instantiate object");
    }

    public static int getCount(PagerAdapter adapter) {
        if (adapter instanceof InfinitePagerAdapter) {
            return ((InfinitePagerAdapter) adapter).getActualCount();
        }
        return adapter == null ? 0 : adapter.getCount();
    }

    public static void registerDataSetObserver(PagerAdapter adapter, DataSetObserver observer) {
        if (adapter == null || observer == null) {
            Log.e(TAG, "registerDataSetObserver(): adapter or observer is null");
            return ;
        }
        PagerAdapter pagerAdapter = adapter;
        if (adapter instanceof InfinitePagerAdapter) {
            pagerAdapter = ((InfinitePagerAdapter) adapter).unwrap();
        }
        if (pagerAdapter != null) {
            pagerAdapter.registerDataSetObserver(observer);
        }
    }
}
