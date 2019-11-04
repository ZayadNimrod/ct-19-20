
//Fibonacci Series using Recursion 
//from https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
 
int fib(int n) 
{ 
   if (n <= 1)
      return n; 
   print_s((char*)"fib:\n");
   print_i(n);
   return fib(n-1) + fib(n-2); 
} 
  
int main () 
{ 
  int n;
  n = read_i(); 
  print_i(fib(n)); 
  return 0; 
} 
