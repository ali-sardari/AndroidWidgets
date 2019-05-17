package sardari.utils.toast;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sardari.utils.FontUtils;
import sardari.utils.UIUtil;
import sardari.utils.Utils;
import sardari.widget.R;

public class ToastUtils {
    private static int textColor;
    private Context context;

    public static void makeText(String message, ToastType type) {
        try {
            if (Utils.getContext() != null) {
                makeText(Utils.getContext(), message, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeText(Context context, String message) {
        makeText(context, message, ToastType.Default);
    }

    public static void makeText(Context context, String message, ToastType type) {
        LinearLayout layout = new LinearLayout(context);
        TextView tv = new TextView(context);

        switch (type) {
            case Success:
                setBackgroundAndTextColor(context, layout, R.color.toast_success, R.color.toast_text);
                break;
            case Error:
                setBackgroundAndTextColor(context, layout, R.color.toast_error, R.color.toast_text);
                break;
            case Info:
                setBackgroundAndTextColor(context, layout, R.color.toast_info, R.color.toast_text);
                break;
            case Warning:
                setBackgroundAndTextColor(context, layout, R.color.toast_warning, R.color.toast_text);
                break;
            case Default:
                setBackgroundAndTextColor(context, layout, R.color.toast_default, R.color.toast_text);
                break;
            default:
                setBackgroundAndTextColor(context, layout, R.color.toast_default, R.color.toast_text);
        }

        layout.setPadding(dpToPixel(context, 10), dpToPixel(context, 5), dpToPixel(context, 10), dpToPixel(context, 5));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(dpToPixel(context, 10), dpToPixel(context, 10), dpToPixel(context, 10), dpToPixel(context, 10));
        layout.setLayoutParams(params);

        // set the TextView properties like color, size etc
        tv.setTextColor(textColor);
        tv.setTextSize(14);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(FontUtils.getTypeface());

        // set the text you want to show in  Toast
        tv.setText(message);

        layout.addView(tv);

        Toast toast = new Toast(context); //context is object of Context write "this" if you are an Activity
        // Set The layout as Toast View
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);

        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 140);
        toast.show();
    }

    //------------------------------------------------------------------------------------------
    private static void setBackgroundAndTextColor(Context context, LinearLayout layout, int toast_color, int text_color) {
        layout.setBackground(drawRoundRect(UIUtil.getInstance().getColor(context, toast_color)));
        textColor = UIUtil.getInstance().getColor(context, text_color);
    }

    private static GradientDrawable drawRoundRect(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20);
        shape.setColor(backgroundColor);
//        shape.setAlpha(1);

        shape.setStroke(1, UIUtil.getInstance().getColor(R.color.toast_text));
        return shape;
    }

    private static int dpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    //------------------------------------------------------------------------------------------
    public enum ToastType {
        Default, Success, Warning, Error, Info
    }
}
