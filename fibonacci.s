.data
n: .space 4
first: .space 4
second: .space 4
next: .space 4
c: .space 4
t: .space 4
string_1: .asciiz "First "
string_2: .asciiz " terms of Fibonacci series are :\n "
string_3: .asciiz " "
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
#read_i
addi $sp $sp -4
sw $v0 0($sp)
li $v0, 5
syscall
move $t9, $v0
lw $v0 0($sp)
addi $sp $sp 4
#read_i ends
move $t8, $t9
la $t9 n
sw $t8 0($t9)
li $t8, 0
move $t9, $t8
la $t8 first
sw $t9 0($t8)
li $t9, 1
move $t8, $t9
la $t9 second
sw $t8 0($t9)
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_1
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8, n
lw $t8, ($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_2
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
li $t8, 0
move $t9, $t8
la $t8 c
sw $t9 0($t8)
while_start_1:
la $t9, c
lw $t9, ($t9)
la $t8, n
lw $t8, ($t8)
slt $t9, $t9, $t8
beqz $t9, while_end_1
#gt last case
la $t9, c
lw $t9, ($t9)
li $t8, 1
slt $t9, $t8, $t9
#gt last case end
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_else_2
la $t9, c
lw $t9, ($t9)
move $t8, $t9
la $t9 next
sw $t8 0($t9)
j if_end_2
if_else_2:
la $t8, first
lw $t8, ($t8)
la $t9, second
lw $t9, ($t9)
add $t8, $t8, $t9
move $t9, $t8
la $t8 next
sw $t9 0($t8)
la $t9, second
lw $t9, ($t9)
move $t8, $t9
la $t9 first
sw $t8 0($t9)
la $t8, next
lw $t8, ($t8)
move $t9, $t8
la $t8 second
sw $t9 0($t8)
move $sp $fp
if_end_2:
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9, next
lw $t9, ($t9)
move $a0 $t9
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_3
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
la $t9, c
lw $t9, ($t9)
addi $t9, $t9, 1
move $t8, $t9
la $t9 c
sw $t8 0($t9)
move $sp $fp
j while_start_1
while_end_1:
move $sp $fp
li $v0, 10
syscall
