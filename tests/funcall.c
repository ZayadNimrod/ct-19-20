int foo(int b){
	return 3+b;
}


int main(){
	int a;
	int* ptr;
	
	a =foo(a);
	
	ptr = (int*)mcmalloc(sizeof(int*));
	print_i(a);
	return 1;
}