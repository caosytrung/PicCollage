package vn.com.grooo.mediacreator.photomaker.ui.act;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.model.IntentBitmap;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVTextColorAdapter;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVTextStyleAdapter;

/**
 * Created by trungcs on 8/19/17.
 */

public class AddTextStickerActivity extends BaseActivity implements
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, TextWatcher, ColorPickerDialog.OnColorPickedListener {
    private RVTextStyleAdapter mTextStyleAdapter;
    private RecyclerView rvTextStyle;
    private List<String> pathStyleList;

    private RVTextColorAdapter mTextColorAdapter;
    private RecyclerView rvTextColor;
    private List<String> colorList;
    private RelativeLayout rlColor;
    private SeekBar sbTextSize;

    private ImageView ivTextStyle;
    private ImageView ivTextSize;
    private ImageView ivTextColor;
    private ImageView ivPickColorSticker;
    private TextView tvTextToMakeSticker;
    private EditText edtTextToSticker;
    private ImageView ivAddTextSticker;
    private ImageView ivSetTextBackground;
    private ImageView ivBack;

    private int tmpTextColor;
    private int tmpBackgroundColor;
    private int tmpPosTypeface;



    @Override
    protected int getLayoutResources() {

        return R.layout.activity_add_text_sticker;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        getListStyleList();
        rvTextStyle = (RecyclerView) findViewById(R.id.rvTextStyleSticker);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTextStyle.setLayoutManager(manager);
        ivBack = (ImageView) findViewById(R.id.ivBackTextSticker);
        ivBack.setOnClickListener(this);

        mTextStyleAdapter = new RVTextStyleAdapter(this);
        mTextStyleAdapter.setListPath(pathStyleList);
        mTextStyleAdapter.setIrvClickListener(new IRVClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                mTextStyleAdapter.setSelectPos(pos);
                mTextStyleAdapter.notifyDataSetChanged();

                rvTextStyle.setAdapter(mTextStyleAdapter);
                rvTextStyle.scrollToPosition(pos);
                rvTextStyle.invalidate();
                Utils.setFontStyle(AddTextStickerActivity.this,pathStyleList.get(pos),tvTextToMakeSticker);
                tmpPosTypeface = pos;
            }

            @Override
            public void onItemLongClick(int pos, View v) {

            }
        });
        rvTextStyle.setAdapter(mTextStyleAdapter);
        mTextStyleAdapter.notifyDataSetChanged();
        rvTextStyle.scrollToPosition(3);


        colorList = Utils.getListColor();
        rvTextColor = (RecyclerView) findViewById(R.id.rvTextColorSticker);
        mTextColorAdapter = new RVTextColorAdapter(this);
        mTextColorAdapter.setColorList(colorList);
        mTextColorAdapter.setIrvClickListener(new IRVClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                tvTextToMakeSticker.setTextColor(Color.parseColor(colorList.get(pos)));
                tmpTextColor = Color.parseColor(colorList.get(pos));
            }

            @Override
            public void onItemLongClick(int pos, View v) {

            }
        });
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTextColor.setLayoutManager(manager1);
        rvTextColor.setAdapter(mTextColorAdapter);
        mTextColorAdapter.notifyDataSetChanged();
        rvTextColor.scrollToPosition(colorList.size() - 1);

        ivAddTextSticker = (ImageView) findViewById(R.id.ivSaveTextSticker);
        sbTextSize = (SeekBar) findViewById(R.id.sbTextSizeSticker);
        rlColor = (RelativeLayout) findViewById(R.id.rlColor);
        ivTextColor = (ImageView) findViewById(R.id.ivTextColorSticker);
        ivTextSize = (ImageView) findViewById(R.id.ivTextSizeSticker);
        ivTextStyle = (ImageView) findViewById(R.id.ivTextStyleSticker);
        ivPickColorSticker = (ImageView) findViewById(R.id.ivPickColorSticker);
        edtTextToSticker = (EditText) findViewById(R.id.edtTextToSticker);
        tvTextToMakeSticker = (TextView) findViewById(R.id.tvTextToMakeSticker);
        ivSetTextBackground = (ImageView) findViewById(R.id.ivSetbackgroundTextSticker);
        tvTextToMakeSticker.setTextSize(32);


        sbTextSize.setOnSeekBarChangeListener(this);
        ivTextColor.setOnClickListener(this);
        ivTextSize.setOnClickListener(this);
        ivTextStyle.setOnClickListener(this);
        ivPickColorSticker.setOnClickListener(this);
        edtTextToSticker.addTextChangedListener(this);
        ivAddTextSticker.setOnClickListener(this);
        ivSetTextBackground.setOnClickListener(this);
        tmpTextColor = Color.parseColor("#FF4081");
        tmpPosTypeface = 3;
        tmpBackgroundColor = Color.TRANSPARENT;
        edtTextToSticker.requestFocus();


    }

    public Bitmap textAsBitmap() {
        String text = tvTextToMakeSticker.getText().toString();

        Typeface plain = Typeface.createFromAsset(getAssets(), pathStyleList.get(tmpPosTypeface));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(tvTextToMakeSticker.getTextSize());
        paint.setColor(tmpTextColor);
        paint.setTextAlign(Paint.Align.LEFT);
        Log.d("Typefacee", pathStyleList.get(tmpPosTypeface));
        paint.setTypeface(plain);
        float baseline = -paint.ascent(); // ascent() is negative

        float measureText =  paint.measureText(text);
        int width = (int) (paint.measureText(text) * 1.5); // round
        int height = (int) ((baseline + paint.descent()) * 2f) ;
        float heightText =baseline + paint.descent();
        float y= (height - heightText) / 2 + baseline;

        float left = (width - measureText) /2 ;

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawColor(tmpBackgroundColor);
        canvas.drawText(text,left , y , paint);
        RectF  recF = new RectF(0,0,width,height);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(tmpTextColor);
        canvas.drawRect(recF,paint);

        Log.d("whhaaaahh", width + " === " + height);

        return image;
    }

    private void getListStyleList(){
        pathStyleList = new ArrayList<>();
        try {
            String[] arrStyle = getAssets().list("textstyle");
            for (int i = 0 ; i < arrStyle.length ; i ++){
                pathStyleList.add("textstyle/" + arrStyle[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showChangingTextSize(){
        rlColor.setVisibility(View.GONE);
        rvTextStyle.setVisibility(View.GONE);
        sbTextSize.setVisibility(View.VISIBLE);

        ivTextSize.setBackgroundResource(R.drawable.icon_text_size_pink);
        ivTextColor.setBackgroundResource(R.drawable.icon_text_color_gray);
        ivTextStyle.setBackgroundResource(R.drawable.icon_text_style_gray);
    }

    private void showChangingTextColor(){
        rlColor.setVisibility(View.VISIBLE);
        rvTextStyle.setVisibility(View.GONE);
        sbTextSize.setVisibility(View.GONE);

        ivTextSize.setBackgroundResource(R.drawable.icon_text_size_gray);
        ivTextColor.setBackgroundResource(R.drawable.icon_text_color_pink);
        ivTextStyle.setBackgroundResource(R.drawable.icon_text_style_gray);
    }

    private void showChangingTextStyle(){
        rlColor.setVisibility(View.GONE);
        rvTextStyle.setVisibility(View.VISIBLE);
        sbTextSize.setVisibility(View.GONE);

        ivTextSize.setBackgroundResource(R.drawable.icon_text_size_gray);
        ivTextColor.setBackgroundResource(R.drawable.icon_text_color_gray);
        ivTextStyle.setBackgroundResource(R.drawable.icon_text_style_pink);
    }





    @Override
    protected void initData(Bundle savedInstanceState) {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackTextSticker:
                finish();
                break;
            case R.id.ivTextColorSticker:
                showChangingTextColor();
                break;
            case R.id.ivTextSizeSticker:
                showChangingTextSize();
                break;
            case R.id.ivTextStyleSticker:
                showChangingTextStyle();
                break;
            case R.id.ivPickColorSticker:
                showDialogPickColor();
                break;
            case R.id.ivSetbackgroundTextSticker:
                showDialogBackground();
                break;
            case R.id.ivSaveTextSticker:
                Log.d("fasdasdasd","ffasssadsa");
                addTextSticker();
                break;
            default:
                break;
        }
    }

    private void addTextSticker(){
//        Intent intent = new Intent();
//        intent.putExtra(Constants.KEY_STICKER_TEXT,textAsBitmap());
//        setResult(Constants.CODE_STICKER_TEXT,intent);
//        finish();
//        Log.d("fasdasdasd","ffasssadsa2312");
        EventBus.getDefault().post(new IntentBitmap(1,textAsBitmap()));
        finish();
    }

    public void showDialogPickColor(){
        ColorPickerDialog dialog = ColorPickerDialog.
                createColorPickerDialog(this,ColorPickerDialog.DARK_THEME);
        dialog.setOnColorPickedListener(this);
        dialog.show();

    }
    public void showDialogBackground(){
        ColorPickerDialog dialog = ColorPickerDialog.
                createColorPickerDialog(this,ColorPickerDialog.DARK_THEME);
        dialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                tvTextToMakeSticker.setBackgroundColor(color);
                tmpBackgroundColor = color;
            }
        });
        dialog.show();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tvTextToMakeSticker.setTextSize(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        tvTextToMakeSticker.setText(editable.toString());
        if (editable.toString().length() == 0){
            ivAddTextSticker.setVisibility(View.GONE);
            return;
        }
        ivAddTextSticker.setVisibility(View.VISIBLE);



    }

    @Override
    public void onColorPicked(int color, String hexVal) {
        tvTextToMakeSticker.setTextColor(color);
        tmpTextColor = color;
    }
}
