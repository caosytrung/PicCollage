package vn.com.grooo.mediacreator.photomaker.ui.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVFrameDialogAdapter;

/**
 * Created by trungcs on 8/16/17.
 */

public class FrameDialog extends Dialog implements IRVClickListener {
    private List<String> frameListPath;
    private RecyclerView rvFrame;
    private RVFrameDialogAdapter mAdapter;
    private IFrameDialogCallback iFrameDialogCallback;


    public FrameDialog(@NonNull Context context,int theme,
                       ArrayList<String> frameListPath) {
        super(context,theme);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewPager.LayoutParams.WRAP_CONTENT,
                ViewPager.LayoutParams.WRAP_CONTENT);

        getWindow().setGravity(Gravity.CENTER);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);
        setCancelable(true);
        getWindow().setBackgroundDrawable(d);
        setContentView(R.layout.dialog_frame);
        this.frameListPath = frameListPath;
        initControl();

    }

    public void setiFrameDialogCallback(IFrameDialogCallback iFrameDialogCallback) {
        this.iFrameDialogCallback = iFrameDialogCallback;
    }

    private void initControl() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvFrame = (RecyclerView) findViewById(R.id.rvFrameDialog);
        rvFrame.setLayoutManager(manager);
        mAdapter = new RVFrameDialogAdapter(getContext());
       // mAdapter.setFrameAssestPaths(frameListPath);
        mAdapter.setIrvClickListener(this);
        rvFrame.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }



    public FrameDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onItemClick(int pos, View v) {
        dismiss();
        iFrameDialogCallback.changeFrame(pos);
    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }

    public interface IFrameDialogCallback{
        void changeFrame(int pos);
    }
}
