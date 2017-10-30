package vn.com.grooo.mediacreator.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseFragment;
import vn.com.grooo.mediacreator.photomaker.ui.act.PickImageMakerActivity;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.act.ProjectListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();


    private Context context;

    private CardView cvFilter, cvFrame, cvFrameEmpty;
    private RippleView rvFilter, rvFrame;
    private Button btnListProject;

    public MainFragment() {

    }

    public MainFragment(AppCompatActivity context) {
        this.context = context;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        rvFilter = rootView.findViewById(R.id.rvFilter);
        rvFrame = rootView.findViewById(R.id.rvFrame);
        cvFilter = rootView.findViewById(R.id.cvFilter);
        cvFrame = rootView.findViewById(R.id.cvFrame);
        cvFrameEmpty = rootView.findViewById(R.id.cvFrameEmpty);
        btnListProject = rootView.findViewById(R.id.btnListProject);
        btnListProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(getContext(), ProjectListActivity.class));
            }
        });

    }

    @Override
    protected void initData(Bundle saveInstanceState) {

        // set layout
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        LinearLayout.LayoutParams paramFilter = new LinearLayout.LayoutParams((displayMetrics.widthPixels - 120) / 2, (displayMetrics.widthPixels - 120) / 2);
        paramFilter.setMargins(40, 40, 20, 40);
        LinearLayout.LayoutParams paramFrame = new LinearLayout.LayoutParams((displayMetrics.widthPixels - 120) / 2, (displayMetrics.widthPixels - 120) / 2);
        paramFrame.setMargins(20, 40, 40, 40);
        LinearLayout.LayoutParams paramFrameEmpty = new LinearLayout.LayoutParams(displayMetrics.widthPixels, (displayMetrics.widthPixels - 120) / 2);
        paramFrame.setMargins(80, 40, 40, 80);
        cvFilter.setLayoutParams(paramFilter);
        cvFrame.setLayoutParams(paramFrame);
        cvFrameEmpty.setLayoutParams(paramFrameEmpty);

        // click

        rvFilter.setRippleColor(R.color.md_grey_700);
        rvFilter.setRippleDuration(200);
        rvFilter.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(context, PickImageMakerActivity.class);
                intent.putExtra(Constants.KIND_CHOOSE, Constants.FILTER_IMAGE_CHOOSE);
                context.startActivity(intent);
            }
        });

        rvFrame.setRippleColor(R.color.md_grey_700);
        rvFrame.setRippleDuration(200);
        rvFrame.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(context, PickImageMakerActivity.class);
                intent.putExtra(Constants.KIND_CHOOSE, Constants.FRAME_IMAGE_CHOOSE);
                context.startActivity(intent);

            }
        });


    }
}
