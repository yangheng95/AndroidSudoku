package com.yangheng.autosudoku;

import java.util.Random;

/**
 * Created by chuan on 2016/4/10 0010.
 */
    public  class Design {
    private ShuduView shuduView;
    private int used[][][] = new int[9][9][];//存放每个单元格所有已不能使用的数字
    int time = 0;//检查数独是否是唯一解递归层数

    final private int MAXLENGT = 200;
    int pointer = 0;
    int  to_x[] = new int [MAXLENGT];
    int  to_y[] = new int [MAXLENGT];
    int  to_value[] = new int [MAXLENGT];//回溯变量组

    public void setShuduView(ShuduView shuduView){
    this.shuduView = shuduView;
    }

    public Design() {}

    private int difficulty = 55;//难度简单表示法
    public int temp[][] =new int[9][9] ;//可供填写的数独数组
    final public int gamereset[][] = new int [9][9];
    private int temp1[] = new int[81];
    final public int shudushow[][] = new int[9][9];//生成的数独存档数组
    public int[][][] usedincludeself = new int[9][9][];

    public int[][] shudu() {
        int a = 0;int g = 0;
        Createshudu main = new Createshudu();
        boolean success = false;
        while (!success) {
            success = main.startmain();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                temp1[a] = main.getInit(i,j);
                shudushow[i][j] = main.getInit(i,j);
                a++;
            }
        }

        Random rand = new Random();
        int tem[] = new int[difficulty];
        for(int i = 0; i<difficulty; i++){
            tem[i] = rand.nextInt(81);
        }

        for(int i=0; i<difficulty; i++){temp1[tem[i]]=0;}
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                temp[i][j] = temp1[g];
                gamereset[i][j]=temp1[g];
                g++;
                System.out.print(temp[i][j] );
            }
            System.out.println();
        }
        return temp;
    }


    private int getTile(int x, int y) { return temp[x][y];}
    public String getTileString(int x, int y) {
        int a = getTile(x, y);
        if (a == 0) {
            return "";
        } else
            return String.valueOf(a);
    }
    public void caculateAllusedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = caculateUsedTiles(x, y);
            }
        }
    }

    public int[] getUsedTilesByCoor(int x, int y) {
        return used[x][y];
    }

    private int[] caculateUsedTiles(int x, int y) {
        int c[] = new int[9];
        for (int i = 0; i < 9; i++) {
            if (i == y)
                continue;
            int t = getTile(x, i);
            if (t != 0)
                c[t - 1] = t;
        }

        for (int i = 0; i < 9; i++) {
            if (i == x)
                continue;
            int t = getTile(i, y);
            if (t != 0)
                c[t - 1] = t;
        }

        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;
        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }
        usedincludeself[x][y] = c;

        int unused = 0;
        for (int t : c) {
            if (t != 0)
                unused++;
        }

        int c1[] = new int[unused];
        unused = 0;
        for (int t : c) {
            if (t != 0)
                c1[unused++] = t;
        }

        int c2[] = new int[unused+1];
        unused =0;
        for (int t : c1) {
            if (t != 0)
                c2[unused++] = t;
        }

        c2[unused] = getTile(x,y);

        return c2;

    }

    protected boolean setTileIfValid(int x, int y, int value) {

//        int tiles[] = getUsedTiles(x, y);//注释去掉弹出选择框时会填写不了已不可使用的数字
//        if (value != 0) {
//            for (int tile : tiles) {
//                if (tile == value)
//                    return false;
//            }
//        }
        setTile(x, y, value);
        caculateAllusedTiles();
        return true;
    }

//    protected int[] getUsedTiles(int x, int y) {
//        return used[x][y];
//    }



    public void InitArray(){
        pointer = 0;
        for (int i = 0; i < MAXLENGT; i++) {
            to_value[i] = to_x[i] = to_y[i] = 0;
        }
    }

    private void setTile(int x, int y, int value) {
        temp[x][y] = value;
        to_x[pointer]=x;
        to_y[pointer]=y;
        to_value[pointer]=value;
        pointer++;
    }

    public boolean undo(){//撤回上一步操作
        for(int i = 0;i < 9;i++){
            for (int j = 0;j < 9;j++){
                if(temp[i][j] != gamereset[i][j]){
                    if(pointer >0) {
                        temp[to_x[pointer -1]][to_y[pointer -1]]=0;
                        caculateAllusedTiles();
                        pointer--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void gamereset(){//重置游戏
        InitArray();
        for(int i =0;i<9;i++){
            for (int j = 0 ; j < 9;j++){
                //temp[i][j] = getInitshudu[i][j];
                temp[i][j] = gamereset[i][j];
                caculateAllusedTiles();
            }
        }
    }

    public void showAnswer(){//显示游戏答案
        InitArray();
        for(int i =0;i<9;i++){
            for (int j = 0 ; j < 9;j++){
                temp[i][j] = shudushow[i][j];
            }
        }
    }

    public boolean isUnque(){
        int i;int j;
        boolean b = true;
        for( i = 0; i < 9;i++){
            for(j = 0;j < 9;j++){
                if(used[i][j].length == 9&&temp[i][j] == 0) {
                    setTile(i, j, getUnqueTile(used[i][j]));
                    caculateAllusedTiles();
                }
            }
        }
        for( i = 0; i < 9;i++){
            for(j = 0;j < 9;j++){
                if(temp[i][j] == 0&&time < 6) {
                    time++;
                    b = isUnque();
                    break;
                }
                else if(temp[i][j] == 0&&time >= 6){
                    b = false;
                    break;
                }
            }
        }
        return b;
    }

    private int getUnqueTile(int used[]){
        int i;
        int numbers[] = new int[10];
        for( i = 0; i < 10;i++){
            numbers[i] = i;
        }
        for( i = 0; i < used.length;i++){//将已用的数字置-1
            numbers[used[i]] = -1;
        }
        for( i = 0; i < numbers.length;i++){
            if(numbers[i] > -1) {
                return numbers[i];
            }
        }
        return numbers[i];
    }
}
