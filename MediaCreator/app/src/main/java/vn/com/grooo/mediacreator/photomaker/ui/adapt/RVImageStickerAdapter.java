package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 8/19/17.
 */

public class RVImageStickerAdapter extends
        RecyclerView.Adapter<RVImageStickerAdapter.MyViewHolder> {

    private List<String> listPath;
    private Context mContext;
    private IRVClickListener irvClickListener;

    public RVImageStickerAdapter(Context context){
        mContext = context;
    }

    public void setListPath(List<String> listPath) {
        this.listPath = listPath;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    @Override
    public RVImageStickerAdapter.
            MyViewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View v = LayoutInflater.from(mContext).
                inflate(R.layout.item_image_sticker_fragment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RVImageStickerAdapter.MyViewHolder holder, final int position) {
        String path = listPath.get(position);
        InputStream ims = null;
        try {
            ims = mContext.getAssets().open(path);
            Drawable d = Drawable.createFromStream(ims, null);
            holder.ivSticker.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.ivSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPath.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSticker;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivSticker = (ImageView)
                    itemView.findViewById(R.id.ivItemImageStickerFragment);
        }
    }
}
