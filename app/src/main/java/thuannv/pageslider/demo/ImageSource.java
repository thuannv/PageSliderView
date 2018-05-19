package thuannv.pageslider.demo;

import android.net.Uri;

import java.io.File;

/**
 * @author thuannv
 * @since 14/03/2018
 *
 * Utility class to present image resource. Since image could be from any sources, such as a network
 * uri, from a path, a file or a resource id. We use exclusive state-base resource to present for
 * each image source. After constructed, a ImageSource is immutable and presented for only single
 * source.
 */
public final class ImageSource {

    private static final int TYPE_NONE = 0;

    private static final int TYPE_RESOURCE_ID = TYPE_NONE + 1;

    private static final int TYPE_PATH = TYPE_NONE + 2;

    private static final int TYPE_FILE = TYPE_NONE + 3;

    private static final int TYPE_URI = TYPE_NONE + 4;

    private final int mType;

    private final int mResourceId;

    private final String mPath;

    private final File mFile;

    private final Uri mUri;

    private ImageSource(int type, int resourceId, String path, File file, Uri uri) {
        mType = type;
        mResourceId = resourceId;
        mPath = path;
        mFile = file;
        mUri = uri;
    }

    public boolean isLocalResourceId() {
        return TYPE_RESOURCE_ID == mType;
    }

    public boolean isPath() {
        return TYPE_PATH == mType;
    }

    public boolean isFile() {
        return TYPE_FILE == mType;
    }

    public boolean isUri() {
        return TYPE_URI == mType;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public String getPath() {
        return mPath;
    }

    public File getFile() {
        return mFile;
    }

    public Uri getUri() {
        return mUri;
    }

    public static ImageSource fromResource(int resourceId) {
        return new ImageSource(TYPE_RESOURCE_ID, resourceId, null, null, null);
    }

    public static ImageSource fromPath(String path) {
        return new ImageSource(TYPE_PATH, 0, path, null, null);
    }

    public static ImageSource fromFile(File file) {
        return new ImageSource(TYPE_FILE, 0, null, file, null);
    }

    public static ImageSource fromUri(Uri uri) {
        return new ImageSource(TYPE_URI, 0, null, null, uri);
    }
}
