package q.rorbin.fastimagesize.cache;

import android.util.LruCache;

/**
 * Created by chqiu on 2017/3/30.
 */

public class MemoryCache extends LruCache<String, int[]> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MemoryCache(int maxSize) {
        super(maxSize);
    }
}
