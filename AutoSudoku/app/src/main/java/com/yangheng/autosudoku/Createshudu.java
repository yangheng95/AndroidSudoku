package com.yangheng.autosudoku;


/**
 * Created by chuan on 2016/5/15 0015.
 */
  public class Createshudu {

    private int[][] init;
    public Createshudu() {
    }
    public boolean startmain() {
        this.init = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int used[] = this.calculateUsed(i, j, init);
                int t = this.rand(used);
                if (t > 500) {
                    return false;
                }
                init[i][j] = t;
            }
        }
        return true;
    }


    public int getInit(int x, int y) {
        return init[x][y];
    }
    public int[] calculateUsed(int x, int y, int init[][]) {
        int c[] = new int[9];
        for (int i = 0; i < 9; i++) {
            if (i != x) {
                int t = init[i][y];
                if (t != 0) {
                    c[t - 1] = t;
                }
            }
            if (i != y) {
                int t1 = init[x][i];
                if (t1 != 0) {
                    c[t1 - 1] = t1;
                }
            }
        }
        int startX = (x / 3) * 3;
        int startY = (y / 3) * 3;
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (i != x || j != y) {
                    int t3 = init[i][j];
                    if (t3 != 0) {
                        c[t3 - 1] = t3;
                    }
                }
            }
        }
        int nused = 0;
                /* for(int t:c){if(t!=0){nused++;}} */
        for (int i = 0; i < c.length; i++) {
            if (c[i] != 0) {
                nused++;
            }
        }
        int c1[] = new int[nused];
        nused = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] != 0) {
                c1[nused++] = c[i];
            }
        }
        return c1;
    }
    public int rand(int used[]) {
        int temp;
        int t = 0;
        while (true) {
            if (t >= 1000) {
                return t;
            }
            t++;
            boolean b = false;
            temp = (int) (Math.random() * 10);
            if (temp == 0) {
                continue;
            }
            for (int k = 0; k < used.length; k++) {
                if (temp == used[k]) {
                    b = true;
                }
            }
            if (b) {
                continue;
            }
            break;
        }
        return temp;
    }
}
