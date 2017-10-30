package vn.com.grooo.mediacreator;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;

import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.FragmentUtils;
import vn.com.grooo.mediacreator.ui.MainFragment;
import vn.com.grooo.mediacreator.ui.MenuFragment;

public class MainActivity extends BaseActivity {

    private ImageView imgToolbarIcon;
    private TextView txtToolbarTitle;
    private Toolbar toolbar;
    private RippleView rvToolbar;

    private boolean isShow = false;

//    static {
//        System.loadLibrary("native-lib");
//    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        imgToolbarIcon = (ImageView) findViewById(R.id.imgToolbarIcon);
        txtToolbarTitle = (TextView) findViewById(R.id.tvToolBarTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbarCustom);

        rvToolbar = (RippleView) findViewById(R.id.rvToolbar);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        FragmentUtils.replaceFragment(new MainFragment(this), R.id.frmMain, "MAIN", this);

        imgToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
        txtToolbarTitle.setText("MEDIA CREATOR");
        toolbar.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
//        rvToolbar.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
//            @Override
//            public void onComplete(RippleView rippleView) {
//                // onclick on toolbar
//                if (isShow) {
//                    FragmentUtils.addFragment(new MainFragment(MainActivity.this), R.id.frmMain, "MAIN", MainActivity.this);
//                    isShow = false;
//                } else {
//                    FragmentUtils.addFragment(new MenuFragment(), R.id.frmMain, MainActivity.this);
//                    isShow = true;
//                }
//            }
//        });
    }

}
