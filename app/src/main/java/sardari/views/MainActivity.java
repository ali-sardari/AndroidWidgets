package sardari.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import sardari.utils.Utils;
import sardari.utils.toast.ToastMode;
import sardari.utils.toast.ToastUtils;
import sardari.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.init(this);

        ImageView btnOK = findViewById(R.id.ivTest);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.makeText(getApplicationContext(), "این یک تست است", ToastMode.Warning, ToastUtils.LENGTH_SHORT);

//                ToastUtils.makeText(
//                        getApplicationContext(),
//                        "این یک تست است...",
//                        R.color.colorAccent,
//                        R.color.textColorPrimaryInverse,
//                        R.drawable.icv_network,
//                        null,
//                        ToastUtils.LENGTH_SHORT);
            }
        });
    }
}
