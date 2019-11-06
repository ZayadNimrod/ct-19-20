
//Fibonacci Series using Recursion 
//from https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
 
int fib(int n) 
{ 

   int ret;


   //print_s((char*)",goto ");
   //print_i(n);

   if (n <= 1){
    	 //print_s((char*)" ");
   		 //print_i(n);
      	 return n;
      } 
      
   
   ret = fib(n-2);
   //((char*)" n ="); 
   //print_i(n); //TODO: n has been decremented somehow???
   ret = ret+fib(n-1);
   
 
   //print_s((char*)" ");
   //print_i(ret);
   
   //ret = fib(n-2)+fib(n-1); this works tho
   return ret;
} 
  
int main () 
{ 
  int n;
  n = read_i(); 
  print_i(fib(n)); 
  return 0; 
} 
