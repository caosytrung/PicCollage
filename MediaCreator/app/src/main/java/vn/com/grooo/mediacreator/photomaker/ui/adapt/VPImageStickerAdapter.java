package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungcs on 8/19/17.
 */

public class VPImageStickerAdapter extends FragmentPagerAdapter {
    private List<Fragment> listFragment;



    public VPImageStickerAdapter(FragmentManager fm) {
        super(fm);
        listFragment = new ArrayList<>();
    }

    public void addFragment(Fragment fragment){
        listFragment.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
