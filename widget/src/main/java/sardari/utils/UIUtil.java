package sardari.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UIUtil {
    //region Singleton
    private static volatile UIUtil instance;
    private static final Object mutex = new Object();

    public static UIUtil getInstance() {
        UIUtil result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new UIUtil();
                }
            }
        }
        return result;
    }
    //endregion

    public void setRTL(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void hideKeyboard(Dialog dialog) {
        try {
            View view = dialog.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void showKeyboard(Activity activity, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }

    public int getColor(int id) {
        return getColor(Utils.getContext(), id);
    }

    public Drawable getDrawable(int ResID) {
        return ResourcesCompat.getDrawable(Utils.getContext().getResources(), ResID, null);
    }

    public Drawable getDrawableFromUri(Uri uri) {
        Drawable drawable = null;
        try {
            InputStream inputStream = Utils.getContext().getContentResolver().openInputStream(uri);
            drawable = Drawable.createFromStream(inputStream, uri.toString());
        } catch (FileNotFoundException e) {
            return null;
        }

        return drawable;
    }

    public int getThemeColor(Context context, int colorId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorId, typedValue, true);
        return typedValue.data;
    }

    public Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    public Bitmap takeScreenShot(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
//        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        Bitmap cache = decorView.getDrawingCache();
        if (cache == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cache, 0, statusBarHeight, decorView.getMeasuredWidth(), decorView.getMeasuredHeight() - statusBarHeight);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    public int getScreenWidth(Context context) {
        return getScreenSize(context, null).x;
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = Utils.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Utils.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getDimensionInPixelResource(int i) {
        return Utils.getContext().getResources().getDimensionPixelSize(i);
    }

    public Display getDisplaySize() {
        return ((WindowManager) Utils.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public Point getScreenSize(Context context, Point outSize) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point ret = outSize == null ? new Point() : outSize;
        Display defaultDisplay = null;

        if (wm != null) {
            defaultDisplay = wm.getDefaultDisplay();
        }

        if (defaultDisplay != null) {
            defaultDisplay.getSize(ret);
        }
        return ret;
    }

    public int getScreenHeight(Context context) {
        return getScreenSize(context, null).y;
    }

    public int getDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public void hiddenView(final ViewGroup view) {
//        AnimUtils.with(Techniques.FadeOut)
//                .duration(1000)
//                .onEnd(animator -> view.setVisibility(View.GONE))
//                .playOn(view);
    }

    public void showView(final ViewGroup view) {
//        view.setVisibility(View.VISIBLE);
//        AnimUtils.with(Techniques.FadeIn)
//                .duration(1000)
//                .playOn(view);
    }

    public void setTintedCompoundDrawable(TextView textView, int drawableRes, int tintRes) {
        textView.setCompoundDrawablesWithIntrinsicBounds(
                null,  // Left
                null, // Top
                tintDrawable(getDrawable(drawableRes), getColor(tintRes)), // Right
                null); //Bottom
        // if you need any space between the icon and text.
        textView.setCompoundDrawablePadding(12);
    }

    public Drawable tintDrawable(Drawable drawable, int tint) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, tint);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);

        return drawable;
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int DpToPx(Context context, int dpId) {
        return (int) context.getResources().getDimension(dpId);
    }

    public float dpToPixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static int dpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void setHtmlToTextView(TextView textView, String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }

        textView.setText(result);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
