
struct block{
	int a;
	char* v;
	int c[2];
};

void main(){
	int** myPtr ; //TODO: does not work with parser
	struct block lego[4] ;
	struct block* indirect;
	
	//lego[2].a = 7;//TODO this does not work, at parser stage
	
	**myPtr =7;
}