package vn.com.grooo.mediacreator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;

/**
 * Created by trungcs on 10/27/17.
 */

public class AboutActivity extends BaseActivity {
    private TextView tvAppStore;
    private TextView tvPolicy;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_about;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        tvAppStore = (TextView) findViewById(R.id.tvAppStore);
        tvPolicy = (TextView) findViewById(R.id.tvPolicy);


        SpannableString appStore = new SpannableString(tvAppStore.getText().toString());
        appStore.setSpan(new UnderlineSpan(), 0, tvAppStore.getText().toString().length(), 0);
        tvAppStore.setText(appStore);

        SpannableString policy = new SpannableString(tvPolicy.getText().toString());
        policy.setSpan(new UnderlineSpan(), 0, tvPolicy.getText().toString().length(), 0);
        tvPolicy.setText(policy);

        tvAppStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(AboutActivity.this)){
                    Intent browserIntent =
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://sites.google.com/view/privacy-policy-my-collages"));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(AboutActivity.this,"No internet access !",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
