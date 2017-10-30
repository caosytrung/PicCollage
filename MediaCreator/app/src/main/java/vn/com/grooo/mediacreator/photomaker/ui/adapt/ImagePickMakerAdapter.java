package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungcs on 8/14/17.
 */

public class ImagePickMakerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;


    public ImagePickMakerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }



    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
