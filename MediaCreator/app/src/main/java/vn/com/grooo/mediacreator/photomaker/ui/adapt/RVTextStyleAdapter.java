package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 8/21/17.
 */

public class RVTextStyleAdapter extends
        RecyclerView.Adapter<RVTextStyleAdapter.MyViewHolder> {
    private int selectPos;
    private List<String> listPath;
    private IRVClickListener irvClickListener;
    private Context mContext;

    public void setListPath(List<String> listPath) {
        this.listPath = listPath;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    public RVTextStyleAdapter(Context context){
        mContext = context;
        selectPos = 3;

    }


    @Override
    public RVTextStyleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_text_style_sticker,
                parent,false);

        return new MyViewHolder(v);
    }

    public void setSelectPos(int pos){
        selectPos = pos;
    }

    @Override
    public void onBindViewHolder(RVTextStyleAdapter.MyViewHolder holder, final int position) {
        String path =  listPath.get(position);
        String[] nameSttr = path.split("/");
        String nameTTF = nameSttr[nameSttr.length - 1];
        String result = nameTTF.substring(0,nameTTF.length() - 4);
        holder.tvTextStyle.setText(result);


        Utils.setFontStyle(mContext,listPath.get(position),holder.tvTextSample);
        if (position == selectPos){
            holder.tvTextStyle.setTextColor(Color.WHITE);
            holder.tvTextSample.setTextColor(Color.WHITE);
            holder.tvTextStyle.setTextSize(24);
            holder.tvTextSample.setTextSize(24);
        }

        holder.vContainer.setOnClickListener(new View.OnClickListener() {
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
        public TextView tvTextStyle;
        public TextView tvTextSample;
        public View vContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            vContainer = itemView;

            tvTextSample = (TextView)
                    itemView.findViewById(R.id.tvItemTextSampleSticker);
            tvTextStyle = (TextView)
                    itemView.findViewById(R.id.tvItemTextStyleSticker);
        }
    }
}
