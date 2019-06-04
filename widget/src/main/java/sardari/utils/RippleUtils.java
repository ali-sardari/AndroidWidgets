//package sardari.utils;
//
//import android.content.res.ColorStateList;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.RippleDrawable;
//import android.os.Build;
//
//public class RippleUtils {
//    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
//        } else {
//
//        }
//    }
//
//    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
//        return new ColorStateList(
//                new int[][]
//                        {
//                                new int[]{android.R.attr.state_pressed},
//                                new int[]{android.R.attr.state_focused},
//                                new int[]{android.R.attr.state_activated},
//                                new int[]{}
//                        },
//                new int[]
//                        {
//                                pressedColor,
//                                pressedColor,
//                                pressedColor,
//                                normalColor
//                        }
//        );
//    }
//
//    public static ColorDrawable getColorDrawableFromColor(int color) {
//        return new ColorDrawable(color);
//    }
//
//}
