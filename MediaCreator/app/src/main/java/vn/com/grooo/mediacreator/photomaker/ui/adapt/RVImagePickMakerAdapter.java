package vn.com.grooo.mediacreator.photomaker.ui.adapt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 8/15/17.
 */

public class RVImagePickMakerAdapter extends RecyclerView.Adapter<RVImagePickMakerAdapter.MyViewholder> {
    private static final String TAG = "RVImagePickMakerAdapterr";
    private List<String> uriList;
    private Context mContext;
    private IRVClickListener irvClickListener;

    public RVImagePickMakerAdapter(Context context){
        mContext = context;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public void setIrvClickListener(IRVClickListener irvClickListener) {
        this.irvClickListener = irvClickListener;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).
                inflate(R.layout.item_pick_image_maker,parent,false);
        switch (viewType){
            case 1:
                return  new MyViewHolderType1(v);
            case 2:
                return  new MyViewHolderType2(v);
            default:
                return  new MyViewHolderType1(v);
        }


    }

    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
        if (position == 0 ){
          //  Log.d(TAG,"set holder for postion 1");
            MyViewHolderType1 myViewHolderType1 = (MyViewHolderType1) holder;

            myViewHolderType1.ivImage.setBackgroundColor(Color.BLACK);
            myViewHolderType1.ivImage.setImageDrawable(mContext.getDrawable(R.drawable.icon_white_camera));

            myViewHolderType1.ivImage.setPadding(64,64,64,64);
            myViewHolderType1.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    irvClickListener.onItemClick(position,view);
                }
            });


        } else {

            MyViewHolderType2 myViewHolderType2 = (MyViewHolderType2) holder;
            File file = new File(uriList.get(position));
            Glide.with(mContext).load(file).into(((MyViewHolderType2) holder).ivImage);


          //  decodeFile(uriList.get(position),myViewHolderType2.ivImage);
//            Bitmap bitmap = Utils.getBitMapFromFile(uriList.get(position));
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);


//            String uri = uriList.get(position);
//            File file = new File(uri);
//            String decodedImgUri = Uri.fromFile(file).toString();
//            try {
//                ImageLoader.getInstance().
//                        displayImage(decodedImgUri, myViewHolderType2.ivImage);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
            myViewHolderType2.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    irvClickListener.onItemClick(position,view);
                }
            });


        }
    }

    public  void decodeFile(String filePath, ImageView image) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 512;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(mContext)
                .load(stream.toByteArray())
                .asBitmap()
                .into(image);

        //image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        public MyViewholder(View itemView) {
            super(itemView);
        }
    }

    public class MyViewHolderType1 extends MyViewholder{
        private ImageView ivImage;

        public MyViewHolderType1(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivItemPickMaker);
        }
    }

    public class MyViewHolderType2 extends RVImagePickMakerAdapter.MyViewholder{
        private ImageView ivImage;
        public MyViewHolderType2(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivItemPickMaker);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return  1;
        }
        return  2;
    }
}
