package vn.com.grooo.mediacreator.saveandroidloadproject.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.CollageOb;

/**
 * Created by trungcs on 10/4/17.
 */

public class RVListProjectAdapter extends RecyclerView.Adapter<RVListProjectAdapter.MyViewholder> {
    private List<CollageOb>  collageObList;
    private Context mContext;
    private IRVClickListener irvClickListener;
    private int selectPos;

    public  RVListProjectAdapter(Context context){
        mContext = context;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(mContext).inflate(R.layout.item_rv_list_project,null);
        return new MyViewholder(view);
    }

    public void setCollageObList(List<CollageOb> collageObList,
                                 IRVClickListener irvClickListener) {
        this.collageObList = collageObList;
        this.irvClickListener = irvClickListener;
    }
    public void setSelectPos(int pos){
        selectPos = pos;
    }

    public int getSelectPos() {
        return selectPos;
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
        if (position ==  selectPos){
            holder.ivForegroud.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(Color.RED);
        }
        try {
            FileInputStream is = mContext.
                    openFileInput(collageObList.get(position).getFileName());
            Bitmap bmp = BitmapFactory.decodeStream(is);
            holder.ivProject.setImageBitmap(bmp);
            Log.d("CreteBG",true + "");

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CreteBG",false + e.getMessage());
        }
        holder.ivProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collageObList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        private ImageView ivProject;
        private ImageView ivForegroud;
        private CardView cardView;
        public MyViewholder(View itemView) {
            super(itemView);
            ivProject = (ImageView) itemView.findViewById(R.id.ivItemListObject);
            ivForegroud = (ImageView) itemView.findViewById(R.id.ivItemFore);

            cardView = (CardView) itemView.findViewById(R.id.cvBorder);
        }
    }
}
