package io.github.zkllll23.utils.thread;

import io.github.zkllll23.model.wemedia.pojos.WmUser;

public class WmThreadLocalUtil {
    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    // 存入线程
    public static void setUser(WmUser wmUser) {
        WM_USER_THREAD_LOCAL.set(wmUser);
    }
    // 从线程获取
    public static WmUser getUser() {
        return WM_USER_THREAD_LOCAL.get();
    }
    // 清理线程
    public static void clear() {
        WM_USER_THREAD_LOCAL.remove();
    }
}
