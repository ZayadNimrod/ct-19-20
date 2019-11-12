int main(){
	int* intPtr;
	char* charPtr;
	char* charPtr2;
	//int* intArrPtr[3];
	
	intPtr = (int*)mcmalloc(sizeof(int*));
	charPtr = (char*)mcmalloc(sizeof(char*));
	charPtr2 = (char*)mcmalloc(sizeof(char*));
	//intArrPtr = (int*)mcmalloc(sizeof(int*)*3);
	
	*intPtr = 7;
	
	*charPtr = 'a';
	*charPtr2 = 'z';
	
	
	
	print_i(*intPtr);
	print_c(*charPtr);
	print_c(*charPtr2);
	
	//print_i(*intArrPtr[0]);
	//print_i(*intArrPtr[1]);
	//print_i(*intArrPtr[2]);
	
	*charPtr = *charPtr2;
	
	print_i(*intPtr);
	print_c(*charPtr);
	print_c(*charPtr2);
	
	return 0;
	

}