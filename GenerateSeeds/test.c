#include<stdio.h>
#include<memory.h>
#include<malloc.h> 

#include"lightsOff.h"

//����һ��ĳ��ĳ��� ���� չʾ����

//��map���ף���¼���� (shots[i]����ǰi�б����� �� �� i+1 �е�״̬)
void sink2(STATE map[ROW][COL], STATE shots[ROW][COL]){
	//��map�ĵ�������ȫ����Ϊ0
	for(int r = 0, c; r < ROW - 1; r++){
		//����һ��֮ǰ�������
		memcpy(shots[r], map[r], sizeof(STATE) * COL); 
		//����map[r] 
		for(c = 0; c < COL; c++){
			if(Off == map[r][c])	continue;
			click(map, r + 1, c);
		}
	}
	//��ײ��Ŀ��� 
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

typedef short vector;	//��Чλ��Ϊ �� COL λ, COL ���ܴ��� 15λ 

//STATE to vector ��arr[0] = vector��COLλ��arr[COL - 1] = vector���λ 
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

//��n��vector����ϵ����������ϵ��ΪSTATE 
vector addV(int n, STATE A[], vector M[]){
	vector v = 0;
	for(int i = 0; i < n; i++){
		if(Off == A[i])		continue;
		v ^= M[i];
	}
	return v;
}

//��v�ֽ�Ϊ M �� n��������������� ,ϵ����¼�� A�� ,�޷��ֽ��򷵻� 0 ,���򷵻� 1 
//A ���ȱ������ n 
int getSolu(int n, vector M[], vector v, STATE A[]){
	if(n <= 0){
		//�޷��ֽ�
		return 0; 
	}
	if(n >= 8 * sizeof(vector)){
		printf("getSolu:Error");
		return 0;
	}
	//�����������е�ϵ�����
	vector r = 0;
	toSTATE(0, A);
	while(v != addV(n, A, M)){
		r++;
		if((0x1 << COL) <= r)	{
			return 0;	//ʧ�� 
		}
		toSTATE(r, A);
	}
	return 1;	//�ɹ� 
}

//��v�ֽ�Ϊ M �� n���������������,�����������ӡ���� 
void printAllSolu(int n, vector M[], vector v){
	printf("print All Solu:");
	//�����������е�ϵ�����
	STATE A[n];
	int i;
	for(i = 0; i < n; i++)	A[i] = Off;
	while(1){
		if(addV(n, A, M) == v){
			displayRow(n, A); 
		}
		//��λA
		//�����ĵ�һ��OffתΪOn,����֮ǰ��On��תΪOff
		for(i = 0; i < n; i++){
			if(On == A[i])		continue;
			A[i] = On;
			for(i--; i >= 0; i--)	A[i] = Off;
			break;
		}
		if(i > 0){
			//˵��ȫΪOn 
			return ;
		}		
	}
}

//�ж� v �Ƿ�ɷֽ�Ϊ M ���������� ���Ƿ���� ,�й� ���� 1���޹� ���� 0 
int isRela(int n, vector M[], vector v){
	STATE A[n];
	return getSolu(n, M, v, A);		//���Էֽ⣬���й� 
}

//����ROW*COLʱ�����������һ����ͼ��˵������ȡ����
//����SoluΪ����ѭ����ϵĵ����ͼ��countΪѭ����ϸ��� 
//int getMaxSteps(int count, STATE Solu[][ROW][COL]){
//	printf("\nCount:%d\n", count); 
//	if(0 >= count)	return ROW * COL;
//	
//	
//	return 0;
//} 


//����һ�� ROW * COL ����� ,���� �����޹�ѭ����� ���� 
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
	
	vector M[COL];	//M[i]����click(map[0][i])����׵Ľ���������λ����Ϊ���� 
	
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
	
	//v �������� M �������� ���е�ϵ�����, seeds��¼���е� v ,count ��¼ ���е� ���� 
	vector v = 0, *seeds = NULL, *t;
	int count = 0;
	
	STATE A[COL];	//A ��¼ v���м���� 
	int i, j;
	while(1){
		v++;	//����v = 0 
		if((0x1 << COL) <= v){
			break;
		}
		toSTATE(v, A);
		//v����ѭ������Ҫ�� ,�������е�seeds�޹� 
		if((addV(COL, A, M) == 0) && (!isRela(count, seeds, v))){
			//��v����seeds
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
	//�ھ�seeds
//	printf("\nSeeds:%d\n", count);
//	for(i = 0; i < count; i++){
//		toSTATE(seeds[i], A);
//		displayRow(COL, A);
//	} 
	if(count < 1){
		//����ӡ
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
	//��ӡѭ������ ������ 
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
