package nikunj.beathangar.model;
import java.util.Collections;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

public class MemcacheUtils {
	static Cache cache; 
	public static void init() { 
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
            // ...
        }
	}
	public static void putUser(User user) { 
		return;
		//cache.put(user.userId,  user); 
	}
	public static User getUser(String userId) {
		return null;
/*		if (cache.containsKey(userId)) { 
			return (User)cache.get(userId);
		}
		return null;
*/	}
}
