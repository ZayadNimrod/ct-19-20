int x;



int main(){

	int y;
	int z;
	
	x=0;
	y=0;
	z = read_i();
	
	
	while(x<z){
		
		y=0;
		while(y<=3){
			y=y+1;
			print_i(x);
			print_i(y);
			print_i(z);
			print_s("\n");
		}
		x=x+1;	
	}

	print_i(x);
	return 0;
}