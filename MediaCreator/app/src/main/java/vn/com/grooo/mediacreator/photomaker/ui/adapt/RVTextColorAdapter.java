package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 8/21/17.
 */

public class RVTextColorAdapter extends RecyclerView.Adapter<RVTextColorAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> colorList;
    private IRVClickListener irvClickListener;

    public RVTextColorAdapter(Context context){
        mContext = context;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    @Override
    public RVTextColorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_text_color_sticker,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RVTextColorAdapter.MyViewHolder holder, final int position) {
        holder.ivColor.setBackgroundColor(Color.parseColor(colorList.get(position)));
        holder.ivColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivColor;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivColor = (ImageView) itemView.findViewById(R.id.ivItemTextColorSticker);
        }
    }
}
