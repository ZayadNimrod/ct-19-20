struct foo{
	int bar;
	char baz;
};

int main(){
	int* ptr;
	
	
	ptr = (int*)(char*)(int*)(struct foo*)(void*)(char*)mcmalloc(sizeof(int*));


	return 1;
}