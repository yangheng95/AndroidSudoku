package com.yangheng.autosudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    ShuduView shuduView;
    Design game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ShuduView view = (ShuduView)findViewById(R.id.shuduview);
        shuduView = getView(view);
        shuduView.activity = this;
        game = getDesign(shuduView.game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("数独游戏");
        toolbar.setSubtitle("Sudoku");
        toolbar.setLogo(R.mipmap.launcher);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shuduView.invalidate();
    }

    public ShuduView getView(ShuduView view){return view;}
    public Design getDesign(Design game){return game;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int item_id = item.getItemId();

        switch (item_id) {

            case R.id.restart:
                shuduView.newGame();
                shuduView.showed = 1;
                shuduView.invalidate();
                break;

            case R.id.gameprest:
                game.gamereset();
                shuduView.convertshudutemp(game.gamereset);
                shuduView.convertdialogshow(game.temp);
                shuduView.invalidate();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "数独已经被重置", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                shuduView.start = System.currentTimeMillis();
                break;

            case  R.id.showtheanswers:
                game.showAnswer();
                shuduView.convertshudutemp(game.gamereset);
                shuduView.convertdialogshow(game.temp);
                game.caculateAllusedTiles();
                shuduView.invalidate();
                shuduView.start = System.currentTimeMillis();
                break;
            case  R.id.share:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View view = layoutInflater.inflate(R.layout.share,null);
                TextView textView = (TextView)view.findViewById(R.id.sharetext);
                ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
                imageView.setImageResource(R.drawable.sharepicture);

                AlertDialog.Builder share = new AlertDialog.Builder(this);
                share.setTitle("游戏分享");

                share.setView(view);
                final AlertDialog alertDialog = share.create();

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        } );
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "查看源码",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("https://github.com/floAlpha/AndroidSudoku");
                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(it);
                            }
                        } );
                alertDialog.show();
                break;

            case R.id.info:
                AlertDialog.Builder builderinfo = new AlertDialog.Builder(this);
                builderinfo.setTitle("游戏说明");
                builderinfo.setMessage(R.string.app_detail );
                final AlertDialog alertdialog = builderinfo.create();
                alertdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        } );
                alertdialog.show();
                break;
            case R.id.undo:
                if(game.undo()) {
                    shuduView.invalidate();
                    Toast t = Toast.makeText(getApplicationContext(),
                            "删除了第 " + (game.to_y[game.pointer] + 1) + " 行第 "
                                    + (game.to_x[game.pointer] + 1) + " 列的填入的数字 " +
                                    game.to_value[game.pointer], Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }else{
                    Toast t = Toast.makeText(getApplicationContext(),"没有填写任何数字或者答案已给出"
                            , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }


               break;
            case R.id.exit:
                finish();

            default:
                break;
        }
        return true;
    }

    private void exitDialog()
    {
        AlertDialog.Builder aa=new AlertDialog.Builder(this);
        aa.setTitle("消息提示");
        aa.setMessage("您确定要退出游戏吗？");
        aa.setPositiveButton("确定",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                MainActivity.this.finish();
            }
        });
        aa.setNegativeButton("取消", null);
        aa.create();
        aa.show();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitDialog();
        }
        return true;

    }


}
