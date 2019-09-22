#include "minic-stdlib.h"

//tests some stuff with characters and strings
void main() {
	
	//should pass
	char escape = '\n';
	char normal = 'a';
	char empty ='';
	
	
	
	print_s((char*) "\t Second text! \n\n");
	print_s((char*) "You say \"Goodbye\"");
	print_s((char*) "");

	//should throw error
	print_s((char*) 'Hello there!');
 
}
