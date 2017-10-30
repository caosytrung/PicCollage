package vn.com.grooo.mediacreator.saveandroidloadproject.ui.act;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.annotations.Until;

import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.saveandroidloadproject.db.CTDatabaseHelper;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.CollageOb;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.adapter.RVListProjectAdapter;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.adapter.VPObjectListAdapter;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.custom.MultiViewPager;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.frag.ProjectCollageFragment;

/**
 * Created by trungcs on 9/14/17.
 */

public class ProjectListActivity extends BaseActivity implements  ViewPager.OnPageChangeListener {
    private ViewPager vpObList;
    private ImageView ivBack;
    private TextView tvBackToHome;
    private VPObjectListAdapter mAdapter;
    private  List<CollageOb> collageObList;
    private ImageView ivRemoveProject;
    private TextView tvEmpty;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_project_list;

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        tvEmpty = (TextView) findViewById(R.id.tvEmptyText);
        vpObList = (ViewPager) findViewById(R.id.vpProjectList);
        ivBack = (ImageView) findViewById(R.id.ivBackToMain);
        ivRemoveProject = (ImageView) findViewById(R.id.ivRemoveProject);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vpObList.setPageMargin(16);

        // set the number of pages that should be retained to either side of the current page

        mAdapter = new VPObjectListAdapter(getSupportFragmentManager());
         collageObList = CTDatabaseHelper.getINSTANCE(this).getListRecord();
        Log.d("REcordount" , collageObList.size() +  "" );
        mAdapter.setCollageObList(collageObList);
        mAdapter.addFragment();

        vpObList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        vpObList.setOffscreenPageLimit(100);
        vpObList.setCurrentItem(mAdapter.getCollageObList().size() / 2, false);
        vpObList.addOnPageChangeListener(this);
        if (collageObList.size() == 0){
            ivRemoveProject.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }


    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        ivRemoveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvEmpty.setVisibility(View.GONE);
                int position = vpObList.getCurrentItem();
                int id = collageObList.get(position ).getId();
                CTDatabaseHelper.getINSTANCE(ProjectListActivity.this).deteteRecord(id);
                Log.d("Currentitem" , position + " " + collageObList.size());
                collageObList.remove(position );
                mAdapter.getFragmentList().remove(position);
                mAdapter.notifyDataSetChanged();
                if (collageObList.size() == 0){
                    ivRemoveProject.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
                Toast.makeText(ProjectListActivity.this,
                        "Delete Successfully ",Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      Log.d("CurrentitemS" , position + " ");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
