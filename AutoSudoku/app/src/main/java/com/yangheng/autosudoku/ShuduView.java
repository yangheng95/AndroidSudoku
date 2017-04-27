package com.yangheng.autosudoku;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by chuan on 2016/4/9 0009.
 */
public class ShuduView extends View {

    private float height;
    private float width;
    public Design game = new Design();

    public int selectedX;
    public int selectedY;
    public int flag = 1;

    public long start ; // 开始时间
    long time = 0;      //总时间
    int showed = 1;
    public Activity activity;

    int Celltype[][] = new int[9][9];//标记每个单元格数字应该显示的颜色
    int isDialogShow[][] = new int[9][9];//标记单元格是否可编辑


    public ShuduView(Context context,AttributeSet attrs){
        super(context, attrs);
        game.setShuduView(this);
        newGame();

    }


    public ShuduView(Context context) {
        super(context);
        game.setShuduView(this);
        newGame();
    }


    public void convertdialogshow(int a[][]){
        for (int i = 0; i < 9;i++)
            for (int j = 0;j < 9;j++){
                if(a[i][j] > 0)
                    isDialogShow[i][j] = 1;
                else{
                    isDialogShow[i][j] = 0;
                }
            }
    }


    public void convertshudutemp(int a[][]){
        for (int i = 0; i < 9;i++)
            for (int j = 0;j < 9;j++){
                if(a[i][j] > 0)
                    Celltype[i][j] = 1;
                else{
                    Celltype[i][j] = 0;
                }
            }
    }


    public void newGame(){

        flag = 1;
        long InitBegin = System.currentTimeMillis();
        int count = 0;
        do{
            game.time = 0;
            game.temp = game.shudu();
            convertshudutemp(game.temp);
            game.caculateAllusedTiles();
            game.InitArray();
            count ++;
        }while(!game.isUnque());
        game.gamereset();
        game.caculateAllusedTiles();
        game.InitArray();
        convertdialogshow(game.gamereset);
        long InitTime = System.currentTimeMillis() - InitBegin;
        Toast t = Toast.makeText(this.getContext(),"计算"+count+"次"+"共用时"
                        +(InitTime/1000)%60+"."+(InitTime/100)%10+(InitTime/10)%10+"秒",
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        showed = 1;
        start= System.currentTimeMillis();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w / 9f;
        this.height = h / 9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Paint BGpaint = new Paint();
        BGpaint.setColor(Color.argb(200, 230, 230, 250));

        BGpaint.setAlpha(100);
        canvas.drawRect(0, 0, getWidth(), getHeight(), BGpaint);

        final Paint darkpaint = new Paint();
        darkpaint.setColor(Color.BLACK);
        darkpaint.setStrokeWidth(2);

        Paint hilitepaint = new Paint();
        hilitepaint.setColor(Color.WHITE);
        hilitepaint.setStrokeWidth(2);

        Paint litepaint = new Paint();
        litepaint.setColor(Color.argb(255, 248, 248, 255));
        litepaint.setStrokeWidth(2);

        for (int i = 0; i < 9; i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height, hilitepaint);
            canvas.drawLine(i * width, 0, i * width, getHeight(), hilitepaint);
        }

        for (int i = 0; i < 9; i++) {
            if (i % 3 != 0) {
                continue;
            }
            canvas.drawLine(0, i * height, getWidth(), i * height, darkpaint);
            canvas.drawLine(i * width, 0, i * width, getHeight(), darkpaint);
        }

        Paint numpaint = new Paint();
        numpaint.setColor(Color.BLUE);
        numpaint.setTextAlign(Paint.Align.CENTER);
        numpaint.setTextSize(height * 0.72f);
        numpaint.setAntiAlias(true);

        Paint wrongpaint = new Paint();
        wrongpaint.setColor(Color.RED);
        wrongpaint.setTextAlign(Paint.Align.CENTER);
        wrongpaint.setTextSize(height * 0.72f);
        wrongpaint.setAntiAlias(true);

        Paint.FontMetrics fm = numpaint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;

        Paint Initpaint = new Paint();
        Initpaint.setColor(Color.BLACK);
        Initpaint.setTextAlign(Paint.Align.CENTER);
        Initpaint.setTextSize(height * 0.72f);
        Initpaint.setAntiAlias(true);


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Celltype[i][j] == 1){
                     canvas.drawText(game.getTileString(i,j), i * width + x, j * height + y, Initpaint);
                }
                else if(Celltype[i][j] == 2){
                    canvas.drawText(game.getTileString(i, j), i * width + x, j * height + y, wrongpaint);

                }
                else if(Celltype[i][j] == 0){
                    canvas.drawText(game.getTileString(i, j), i * width + x, j * height + y, numpaint);

                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (game.temp[i][j] != game.shudushow[i][j]) {
                    flag=0;
                    break;
                }
            }
        }



        if(flag==1){
            final long end = System.currentTimeMillis();  //结束时间
            time =end - start ;

            if((time/1000)%60 > 10){//时间太短不显示结束游戏提示，显示答案会在一秒内完成，故滤去
                Vibrator vibrator=(Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
                View layoutView = layoutInflater.inflate(R.layout.textview,null);
                final TextView TextView = (TextView)layoutView.findViewById(R.id.textview);
                TextView.setText("\n答案正确！ 用时: "+(time/60000)%60+"分"+(time/1000)%60+"秒");

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("提示");
                builder.setView(layoutView);

                final AlertDialog dialog =builder.create();
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        } );
                if(showed==1){
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            Celltype[i][j] = 1;
                            isDialogShow[i][j] = 1;
                        }
                    }
                    invalidate();
                    dialog.show();
                    showed = 0;
                }
            }
        }
        flag=1;
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        selectedX = (int) (event.getX() / width);
        selectedY = (int) (event.getY() / height);
        int used[] = game.getUsedTilesByCoor(selectedX, selectedY);

        if (isDialogShow[selectedX][selectedY]==0|| isDialogShow[selectedX][selectedY]==2){
            Log.e("11",String.valueOf(isDialogShow[selectedX][selectedY]));
            KeyDialog keyDialog = new KeyDialog(getContext(), used,this,game);
            keyDialog.show();
        }
        return true;

    }

    public void setSelectedTile(int tile) {
        if (game.setTileIfValid(selectedX, selectedY, tile)) {
            convertdialogshow(game.gamereset);
            convertshudutemp(game.gamereset);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for(int k = 0; k < game.usedincludeself[i][j].length;k++) {
                        if (game.temp[i][j] == game.usedincludeself[i][j][k]) {
                            Celltype[i][j] = 2;
                            break;
                        }
                    }
                }
            }
            invalidate();
        }
    }


}