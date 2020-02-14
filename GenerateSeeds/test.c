#include<stdio.h>
#include<memory.h>
#include<malloc.h> 

#include"lightsOff.h"

//将第一行某块的沉底 连拍 展示出来

//将map沉底，记录快照 (shots[i]代表前i行被清零 后 第 i+1 行的状态)
void sink2(STATE map[ROW][COL], STATE shots[ROW][COL]){
	//将map的底行以上全部化为0
	for(int r = 0, c; r < ROW - 1; r++){
		//处理一行之前，存快照
		memcpy(shots[r], map[r], sizeof(STATE) * COL); 
		//处理map[r] 
		for(c = 0; c < COL; c++){
			if(Off == map[r][c])	continue;
			click(map, r + 1, c);
		}
	}
	//存底部的快照 
	memcpy(shots[ROW - 1], map[ROW - 1], sizeof(STATE) * COL); 
}

void display(STATE map[ROW][COL]){
	printf("\n\tDisplay\n");
	for(int i = 0, j; i < ROW; i++){
		for(j = 0; j < COL; j++){
			printf((On == map[i][j]) ? "\t1" : "\t0");
		}
		printf("\n\n");
	}
}

void displayRow(int len, STATE map[]){
	printf("\n\tDisplay\n");
	for(int j = 0; j < len; j++){
		printf((On == map[j]) ? "\t1" : "\t0");
	}
	printf("\n\n");
}

typedef short vector;	//有效位数为 低 COL 位, COL 不能大于 15位 

//STATE to vector ，arr[0] = vector高COL位，arr[COL - 1] = vector最低位 
vector toVector(STATE arr[COL]){
	vector v = 0;
	for(int i = 0; i < COL; i++){
		v = (v << 1) | ((On == arr[i]) ? 1 : 0);
	}
	return v;
}

void toSTATE(vector v, STATE res[COL]){
	for(int i = COL - 1; i >= 0; i--){
		res[i] = ((0x1 == (v & 0x1)) ? On : Off);
		v >>= 1;
	}
}

//将n个vector根据系数加起来，系数为STATE 
vector addV(int n, STATE A[], vector M[]){
	vector v = 0;
	for(int i = 0; i < n; i++){
		if(Off == A[i])		continue;
		v ^= M[i];
	}
	return v;
}

//将v分解为 M 中 n个向量的线性组合 ,系数记录在 A里 ,无法分解则返回 0 ,否则返回 1 
//A 长度必须等于 n 
int getSolu(int n, vector M[], vector v, STATE A[]){
	if(n <= 0){
		//无法分解
		return 0; 
	}
	if(n >= 8 * sizeof(vector)){
		printf("getSolu:Error");
		return 0;
	}
	//暴力遍历所有的系数情况
	vector r = 0;
	toSTATE(0, A);
	while(v != addV(n, A, M)){
		r++;
		if((0x1 << COL) <= r)	{
			return 0;	//失败 
		}
		toSTATE(r, A);
	}
	return 1;	//成功 
}

//将v分解为 M 中 n个向量的线性组合,将所有情况打印出来 
void printAllSolu(int n, vector M[], vector v){
	printf("print All Solu:");
	//暴力遍历所有的系数情况
	STATE A[n];
	int i;
	for(i = 0; i < n; i++)	A[i] = Off;
	while(1){
		if(addV(n, A, M) == v){
			displayRow(n, A); 
		}
		//进位A
		//遇到的第一个Off转为On,在这之前的On都转为Off
		for(i = 0; i < n; i++){
			if(On == A[i])		continue;
			A[i] = On;
			for(i--; i >= 0; i--)	A[i] = Off;
			break;
		}
		if(i > 0){
			//说明全为On 
			return ;
		}		
	}
}

//判断 v 是否可分解为 M 的行向量， 即是否相关 ,有关 返回 1，无关 返回 0 
int isRela(int n, vector M[], vector v){
	STATE A[n];
	return getSolu(n, M, v, A);		//可以分解，则有关 
}

//计算ROW*COL时的最大步数，对一个地图来说，步数取最少
//输入Solu为若干循环组合的点击地图，count为循环组合个数 
//int getMaxSteps(int count, STATE Solu[][ROW][COL]){
//	printf("\nCount:%d\n", count); 
//	if(0 >= count)	return ROW * COL;
//	
//	
//	return 0;
//} 


//处理一个 ROW * COL 的情况 ,返回 极大无关循环组合 个数 
int deal(){
	
//	printf("Deal:\tROW:%d\tCOL:%d\n", ROW, COL);
	
	STATE map[ROW][COL], shots[ROW][COL];
		
//	for(int i = 0; i <= ROW / 2; i++){
//		for(int r = 0; r < ROW; r++){
//			for(int c = 0; c < COL; c++){
//				map[r][c] = Off;
//			}
//		}
//		map[0][i] = On;
//		printf("\nmap");
//		display(map);
//		
//		sink2(map, shots);
//		
//		printf("\nshot");
//		display(shots);
//	}
	
	vector M[COL];	//M[i]代表click(map[0][i])后沉底的结果，结果按位编码为向量 
	
	for(int i = 0; i < COL; i++){
		
		for(int r = 0; r < ROW; r++){
			for(int c = 0; c < COL; c++){
				map[r][c] = Off;
			}
		}
		click(map, 0, i);
//		printf("\nmap");
//		display(map);
		
		sink(map);
		
//		printf("\nbottom");
//		displayRow(COL, map[ROW - 1]);
		
		M[i] = toVector(map[ROW - 1]);
	}
	
//	STATE M_i[COL];
//	printf("M\n");
//	for(int i = 0; i < COL; i++){
//		toSTATE(M[i], M_i);
//		displayRow(COL, M_i);
//	}
	
//	printf("print All Solu:");
	
	//v 暴力遍历 M 的行向量 所有的系数情况, seeds记录可行的 v ,count 记录 可行的 个数 
	vector v = 0, *seeds = NULL, *t;
	int count = 0;
	
	STATE A[COL];	//A 记录 v，中间变量 
	int i, j;
	while(1){
		v++;	//跳过v = 0 
		if((0x1 << COL) <= v){
			break;
		}
		toSTATE(v, A);
		//v满足循环种子要求 ,且与已有的seeds无关 
		if((addV(COL, A, M) == 0) && (!isRela(count, seeds, v))){
			//将v加入seeds
			count++;
			t = (vector*)realloc(seeds, count * sizeof(vector));
			if(NULL == t){
				printf("Error:Heap memory run out\n");
				break;
			}
			seeds = t;
			seeds[count - 1] = v;
			
		}
	}
	//挖掘seeds
//	printf("\nSeeds:%d\n", count);
//	for(i = 0; i < count; i++){
//		toSTATE(seeds[i], A);
//		displayRow(COL, A);
//	} 
	if(count < 1){
		//不打印
		return count; 
	} 
	
	printf("\nseeds[%d][%d] = new int[]{", ROW, COL);
	printf("0x%x", seeds[0]);
	for(i = 1; i < count; i++){
		printf(", 0x%x", seeds[i]);
	}
	printf("};\n");
	free(seeds);
	return count;
}

int main(){
	freopen("1.java", "w", stdout);
	
	int cycles[11][11] = {0};
	for(ROW = 1; ROW < 11; ROW++){
		for(COL = 1; COL < 11; COL++){
			cycles[ROW][COL] = deal();
		}
	}
	//打印循环种子 个数表 
//	printf("\n%4s", "ROW:");
//	for(int i = 1; i < 11; i++){
//		printf("\t%5d", i);
//	}
//	printf("\nCOL:");
//	for(int i = 1, j; i < 11; i++){
//		printf("\n\n%4d", i);
//		for(j = 1; j <= i; j++){
//			printf("\t%5d", cycles[i][j]);
//		}
//	}
	return 0;
}
