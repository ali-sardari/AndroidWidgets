package sardari.utils.toast;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import sardari.utils.FontUtils;
import sardari.utils.UIUtil;
import sardari.utils.Utils;
import sardari.widget.R;

public class ToastUtils {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    @IntDef(value = {
            LENGTH_SHORT,
            LENGTH_LONG
    })
    @Retention(RetentionPolicy.RUNTIME)
    @interface Duration {
    }

    public static void makeText(String message, ToastMode type, @Duration int duration) {
        try {
            if (Utils.getContext() != null) {
                makeText(Utils.getContext(), message, type, duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeText(String message, ToastMode type) {
        try {
            if (Utils.getContext() != null) {
                makeText(Utils.getContext(), message, type, LENGTH_LONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeText(Context context, String message) {
        makeText(context, message, ToastMode.Default, LENGTH_LONG);
    }

//    public static void makeText(Context context, String message, ToastMode type, @Duration int duration) {
//        LinearLayout layout = new LinearLayout(context);
//        TextView tv = new TextView(context);
//
//        switch (type) {
//            case Success:
//                setBackgroundAndTextColor(context, layout, R.color.toast_success, R.color.toast_text);
//                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icv_success, 0);
//                break;
//            case Error:
//                setBackgroundAndTextColor(context, layout, R.color.toast_error, R.color.toast_text);
//                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icv_error, 0);
//                break;
//            case Info:
//                setBackgroundAndTextColor(context, layout, R.color.toast_info, R.color.toast_text);
//                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icv_info, 0);
//                break;
//            case Warning:
//                setBackgroundAndTextColor(context, layout, R.color.toast_warning, R.color.toast_text);
//                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icv_warning, 0);
//                break;
//            case Default:
//                setBackgroundAndTextColor(context, layout, R.color.toast_default, R.color.toast_text);
//                break;
//            default:
//                setBackgroundAndTextColor(context, layout, R.color.toast_default, R.color.toast_text);
//        }
//
//        layout.setPadding(
//                dpToPixel(context, 10),
//                dpToPixel(context, 6),
//                dpToPixel(context, 10),
//                dpToPixel(context, 6));
//
//        tv.setTextColor(textColor);
////        tv.setTextSize(13);
//        tv.setGravity(Gravity.CENTER);
//        tv.setTypeface(FontUtils.getTypeface());
//        tv.setText(message);
//
//        tv.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen._5sdp));
//
//        layout.addView(tv);
//
//        Toast toast = new Toast(context);
//        toast.setView(layout);
//        toast.setDuration(duration == LENGTH_SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
//
//        toast.show();
//    }

    public static void makeText(Context context, String message, ToastMode type, @Duration int duration) {
        Typeface typeface;

        try {
            typeface = FontUtils.getTypeface();
        } catch (Exception e) {
            typeface = null;
        }


        switch (type) {
            case Success:
                //region Generate Toast
                makeText(
                        context,
                        message,
                        R.color.toast_success,
                        R.color.toast_text,
                        R.drawable.icv_success,
                        typeface,
                        duration);
                //endregion
                break;
            case Error:
                //region Generate Toast
                makeText(
                        context,
                        message,
                        R.color.toast_error,
                        R.color.toast_text,
                        R.drawable.icv_error,
                        typeface,
                        duration);
                //endregion
                break;
            case Info:
                //region Generate Toast
                makeText(
                        context,
                        message,
                        R.color.toast_info,
                        R.color.toast_text,
                        R.drawable.icv_info,
                        typeface,
                        duration);
                //endregion
                break;
            case Warning:
                //region Generate Toast
                makeText(
                        context,
                        message,
                        R.color.toast_warning,
                        R.color.toast_text,
                        R.drawable.icv_warning,
                        typeface,
                        duration);
                //endregion
                break;
            case Default:
            default:
                //region Generate Toast
                makeText(
                        context,
                        message,
                        R.color.toast_default,
                        R.color.toast_text,
                        null,
                        typeface,
                        duration);
                //endregion
        }
    }

    private static void makeText(Context context, String message, int backgroundColor, int textColor, int icon, @Duration int duration) {
        makeText(context, message, backgroundColor, textColor, icon, FontUtils.getTypeface(), duration);
    }

    public static void makeText(Context context, String message, Integer backgroundColor, Integer textColor, Integer icon, Typeface typeface, @Duration int duration) {
        LinearLayout layout = new LinearLayout(context);
        TextView tv = new TextView(context);

        //        tv.setTextSize(13);
        tv.setGravity(Gravity.CENTER);
        tv.setText(message);

        if (backgroundColor != null) {
            layout.setBackground(drawRoundRect(context, UIUtil.getColor(context, backgroundColor)));
        }

        if (textColor != null) {
            tv.setTextColor(UIUtil.getColor(context, textColor));
        }

        if (icon != null) {
            try {
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
                tv.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen._5sdp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (typeface != null) {
            tv.setTypeface(typeface);
        }

        layout.setPadding(
                dpToPixel(context, 10),
                dpToPixel(context, 6),
                dpToPixel(context, 10),
                dpToPixel(context, 6));

        layout.addView(tv);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setDuration(duration == LENGTH_SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);

//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 120);
        toast.show();
    }

    private static GradientDrawable drawRoundRect(Context context, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen._20sdp));
        shape.setColor(backgroundColor);
//        shape.setAlpha(1);

        shape.setStroke(1, UIUtil.getColor(context, R.color.toast_text));
        return shape;
    }

    private static int dpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
