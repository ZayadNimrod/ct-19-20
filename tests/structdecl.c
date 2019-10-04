struct s { 
	int n;
	int d[3]; 
}; //this is what I guess the "struct_decleration" test is supposed to be


	
struct spaceship { char *manufacturer; char *model; char *date; };

int main(){
	//struct s t1 =  { 0 };  does not work. Should this work? looking at the grammar suggests not.
	//struct s t1;
	//t1 = { 0 };
	
	
	struct spaceship xwing;
	
	//xwing = {"Incom Corporation", "T-65 X-wing", "Long time ago"}; also does not work, as above...
	//crashing here since exp can be IDENT but also arrayOrFieldAccess from same start symbol...
	xwing.manufacturer = "Incom Corporation";
	xwing.model = "T-65 X-wing";
	xwing.date = "A long time ago";
	
	
	
	
	return 0;
	
	
	
}