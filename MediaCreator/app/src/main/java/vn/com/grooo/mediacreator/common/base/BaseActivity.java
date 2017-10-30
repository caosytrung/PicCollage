package vn.com.grooo.mediacreator.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Thaihn on 8/7/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResources();

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());
        initVariables(savedInstanceState);
        initData(savedInstanceState);

    }
}