package thuannv.pageslider.demo;

import android.net.Uri;

import java.util.Arrays;
import java.util.List;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public final class ImagesProvider {

    private ImagesProvider() {
        throw new UnsupportedOperationException("Disallow instantiate object of utility classes");
    }

    public static List<ImageSource> provides() {
        return Arrays.asList(
                ImageSource.fromResource(R.drawable.flower1),
                ImageSource.fromResource(R.drawable.flower2),
                ImageSource.fromResource(R.drawable.flower3),
                ImageSource.fromUri(Uri.parse("file:///android_asset/flower4.jpg"))
        );
    }
}
