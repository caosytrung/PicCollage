package vn.com.grooo.mediacreator.saveandroidloadproject.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.CollageOb;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.frag.ProjectCollageFragment;

/**
 * Created by trungcs on 9/14/17.
 */

public class VPObjectListAdapter extends FragmentStatePagerAdapter {
    public  int max;
    public static int LOOPS_COUNT = 1000;
    private List<CollageOb> collageObList;
    private List<Fragment> fragmentList;

    public VPObjectListAdapter(FragmentManager fm) {
        super(fm);
        max = 0;
        collageObList = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    public void setCollageObList(List<CollageOb> collageObList) {
        this.collageObList = collageObList;
    }
    public void addFragment(){
        for (int i = 0 ; i < collageObList.size() ; i ++){
            CollageOb collageOb = collageObList.get(i);
            Fragment a = new ProjectCollageFragment();
            Bundle args = new Bundle();

            args.putInt(ProjectCollageFragment.KEY_ID, collageOb.getId());
            args.putString(ProjectCollageFragment.KEY_FILE,collageOb.getFileName());
            Log.d("GetLisyINdorrrr",  "---- "+ collageOb.getFileName());
            a.setArguments(args);
           fragmentList.add(a);
        }
    }

    public List<CollageOb> getCollageObList() {
        return collageObList;
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
//        CollageOb collageOb = collageObList.get(position);
//        Fragment a = new ProjectCollageFragment();
//        Bundle args = new Bundle();
//
//        args.putInt(ProjectCollageFragment.KEY_ID, collageOb.getId());
//        args.putString(ProjectCollageFragment.KEY_FILE,collageOb.getFileName());
//        Log.d("GetLisyINdorrrr",  "---- "+ collageOb.getFileName());
//        a.setArguments(args);
        return fragmentList.get(position);


    }



    @Override
    public int getCount() {
        return collageObList.size() ;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    //    private List<CollageOb> collageObList;
//    private Context mContext;
//    public VPObjectListAdapter(Context context){
//        collageObList = new ArrayList<>();
//        mContext = context;
//    }
//
//    public void setCollageObList(List<CollageOb> collageObList) {
//        this.collageObList = collageObList;
//
//    }
//
//    @Override
//    public int getCount() {
//        return collageObList.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//
//        if (view == (View) object) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.item_object_list, container,false);
//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        ((RelativeLayout) view.findViewById(R.id.rvvvvvvv)).setBackgroundColor(color);
//        container.addView(view
//        );
//
//        return view;
//    }
//
//    @Override
//    public float getPageWidth(int position) {
//        return super.getPageWidth(position);
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//
//        ((ViewPager) container)
//                .removeView((view));
//    }
//    @Override
//    public int getItemPosition(Object object) {
//        return PagerAdapter.POSITION_NONE;
//    }
}
