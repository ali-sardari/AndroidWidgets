package sardari.utils;

import android.content.Context;

public class Utils {
    private static Context appContext;

    public static Context getContext() {
        return appContext;
    }

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

//    static void initTimber() {
//        Timber.plant(new Timber.DebugTree() {
//            @Override
//            protected String createStackElementTag(@NotNull StackTraceElement element) {
//                return String.format("Timber -> %s:%s:%s",
//                        super.createStackElementTag(element),
//                        element.getMethodName(),
//                        element.getLineNumber());
//            }
//        });
//    }
}
