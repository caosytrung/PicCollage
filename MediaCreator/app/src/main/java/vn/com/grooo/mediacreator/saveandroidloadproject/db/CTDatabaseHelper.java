package vn.com.grooo.mediacreator.saveandroidloadproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.saveandroidloadproject.model.CollageOb;

/**
 * Created by trungcs on 9/14/17.
 */

public class CTDatabaseHelper extends SQLiteOpenHelper {
    private static  final String TAG = "mCTDatabaseHelper";
    private static final String DB_NAME = "colimage.db";

    public static final String ID_COLUME = "id";
    public static final String FRAME_INDEX_COLUME = "frame_index";
    public static final String IMAGE_INSIDE_FRAME = "image_frame";
    public static final String STICKER ="sticker";
    public static final String BORDER_COLUME="border";
    public static final String FILE_COLUM = "file";
    public static final String COLOR_COLUME = "color";

    public static final String TB_NAME = "collagetb";
    public static final String CREATE_TABLE_COLLAGE =
            "CREATE TABLE " + TB_NAME + "(" +
                    ID_COLUME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                    ", " + FRAME_INDEX_COLUME + " INTEGER NOT NULL" +
                    "," + IMAGE_INSIDE_FRAME + " TEXT" +
                    "," + STICKER + " TEXT" +
                    "," + BORDER_COLUME + " INTEGER NOT NULL" +
                    "," + COLOR_COLUME + " INTEGER NOT NULL" +
                    "," + FILE_COLUM + " TEXT NOT NULL" +
                    ")";
    private SQLiteDatabase mSqLiteDatabase;

    public static  CTDatabaseHelper INSTANCE = null ;
    public static synchronized CTDatabaseHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            INSTANCE = new CTDatabaseHelper(context);
        }

        return INSTANCE;
    }


    private   CTDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE_COLLAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG,"Create table faile");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDB(){
        mSqLiteDatabase = getWritableDatabase();
    }

    public void closeDB(){
        if (mSqLiteDatabase != null && mSqLiteDatabase.isOpen()){
            mSqLiteDatabase.close();
        }
    }

    public void addRecord(CollageOb collageOb){
        Log.d("GetLisyINdorrrr4",  "---- "+ collageOb.getFileName());
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRAME_INDEX_COLUME,collageOb.getFrameIndex());
        contentValues.put(IMAGE_INSIDE_FRAME,collageOb.getImageFrame());
        contentValues.put(STICKER,collageOb.getSticker());
        contentValues.put(BORDER_COLUME,collageOb.getBorder());
        contentValues.put(FILE_COLUM,collageOb.getFileName());
        contentValues.put(COLOR_COLUME,collageOb.getColor());

        mSqLiteDatabase.insert(TB_NAME,null,contentValues);
        closeDB();
    }

    public boolean deteteRecord(int id){
        openDB();
        boolean b =   mSqLiteDatabase.delete(TB_NAME,"id = '" + id + "'",null) > 0;
        closeDB();
        return b;
    }

    public boolean updateRecord(CollageOb collageOb ){
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRAME_INDEX_COLUME,collageOb.getFrameIndex());
        contentValues.put(IMAGE_INSIDE_FRAME,collageOb.getImageFrame());
        contentValues.put(STICKER,collageOb.getSticker());
        contentValues.put(BORDER_COLUME,collageOb.getBorder());
        contentValues.put(FILE_COLUM,collageOb.getFileName());
        contentValues.put(COLOR_COLUME,collageOb.getColor());

        boolean result =  mSqLiteDatabase.update(TB_NAME,contentValues,"id = '" + collageOb.getId() + "'",null) > 0;
        closeDB();
        return  result;
    }

    public void romeveAllREcord(){
        openDB();
        mSqLiteDatabase.execSQL("delete from "+ TB_NAME);
        closeDB();
    }

    public CollageOb getCollageObj(int  id){
        openDB();
        String query = "SELECT * FROM " + TB_NAME  + " WHERE " + ID_COLUME  + "='" + id +"'";
        Cursor cursor = mSqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();

        int indexFrame = cursor.getColumnIndex(FRAME_INDEX_COLUME);
        int indexImageFrame = cursor.getColumnIndex(IMAGE_INSIDE_FRAME);
        int indexSticker = cursor.getColumnIndex(STICKER);
        int indexBorder = cursor.getColumnIndex(BORDER_COLUME);
        int indexFileName = cursor.getColumnIndex(FILE_COLUM);
        int indexColor = cursor.getColumnIndex(COLOR_COLUME);

        int frameIndex = cursor.getInt(indexFrame);
        String imageFrame = cursor.
                getString(indexImageFrame);
        String sticker = cursor.getString(indexSticker);
        int border = cursor.getInt(indexBorder);
        String fileName = cursor.getString(indexFileName);
        int color = cursor.getInt(indexColor);

        Log.d("GetColoaageFormId",  id + " == " + color );


        CollageOb collageOb = new CollageOb(id,frameIndex,imageFrame,sticker,border,fileName,color);
        cursor.close();
        closeDB();
        return  collageOb;
    }

    public List<CollageOb> getListRecord(){
        openDB();
        List<CollageOb> collageObList = new ArrayList<>();
        String query = "SELECT * FROM " + TB_NAME;
        Cursor cursor = mSqLiteDatabase.rawQuery(query,null);
        int indexId = cursor.getColumnIndex(ID_COLUME);
        int indexFrame = cursor.getColumnIndex(FRAME_INDEX_COLUME);
        int indexImageFrame = cursor.getColumnIndex(IMAGE_INSIDE_FRAME);
        int indexSticker = cursor.getColumnIndex(STICKER);
        int indexBorder = cursor.getColumnIndex(BORDER_COLUME);
        int indexFileName = cursor.getColumnIndex(FILE_COLUM);
        int indexColor = cursor.getColumnIndex(COLOR_COLUME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(indexId);
            int frameIndex = cursor.getInt(indexFrame);
            String imageFrame = cursor.
                    getString(indexImageFrame);
            String sticker = cursor.getString(indexSticker);
            int border = cursor.getInt(indexBorder);
            String fileName = cursor.getString(indexFileName);
            int color = cursor.getInt(indexColor);

            Log.d("GetLisyINdorrrr3",  "---- "+ fileName);


            CollageOb collageOb = new CollageOb(id,frameIndex,imageFrame,sticker,border,fileName,color);
            collageObList.add(collageOb);
            cursor.moveToNext();
        }
        closeDB();
        return collageObList;

    }


}
