package sardari.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import sardari.utils.Utils;
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
                ToastUtils.makeText(
                        getApplicationContext(),
                        "این یک تست است...",
                        R.color.colorAccent,
                        R.color.textColorPrimaryInverse,
                        R.drawable.icv_network,
                        null,
                        ToastUtils.LENGTH_SHORT);

//                ToastUtils.makeText(getApplicationContext(),"این یک تست است...", ToastMode.Default, ToastUtils.LENGTH_SHORT);

//                ToastUtils.makeText(getApplicationContext(),"این یک تست است...");
//                ToastUtils.makeText("این یک تست است...", ToastMode.Error, ToastUtils.LENGTH_LONG);
//                Toast.makeText(MainActivity.this, "این یک تست است.", Toast.LENGTH_LONG).show();
                Log.w("btnOK", "Click...........");
            }
        });
    }

    public void test(View view) {
    }
}
