package vn.com.grooo.mediacreator.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Thaihn on 8/7/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract int getLayoutResource();

    protected abstract void initVariables(Bundle saveInstanceState, View rootView);

    protected abstract void initData(Bundle saveInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        initVariables(savedInstanceState, rootView);
        initData(savedInstanceState);
        return rootView;
    }
}