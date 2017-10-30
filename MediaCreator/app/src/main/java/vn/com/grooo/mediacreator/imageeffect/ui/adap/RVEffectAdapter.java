package vn.com.grooo.mediacreator.imageeffect.ui.adap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.wysaid.view.ImageGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.BrightnessFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.ColorDepth;
import vn.com.grooo.mediacreator.imageeffect.filter.ColorFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.ContractFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.GammaFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.GreyScaleFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.base.BaseFilterFactory;
import vn.com.grooo.mediacreator.photoeffect.models.FilterObject;
import vn.com.grooo.mediacreator.photoeffect.ui.utils.FilterUtils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 9/6/17.
 */

public class RVEffectAdapter extends RecyclerView.Adapter<RVEffectAdapter.MyViewHolder> {
    private List<FilterObject> filterObjectList;
    private Context mContext;
    private IRVClickListener irvClickListener;
    private Bitmap mBitmap;
    private int selectPost;


    public RVEffectAdapter(Context context){
        mContext = context;

    }

    public void setFilterFactories(List<FilterObject> filterObjectList) {
        this.filterObjectList = filterObjectList;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {

        this.irvClickListener = irvClickListener;
    }

    @Override
    public RVEffectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_choose_effect,parent,false);

        return new MyViewHolder(v);
    }

    public void setSelectPost(int selectPost) {
        this.selectPost = selectPost;
    }

    @Override
    public void onBindViewHolder(final RVEffectAdapter.MyViewHolder holder, final int position) {

        if (position == selectPost){
            holder.cvItemProject.setCardBackgroundColor(Color.RED);
        }
        final int offset = filterObjectList.get(position).getOffset();
//        /holder.ivEffect.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_SCALE_TO_FILL);

//        holder.ivEffect.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
//            @Override
//            public void surfaceCreated() {
//                holder.ivEffect.setImageBitmap(tmp);
//                holder.ivEffect.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        holder.ivEffect.setFilterWithConfig(FilterUtils.EFFECT_CONFIGS[offset]);
//                        holder.ivEffect.setFilterIntensity(0.6f);
//                    }
//                });
//            }
//        });
        holder.tvName.setText(filterObjectList.get(position).getName());
        InputStream ims = null;
        String path = "filter/fil" + position + ".jpg";
        try {
            ims = mContext.getAssets().open(path);
            Drawable d = Drawable.createFromStream(ims, null);
            holder.ivEffect.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }



        holder.ivEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterObjectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivEffect;
        private TextView tvName;
        private CardView cvItemProject;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivEffect = (ImageView) itemView.findViewById(R.id.ivItemChooseEffec);
            tvName = (TextView) itemView.findViewById(R.id.tvItemChooseEffect);
            cvItemProject = (CardView) itemView.findViewById(R.id.cvItemProject);

        }
    }
}
