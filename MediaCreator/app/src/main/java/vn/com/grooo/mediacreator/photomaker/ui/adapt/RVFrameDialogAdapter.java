package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.model.FrameObj;
import vn.com.grooo.mediacreator.photomaker.ui.custom.frame.ImageFrame;

/**
 * Created by trungcs on 8/16/17.
 */

public class RVFrameDialogAdapter extends
        RecyclerView.Adapter<RVFrameDialogAdapter.MyViewHolder> {
    private Context mContext;
    private IRVClickListener irvClickListener;
    private List<FrameObj> frameObjList;

    public RVFrameDialogAdapter(Context context){
        mContext = context;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    public void setFrameObjList(List<FrameObj> frameObjList) {
        this.frameObjList = frameObjList;
    }

    @Override
    public RVFrameDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).
                inflate(R.layout.item_frame_dialog,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RVFrameDialogAdapter.MyViewHolder holder, final int position) {
        // get input stream
        holder.ivFrame.setmFrameObj(frameObjList.get(position));
        holder.ivFrame.invalidate();
        holder.ivFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irvClickListener.onItemClick(position,view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return frameObjList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageFrame ivFrame;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivFrame = (ImageFrame)
                    itemView.findViewById(R.id.ivItemFrameDialog);

        }
    }
}
