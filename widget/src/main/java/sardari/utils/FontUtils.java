package sardari.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.SimpleArrayMap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtils {
    private static final String PATH = "fonts/%s.ttf";
    private static final SimpleArrayMap<String, Typeface> cache = new SimpleArrayMap<>();
    private static Typeface typeface;
    private static FontUtils.Font font = new FontUtils().new Font();

    //----------------------------------------------------------------------------------------
    private static Typeface GetFontFromCache(String fontName) {
        synchronized (cache) {
            String path = String.format(PATH, fontName);
            if (!cache.containsKey(fontName)) {
                Typeface t = Typeface.createFromAsset(Utils.getContext().getAssets(), path);
                cache.put(fontName, t);
                return t;
            }
            return cache.get(fontName);
        }
    }

    public static Typeface getTypeface() {
        return font.getTypeface();
    }

    public static Font Default() {
        return F_IranYekan();
    }

    //----------------------------------------------------------------------------------------
    public static Font F_Consolas() {
        typeface = GetFontFromCache("Consolas");
        return font;
    }

    public static Font F_IranYekan() {
        typeface = GetFontFromCache("IranYekan");
        return font;
    }

    //----------------------------------------------------------------------------------------
    public class Font {
        public Typeface getTypeface() {
            if (typeface == null) {
                Default();
            }

            return typeface;
        }

        public void setTypeface(Typeface typeface) {
            FontUtils.typeface = typeface;
        }

        //----------------------------------------------------------------------------------------
        public void setFont(TextInputLayout view) {
            view.setTypeface(typeface);

            View child = view.getChildAt(0);
            setFont(child);
        }

        public void setFont(View view) {
            try {
                if (view instanceof ViewGroup && !(view instanceof TextInputLayout)) {
                    ViewGroup vg = (ViewGroup) view;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View child = vg.getChildAt(i);
                        setFont(child);
                    }
                } else if (view instanceof TextView) {
                    ((TextView) view).setTypeface(typeface);
                } else if (view instanceof TextInputLayout) {
                    ((TextInputLayout) view).setTypeface(typeface);

                    ViewGroup vg = (ViewGroup) view;
                    View child = vg.getChildAt(0);
                    setFont(child);
                }
            } catch (Exception ignored) {
            }
        }

        public void setFont(TextView view) {
            view.setTypeface(typeface);
        }

        public void setMenuFont(NavigationView nav) {
            Menu m = nav.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for applying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                applyFontToMenuItem(mi);
            }
        }

        public void applyFontToMenuItem(MenuItem mi) {
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
        }

        public SpannableStringBuilder applyFontToMenuItem(String string) {
            SpannableStringBuilder SS = new SpannableStringBuilder(string);
            SS.setSpan(new CustomTypefaceSpan("", typeface), 0, string.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            return SS;
        }
    }

    //----------------------------------------------------------------------------------------
    class CustomTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }
    }
}
