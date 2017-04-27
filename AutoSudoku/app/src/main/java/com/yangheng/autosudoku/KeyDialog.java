package com.yangheng.autosudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by chuan on 2016/5/12 0012.
 */
public class KeyDialog extends Dialog{
    //用来存放对话框中按钮的对象
    private final View keys[] = new View[12];
    private final int used[];
    private ShuduView shuduview;
    private Design game;


    public KeyDialog(Context context, int[] used, ShuduView shuduview, Design game){
        super(context);
        this.used =used;
        this.shuduview = shuduview;
        this.game=game;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("请选择:");
        setContentView(R.layout.keypad);
        findViews();
//        for(int i=0;i<used.length;i++){
//            if(used[i]!=0){
//                keys[used[i]-1].setVisibility(View.INVISIBLE);
//            }
//        }

        setListeners();
    }
    private void findViews(){
        keys[0]=findViewById(R.id.keypad_1);
        keys[1]=findViewById(R.id.keypad_2);
        keys[2]=findViewById(R.id.keypad_3);
        keys[3]=findViewById(R.id.keypad_4);
        keys[4]=findViewById(R.id.keypad_5);
        keys[5]=findViewById(R.id.keypad_6);
        keys[6]=findViewById(R.id.keypad_7);
        keys[7]=findViewById(R.id.keypad_8);
        keys[8]=findViewById(R.id.keypad_9);
        keys[9]=findViewById(R.id.keypad_10);
        keys[10]=findViewById(R.id.keypad_11);
        keys[11]=findViewById(R.id.keypad_12);
    }
    private void returnResult(int tile){
      shuduview.setSelectedTile(tile);
      dismiss();
    }
    private void setListeners(){
        for (int i=0;i<keys.length;i++){
            final int t;
            if(i<9){t = i+1;}else t=0;
            keys[i].setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {
                   returnResult(t);
               }
            });
        }
        keys[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        keys[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuduview.setSelectedTile(game.shudushow[shuduview.selectedX][shuduview.selectedY]);
//                game.setTile(shuduview.selectedX,shuduview.selectedY,
//                        game.shudushow[shuduview.selectedX][shuduview.selectedY]);
//                game.caculateAllusedTiles();
//                shuduview.invalidate();
                dismiss();
            }
        });
    }
}

