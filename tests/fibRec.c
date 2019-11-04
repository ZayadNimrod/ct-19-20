
//Fibonacci Series using Recursion 
//from https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
 
int fib(int n) 
{ 

   	int ret;

	//print_s((char*)"\nfib:");
   	//print_i(n);
   	
   if (n <= 1){
      	 return n;
      } 
      
   
   ret = fib(n-2)+ fib(n-1);
   print_i(ret);
   print_s((char*)" ");
   return ret;
} 
  
int main () 
{ 
  int n;
  n = read_i(); 
  print_i(fib(n)); 
  return 0; 
} 
