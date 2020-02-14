package LightsOff;

import java.util.Arrays;

public class Tip{
    static int[][][] seeds = new int[11][11][];
    static {
        seeds[1][2] = new int[]{0x3};

        seeds[1][5] = new int[]{0x1b};

        seeds[1][8] = new int[]{0xdb};

        seeds[2][1] = new int[]{0x1};

        seeds[2][3] = new int[]{0x2, 0x5};

        seeds[2][5] = new int[]{0x15};

        seeds[2][7] = new int[]{0x22, 0x55};

        seeds[2][9] = new int[]{0x155};

        seeds[3][2] = new int[]{0x1, 0x2};

        seeds[3][5] = new int[]{0x7, 0xa, 0x11};

        seeds[3][8] = new int[]{0x51, 0x8a};

        seeds[4][4] = new int[]{0x1, 0x2, 0x4, 0x8};

        seeds[4][9] = new int[]{0x28, 0x44, 0x82, 0x101};

        seeds[5][1] = new int[]{0x1};

        seeds[5][2] = new int[]{0x3};

        seeds[5][3] = new int[]{0x1, 0x2, 0x4};

        seeds[5][5] = new int[]{0xe, 0x15};

        seeds[5][7] = new int[]{0x8, 0x14, 0x22, 0x41};

        seeds[5][8] = new int[]{0xdb};

        seeds[5][9] = new int[]{0x155};

        seeds[6][8] = new int[]{0x4, 0xa, 0x11, 0x20, 0x41, 0x82};

        seeds[7][2] = new int[]{0x1, 0x2};

        seeds[7][5] = new int[]{0x2, 0x5, 0x8, 0x11};

        seeds[7][8] = new int[]{0x51, 0x8a};

        seeds[8][1] = new int[]{0x1};

        seeds[8][3] = new int[]{0x2, 0x5};

        seeds[8][5] = new int[]{0x15};

        seeds[8][6] = new int[]{0x1, 0x2, 0x4, 0x8, 0x10, 0x20};

        seeds[8][7] = new int[]{0x22, 0x55};

        seeds[8][9] = new int[]{0x155};

        seeds[9][2] = new int[]{0x3};

        seeds[9][4] = new int[]{0x1, 0x2, 0x4, 0x8};

        seeds[9][5] = new int[]{0x1b};

        seeds[9][8] = new int[]{0xdb};

        seeds[9][9] = new int[]{0x2, 0x5, 0x8, 0x11, 0x20, 0x41, 0x80, 0x101};
    }

    int row, col;
    boolean[][][] circleClickMaps;

    public Tip(int _row, int _col){
        if(_row < 1 || _col < 1 || _row > 10 || _col > 10){
            System.out.println("Error, Tip{}");
            return;
        }
        row = _row;
        col = _col;
        //用seeds产生若干circleClickMap，有2^n个
        if(seeds[row][col] == null || 0 == seeds[row][col].length){
            circleClickMaps = new boolean[1][row][col]; //会默认初始化为false
            return;
        }
        circleClickMaps = new boolean[1 << seeds[row][col].length][row][col];

        //产生seedMaps
        boolean[][][] seedMaps = new boolean[seeds[row][col].length][row][col];
        boolean[][] map;
        boolean[] clicks;
        for(int i = 0; i < seeds[row][col].length; i++){
            //初始化数组，以防万一
            map = new boolean[row][col];
            //产生一个seedMap
            clicks = getClick(seeds[row][col][i]);
            seedMaps[i][0] = Arrays.copyOf(clicks, col);
            //处理第一行的点击
            for(int j = 0; j < col; j++){
                if(clicks[j]) click(0, j, map);
            }
            //沉底，边记录点击在seedMap里
            for(int j = 1; j < row; j++){
                //j - 1行即为第j行的点击情况
                seedMaps[i][j] = Arrays.copyOf(map[j - 1], col);
                //点击第j行，目的是把第j-1行清空
                for(int k = 0; k < col; k++){
                    if(map[j - 1][k])   click(j, k, map);
                }
            }
            //检验map最后一行为false
            for(int j = 0; j < col; j++){
                if(map[row - 1][j]) {
                    System.out.println("Error:Tip:seed is fake");
                    break;
                }
            }
        }

        //根据seedMaps产生circleClickMaps
        for(int index = 0; index < (1 << seedMaps.length); index++){
            //产生第index个circleClickMap
            for(int i = 0; i < seedMaps.length; i++){
                if((index & (1 << i)) > 0)
                    circleClickMaps[index] = pileMap(circleClickMaps[index], seedMaps[i]);
            }
        }
    }

    private void click(int r, int c, boolean[][] map){
        map[r][c] ^= true;
        if(r - 1 >= 0)  map[r - 1][c] ^= true;
        if(r + 1 < row) map[r + 1][c] ^= true;
        if(c - 1 >= 0)  map[r][c - 1] ^= true;
        if(c + 1 < col) map[r][c + 1] ^= true;
    }

    //code展开成长度为col的boolean数组
    private boolean[] getClick(int code){
        //code最低位为res[col - 1]
        boolean[] res = new boolean[col];
        for(int i = col - 1; i >= 0; i--){
            res[i] = ((code & 0x1) == 1);
            code >>= 1;
        }
        return res;
    }

    private int getSteps(boolean[][] clickMap){
        if(clickMap == null || clickMap.length != row ||clickMap[0].length != col){
            System.out.println("getSteps:Error");
            return -1;
        }
        //统计点击地图中的点击数
        int steps = 0;
        for(int i = 0, j; i < row; i++){
            for(j = 0; j < col; j++){
                if(clickMap[i][j])  steps++;
            }
        }
        return steps;
    }

    private boolean[][] pileMap(boolean[][] map1, boolean[][] map2){
        if(map1 == null || map1.length != row || map1[0].length != col
        || map2 == null || map2.length != row || map2[0].length != col){
            System.out.println("Pile:Error");
            return null;
        }
        boolean[][] newMap = new boolean[row][col];
        for(int i = 0, j; i < row; i++){
            for(j = 0; j < col; j++){
                newMap[i][j] = map1[i][j] ^ map2[i][j];
            }
        }
        return newMap;
    }

    public boolean[][] getTip(boolean[][] clickMap){
        if(clickMap.length != row || clickMap[0].length != col){
            System.out.println("Error! getTip()\nthe length of map is illegal");
            return null;
        }
        //将clickMap依次与若干circleClickMaps堆叠，看结果中哪个步数最少
        int minStep = row * col + 1, step;  //minStep初始值不能少于此，否则全点的情况不会被计入。
        boolean[][] tipMap = new boolean[row][col], map;
        for (boolean[][] circleClickMap : circleClickMaps) {
            map = pileMap(clickMap, circleClickMap);
            step = getSteps(map);
            if (step < minStep) {
                //更新
                minStep = step;
                tipMap = map;
            }
        }
        return tipMap;
    }
}
