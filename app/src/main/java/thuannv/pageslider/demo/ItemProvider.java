package thuannv.pageslider.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author thuannv
 * @since 19/05/2018
 */
public final class ItemProvider {

    private ItemProvider() {
        throw new UnsupportedOperationException("Disallow instantiate object of utility class");
    }

    public static List<ListItem> provides() {
        ListItem item;
        final int[] res = {R.drawable.flower1, R.drawable.flower2, R.drawable.flower3};
        final ArrayList<ListItem> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            item = new ListItem(
                    ImageSource.fromResource(res[(i % res.length)]),
                    "Flower " + i,
                    "This is description for image " + i
            );
            items.add(item);
        }
        return items;
    }
}
