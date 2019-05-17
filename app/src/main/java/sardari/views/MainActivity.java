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
                ToastUtils.makeText("این یک تست است...", ToastUtils.ToastType.Info);

                Log.w("btnOK","Click...........");
            }
        });
    }
}
