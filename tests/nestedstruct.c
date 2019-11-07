
struct a {
	int i;
	char j;
};

struct b{
	struct a s;
	int i;
};


int k;
struct a first;


void main() {

  struct b second;
 
  first.i = 8;
  
  second.s=first;
  second.i=12;
  
  first.j = 'b';
  
  second.s.j = 'a'; 
  first.i = 11;
  second.s.i = 9;
  
  
  print_i(first.i);
  print_c(first.j);
  
  print_i(second.i);
  print_i(second.s.i);
  print_c(second.s.j);
}
