package util;

import java.util.HashMap;

public class ThreadLocalUtil {
    private static final ThreadLocal<HashMap<String, Object>> threadLocal = ThreadLocal.withInitial(
            HashMap::new
    );

    public static HashMap<String, Object> getMap() {
        return threadLocal.get();
    }

    public static void set(String key, Object value) {
        HashMap<String, Object> dataMap = threadLocal.get();
        dataMap.put(key, value);
        threadLocal.set(dataMap);
    }

    public static HashMap<String, Object> getAndRemove() {
        HashMap<String, Object> result = threadLocal.get();
        threadLocal.remove();
        return result;
    }
}
