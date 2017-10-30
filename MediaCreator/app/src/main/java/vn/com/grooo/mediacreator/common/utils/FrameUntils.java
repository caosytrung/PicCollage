package vn.com.grooo.mediacreator.common.utils;

import android.content.Context;
import android.text.LoginFilter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.model.FrameObj;
import vn.com.grooo.mediacreator.photomaker.model.ShapeInsideFrame;

/**
 * Created by trungcs on 8/15/17.
 */

public class FrameUntils {
    public static List<FrameObj>  getListType2(){
        List<FrameObj> frameObjList = new ArrayList<>();
        ShapeInsideFrame shapeInsideFrame11 = new ShapeInsideFrame(0,100,0,50);
        ShapeInsideFrame shapeInsideFrame12 = new ShapeInsideFrame(0,100,50,100);
        List<ShapeInsideFrame> shapeInsideFrameList1 = new ArrayList<>();
        shapeInsideFrameList1.add(shapeInsideFrame11);
        shapeInsideFrameList1.add(shapeInsideFrame12);
        frameObjList.add(new FrameObj(shapeInsideFrameList1, "frame/type2/f1.png"));

        ShapeInsideFrame shapeInsideFrame21 = new ShapeInsideFrame(0,50,0,100);
        ShapeInsideFrame shapeInsideFrame22 = new ShapeInsideFrame(50,100,0,100);
        List<ShapeInsideFrame> shapeInsideFrameList2 = new ArrayList<>();
        shapeInsideFrameList2.add(shapeInsideFrame21);
        shapeInsideFrameList2.add(shapeInsideFrame22);
        frameObjList.add(new FrameObj(shapeInsideFrameList2, "frame/type2/f2.png"));

        ShapeInsideFrame shapeInsideFrame31 = new ShapeInsideFrame(0,100,0,33.33);
        ShapeInsideFrame shapeInsideFrame32 = new ShapeInsideFrame(0,100,33.33,100);
        List<ShapeInsideFrame> shapeInsideFrameList3 = new ArrayList<>();
        shapeInsideFrameList3.add(shapeInsideFrame31);
        shapeInsideFrameList3.add(shapeInsideFrame32);
        frameObjList.add(new FrameObj(shapeInsideFrameList3, "frame/type2/f3.png"));


        return frameObjList;
    }

    public static List<FrameObj> getListType3(){
        List<FrameObj> frameObjList = new ArrayList<>();
        ShapeInsideFrame shapeInsideFrame11 = new ShapeInsideFrame(0,100,0,33.33);
        ShapeInsideFrame shapeInsideFrame12 = new ShapeInsideFrame(0,100,33.33,66.67);
        ShapeInsideFrame shapeInsideFrame13 = new ShapeInsideFrame(0,100,66.67,100);
        List<ShapeInsideFrame> shapeInsideFrameList1 = new ArrayList<>();
        shapeInsideFrameList1.add(shapeInsideFrame11);
        shapeInsideFrameList1.add(shapeInsideFrame12);
        shapeInsideFrameList1.add(shapeInsideFrame13);
        frameObjList.add(new FrameObj(shapeInsideFrameList1, "frame/type3/f1.png"));

        ShapeInsideFrame shapeInsideFrame21 = new ShapeInsideFrame(0,33.33,0,100);
        ShapeInsideFrame shapeInsideFrame22 = new ShapeInsideFrame(33.33,66.67,0,100);
        ShapeInsideFrame shapeInsideFrame23 = new ShapeInsideFrame(66.67,100,0,100);
        List<ShapeInsideFrame> shapeInsideFrameList2 = new ArrayList<>();
        shapeInsideFrameList2.add(shapeInsideFrame21);
        shapeInsideFrameList2.add(shapeInsideFrame22);
        shapeInsideFrameList2.add(shapeInsideFrame23);
        frameObjList.add(new FrameObj(shapeInsideFrameList2, "frame/type3/f2.png"));

        ShapeInsideFrame shapeInsideFrame31 = new ShapeInsideFrame(0,50,0,100);
        ShapeInsideFrame shapeInsideFrame32 = new ShapeInsideFrame(50,100,0,50);
        ShapeInsideFrame shapeInsideFrame33 = new ShapeInsideFrame(50,100,50,100);
        List<ShapeInsideFrame> shapeInsideFrameList3 = new ArrayList<>();
        shapeInsideFrameList3.add(shapeInsideFrame31);
        shapeInsideFrameList3.add(shapeInsideFrame32);
        shapeInsideFrameList3.add(shapeInsideFrame33);
        frameObjList.add(new FrameObj(shapeInsideFrameList3, "frame/type3/f3.png"));

        return frameObjList;
    }

    public static List<FrameObj> getListType4(){
        List<FrameObj> frameObjList = new ArrayList<>();
        ShapeInsideFrame shapeInsideFrame11 = new ShapeInsideFrame(0,50,0,50);
        ShapeInsideFrame shapeInsideFrame12 = new ShapeInsideFrame(50,100,0,50);
        ShapeInsideFrame shapeInsideFrame13 = new ShapeInsideFrame(0,50,50,100);
        ShapeInsideFrame shapeInsideFrame14 = new ShapeInsideFrame(50,100,50,100);
        List<ShapeInsideFrame> shapeInsideFrameList1 = new ArrayList<>();
        shapeInsideFrameList1.add(shapeInsideFrame11);
        shapeInsideFrameList1.add(shapeInsideFrame12);
        shapeInsideFrameList1.add(shapeInsideFrame13);
        shapeInsideFrameList1.add(shapeInsideFrame14);
        frameObjList.add(new FrameObj(shapeInsideFrameList1, "frame/type4/f1.png"));


        ShapeInsideFrame shapeInsideFrame21 = new ShapeInsideFrame(0,50,0,100);
        ShapeInsideFrame shapeInsideFrame22 = new ShapeInsideFrame(50,100,0,33.33);
        ShapeInsideFrame shapeInsideFrame23 = new ShapeInsideFrame(50,100,33.33,66.67);
        ShapeInsideFrame shapeInsideFrame24 = new ShapeInsideFrame(50,100,66.67,100);
        List<ShapeInsideFrame> shapeInsideFrameList2 = new ArrayList<>();
        shapeInsideFrameList2.add(shapeInsideFrame21);
        shapeInsideFrameList2.add(shapeInsideFrame22);
        shapeInsideFrameList2.add(shapeInsideFrame23);
        shapeInsideFrameList2.add(shapeInsideFrame24);
        frameObjList.add(new FrameObj(shapeInsideFrameList2, "frame/type4/f2.png"));


        ShapeInsideFrame shapeInsideFrame31 = new ShapeInsideFrame(0,33.33,0,50);
        ShapeInsideFrame shapeInsideFrame32 = new ShapeInsideFrame(33.33,66.67,0,50);
        ShapeInsideFrame shapeInsideFrame33 = new ShapeInsideFrame(66.67,100,0,50);
        ShapeInsideFrame shapeInsideFrame34 = new ShapeInsideFrame(0,100,50,100);
        List<ShapeInsideFrame> shapeInsideFrameList3 = new ArrayList<>();
        shapeInsideFrameList3.add(shapeInsideFrame31);
        shapeInsideFrameList3.add(shapeInsideFrame32);
        shapeInsideFrameList3.add(shapeInsideFrame33);
        shapeInsideFrameList3.add(shapeInsideFrame34);
        frameObjList.add(new FrameObj(shapeInsideFrameList3, "frame/type4/f3.png"));

        return frameObjList;

    }

    public static List<FrameObj> getListType5(){

        List<FrameObj> frameObjList = new ArrayList<>();
        ShapeInsideFrame shapeInsideFrame11 = new ShapeInsideFrame(0,100,0,50);
        ShapeInsideFrame shapeInsideFrame12 = new ShapeInsideFrame(0,50,50,75);
        ShapeInsideFrame shapeInsideFrame13 = new ShapeInsideFrame(50,100,50,75);
        ShapeInsideFrame shapeInsideFrame14 = new ShapeInsideFrame(0,50,75,100);
        ShapeInsideFrame shapeInsideFrame15 = new ShapeInsideFrame(50,100,75,100);
        List<ShapeInsideFrame> shapeInsideFrameList1 = new ArrayList<>();
        shapeInsideFrameList1.add(shapeInsideFrame11);
        shapeInsideFrameList1.add(shapeInsideFrame12);
        shapeInsideFrameList1.add(shapeInsideFrame13);
        shapeInsideFrameList1.add(shapeInsideFrame14);
        shapeInsideFrameList1.add(shapeInsideFrame15);
        frameObjList.add(new FrameObj(shapeInsideFrameList1, "frame/type5/f1.png"));

        ShapeInsideFrame shapeInsideFrame21 = new ShapeInsideFrame(0,50,0,50);
        ShapeInsideFrame shapeInsideFrame22 = new ShapeInsideFrame(50,100,0,50);
        ShapeInsideFrame shapeInsideFrame23 = new ShapeInsideFrame(0,33.33,50,100);
        ShapeInsideFrame shapeInsideFrame24 = new ShapeInsideFrame(33.33,66.67,50,100);
        ShapeInsideFrame shapeInsideFrame25 = new ShapeInsideFrame(66.67,100,50,100);
        List<ShapeInsideFrame> shapeInsideFrameList2 = new ArrayList<>();
        shapeInsideFrameList2.add(shapeInsideFrame21);
        shapeInsideFrameList2.add(shapeInsideFrame22);
        shapeInsideFrameList2.add(shapeInsideFrame23);
        shapeInsideFrameList2.add(shapeInsideFrame24);
        shapeInsideFrameList2.add(shapeInsideFrame25);
        frameObjList.add(new FrameObj(shapeInsideFrameList2, "frame/type5/f2.png"));

        ShapeInsideFrame shapeInsideFrame31 = new ShapeInsideFrame(0,50,0,50);
        ShapeInsideFrame shapeInsideFrame32 = new ShapeInsideFrame(50,100,0,50);
        ShapeInsideFrame shapeInsideFrame33 = new ShapeInsideFrame(0,50,50,100);
        ShapeInsideFrame shapeInsideFrame34 = new ShapeInsideFrame(50,100,50,100);
        ShapeInsideFrame shapeInsideFrame35 = new ShapeInsideFrame(20,80,20,80);
        List<ShapeInsideFrame> shapeInsideFrameList3 = new ArrayList<>();
        shapeInsideFrameList3.add(shapeInsideFrame31);
        shapeInsideFrameList3.add(shapeInsideFrame32);
        shapeInsideFrameList3.add(shapeInsideFrame33);
        shapeInsideFrameList3.add(shapeInsideFrame34);
        shapeInsideFrameList3.add(shapeInsideFrame35);
        frameObjList.add(new FrameObj(shapeInsideFrameList3, "frame/type5/f3.png"));

        return frameObjList;
    }

    public static List<FrameObj> getFrameObjectList(int type,Context context){

        List<FrameObj> frameObjList = new ArrayList<>();
        String jsonStr = readJsonFileToString(context);
        try {
            JSONObject jsonObjRoot = new JSONObject(jsonStr);

            for (int  t = 1 ; t <= 10; t ++){
                JSONArray jsonFrames = jsonObjRoot.getJSONArray("type" + t);
                for (int i = 0 ; i < jsonFrames.length() ; i ++){
                    JSONObject jsonShape = jsonFrames.getJSONObject(i);
                    List<ShapeInsideFrame> shapeInsideFrameList = new ArrayList<>();
                    for (int j = 0 ; j < t ; j ++){
                        JSONArray arrayPost = jsonShape.getJSONArray("f" + j);
                        ShapeInsideFrame shape = new ShapeInsideFrame();
                        Log.d("POSitionn",shape.getBottom() + " ");
                        shape.setLeft(arrayPost.getInt(0));
                        shape.setTop(arrayPost.getInt(1));
                        shape.setRighr(arrayPost.getInt(2));
                        shape.setBottom(arrayPost.getInt(3));
                        shapeInsideFrameList.add(shape);

                    }
                    Log.d("POSitionnSize", shapeInsideFrameList.size() + "");
                    FrameObj frameObj = new FrameObj();
                    frameObj.setShapeList(shapeInsideFrameList);
                    frameObjList.add(frameObj);
                }
            }




        } catch (JSONException e) {
            Log.d("ErrorParse",e.getMessage());
            e.printStackTrace();
        }
        return frameObjList;

    }


    private static  String readJsonFileToString(Context context){
        InputStream is = context.getResources().openRawResource(R.raw.datas);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builderStr = new StringBuilder();
        String tmp = null;
        try {
            while ((tmp = reader.readLine()) != null){
                builderStr.append(tmp);
                builderStr.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  builderStr.toString();
    }


}
