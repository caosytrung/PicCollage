package vn.com.grooo.mediacreator.photomaker.ui.fragt;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.StaticLayout;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseFragment;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVImageStickerAdapter;

/**
 * Created by trungcs on 8/19/17.
 */

public class ImageStickerFragment extends BaseFragment implements IRVClickListener {
    private RecyclerView rvImageSticker;
    private RVImageStickerAdapter mAdapter;
    private List<String> pathList;
    private String type;
    private IAddImageSticker iAddImageSticker;

    public ImageStickerFragment(String type, Context context,IAddImageSticker iAddImageSticker){
        this.iAddImageSticker = iAddImageSticker;
        pathList = new ArrayList<>();
        try {
            String[] arrType =
                    context.getAssets().list(type);
            for (int i = 0 ; i < arrType.length ; i ++){
                pathList.add(type + "/" + arrType[i] );
            }

        } catch (IOException e) {
            e.printStackTrace();
            pathList = new ArrayList<>();
        }

    }



    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_image_sticker;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        rvImageSticker = (RecyclerView) rootView.findViewById(R.id.rvFragImageSticker);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        rvImageSticker.setLayoutManager(manager);

        mAdapter = new RVImageStickerAdapter(getContext());
        mAdapter.setListPath(pathList);
        mAdapter.setIrvClickListener(this);
        rvImageSticker.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData(Bundle saveInstanceState) {


    }

    @Override
    public void onItemClick(int pos, View v) {
        iAddImageSticker.addImageSticker(pathList.get(pos));
    }

    @Override
    public void onItemLongClick(int pos, View v) {
        iAddImageSticker.addImageSticker(pathList.get(pos));
    }

    public interface IAddImageSticker{
        public void addImageSticker(String path);
    }
}
