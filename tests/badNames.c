int #badStart;
char ?badStart;
string !badStart
int 3badStart; // this s not being caught by the lexer, it sees it as an int literal followed by an identifier
char \badStart;

//the proceeding should break since it is never closed, but it passes when it hits the EOF...
string badString = "