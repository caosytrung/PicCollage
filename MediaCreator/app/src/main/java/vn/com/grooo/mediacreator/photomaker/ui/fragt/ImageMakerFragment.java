package vn.com.grooo.mediacreator.photomaker.ui.fragt;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseFragment;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.callback.IAddUri;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVImagePickMakerAdapter;

/**
 * Created by trungcs on 8/14/17.
 */

public class ImageMakerFragment extends BaseFragment implements IRVClickListener {
    private String mTitle;
    private ArrayList<String> mUriList;
    private RecyclerView rvImageMaker;
    private RVImagePickMakerAdapter mAdapter;
    private IAddUri iAddUri;
    public ImageMakerFragment(){

    }

    public void setiAddUri(IAddUri iAddUri) {
        this.iAddUri = iAddUri;
    }

    public ImageMakerFragment(String title, IAddUri iAddUri){
        mTitle = title;
        this.iAddUri = iAddUri;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_image_pick_maker;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        rvImageMaker = (RecyclerView) rootView.findViewById(R.id.rvImagePickMaker);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        rvImageMaker.setLayoutManager(manager);

    }

    @Override
    protected void initData(Bundle saveInstanceState) {

        mUriList = Utils.getUriFromTitle(getContext(),mTitle);
        mUriList.add(0,"camera");
        Log.d("TITLEe",mTitle + " -- " + mUriList.size() );
        mAdapter = new RVImagePickMakerAdapter(getContext());
        mAdapter.setIrvClickListener(this);
        mAdapter.setUriList(mUriList);
        rvImageMaker.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        mUriList = null;
        rvImageMaker = null;
    }

    @Override
    public void onItemClick(int pos, View v) {
        if (pos == 0){
            iAddUri.moveToCamera();
            return;
        }
        String uri = mUriList.get(pos);
        iAddUri.addUri(uri);
    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }


}
