
struct block{
	int a;
	char* v;
	int c[2];
};

void main(){
	int** myPtr ; 
	struct block* lego[4] ;
	int i;
	
	i=0;
	//struct block* indirect;
	lego = (struct block*)mcmalloc(4*sizeof(struct block*));
	
	while (i<4){
		lego[i] = (struct block*)mcmalloc(sizeof(struct block));
		i=i+1;
	}
	
	(*lego[2]).a=5;
	
	
	
	**myPtr =7;
}