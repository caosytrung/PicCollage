package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 8/15/17.
 */

public class RVImageToCollageAdapter extends
        RecyclerView.Adapter<RVImageToCollageAdapter.MyViewHolder> {
    private Context mContext;
    private IRVClickListener irvClickListener;
    private List<String> uriList;
    public RVImageToCollageAdapter(Context context){
        mContext = context;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image_to_collage,
                parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        try {
            File file = new File(uriList.get(position));
            Glide.with(mContext).load(file).into(holder.ivIamge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.ivIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIamge;
        public ImageView ivRemove;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivIamge = (ImageView) itemView.findViewById(R.id.ivItemImageToCollage);
            ivRemove = (ImageView) itemView.findViewById(R.id.ivItemRemoveImageForCollage);
        }
    }
}
