package sardari.utils.toast

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import sardari.utils.FontUtils
import sardari.utils.UIUtil
import sardari.widget.R

class ToastUtils
{
    companion object
    {
        const val LENGTH_SHORT = 0
        const val LENGTH_LONG = 1

        @IntDef(value = [LENGTH_SHORT, LENGTH_LONG])
        @Retention(AnnotationRetention.RUNTIME)
        internal annotation class Duration

        @JvmOverloads
        @JvmStatic
        fun makeText(context: Context, message: String, mode: ToastMode = ToastMode.Default, @Duration duration: Int = LENGTH_SHORT)
        {
            val typeface: Typeface? = try
            {
                FontUtils.getTypeface()
            } catch (e: Exception)
            {
                null
            }

            val backgroundColor: Int
            val textColor: Int
            val icon: Int?
            when (mode)
            {
                ToastMode.Success ->
                {
                    //region Generate Toast
                    backgroundColor = R.color.toast_success
                    textColor = R.color.toast_text
                    icon = R.drawable.icv_success
                }
                ToastMode.Error ->
                {
                    //region Generate Toast
                    backgroundColor = R.color.toast_error
                    textColor = R.color.toast_text
                    icon = R.drawable.icv_error
                }
                ToastMode.Info ->
                {
                    //region Generate Toast
                    backgroundColor = R.color.toast_info
                    textColor = R.color.toast_text
                    icon = R.drawable.icv_info
                }
                ToastMode.Warning ->
                {
                    //region Generate Toast
                    backgroundColor = R.color.toast_warning
                    textColor = R.color.toast_text
                    icon = R.drawable.icv_warning
                }
                ToastMode.Default ->
                {
                    //region Generate Toast
                    backgroundColor = R.color.toast_default
                    textColor = R.color.toast_text
                    icon = null
                }
            }

            makeText(
                context, message, backgroundColor, textColor, icon, typeface, duration
            )
        }

        @JvmStatic
        fun makeText(
            context: Context,
            message: String,
            backgroundColor: Int? = null,
            textColor: Int? = null,
            icon: Int? = null,
            typeface: Typeface? = null,
            @Duration duration: Int = LENGTH_SHORT
        )
        {
            val layout = LinearLayout(context)
            val tv = TextView(context)

            tv.gravity = Gravity.CENTER
            tv.text = message
            if (backgroundColor != null)
            {
                try
                {
                    layout.background = drawRoundRect(
                        context, UIUtil.getColor(context, backgroundColor)
                    )
                } catch (e: Exception)
                {
                    layout.background = drawRoundRect(
                        context, UIUtil.getColor(context, R.color.toast_default)
                    )
                }
            }

            if (textColor != null)
            {
                try
                {
                    tv.setTextColor(UIUtil.getColor(context, textColor))
                } catch (e: Exception)
                {
                    tv.setTextColor(UIUtil.getColor(context, R.color.toast_text))
                }
            }

            if (icon != null)
            {
                try
                {
                    tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
                    tv.compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen._5sdp)
                } catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }

            if (typeface != null)
            {
                tv.typeface = typeface
            }

            layout.setPadding(
                dpToPixel(context, 10),
                dpToPixel(context, 6),
                dpToPixel(context, 10),
                dpToPixel(context, 6)
            )
            layout.addView(tv)
            val toast = Toast(context)
            toast.view = layout
            toast.duration = if (duration == LENGTH_SHORT) Toast.LENGTH_SHORT else Toast.LENGTH_LONG

//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 120);
            toast.show()
        }

        private fun drawRoundRect(context: Context, backgroundColor: Int): GradientDrawable
        {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = context.resources.getDimensionPixelSize(R.dimen._20sdp).toFloat()
            shape.setColor(backgroundColor)
            //        shape.setAlpha(1);
            shape.setStroke(1, UIUtil.getColor(context, R.color.toast_text))
            return shape
        }

        private fun dpToPixel(context: Context, dp: Int): Int
        {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
            ).toInt()
        }
    }
}