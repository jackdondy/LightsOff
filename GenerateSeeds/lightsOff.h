#include<stdio.h>

typedef enum{On, Off} STATE;
int ROW = 5, COL = 5; 

//翻转map[r][c],如果不存在，则返回0 
int safeTurn(STATE map[ROW][COL], int r, int c){
	if(r < 0 || r > (ROW - 1) || c < 0 || c > (COL - 1)){
		return 0;	//表示失败 
	}
	if(On == map[r][c]){
		map[r][c] = Off;
	}else map[r][c] = On;
	return 1;
}

//点击map[r][c]，并翻转周围的块 
void click(STATE map[ROW][COL], int r, int c){
	if(!safeTurn(map, r, c)){
		printf("Click Error!");
		return ;
	}
	safeTurn(map, r - 1, c);
	safeTurn(map, r + 1, c);
	safeTurn(map, r, c - 1);
	safeTurn(map, r, c + 1);
}

//将map沉底
void sink(STATE map[ROW][COL]){
	//将map的底行以上全部化为0
	for(int r = 0, c; r < ROW - 1; r++){
		//处理map[r] 
		for(c = 0; c < COL; c++){
			if(Off == map[r][c])	continue;
			click(map, r + 1, c);
		}
	}
}

/*
int main(){
	STATE map[ROW][COL], bottom[COL];
	sink(map, bottom);
	return 0;
} 
*/
