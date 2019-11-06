.data
string_1: .asciiz "First "
string_2: .asciiz " terms of Fibonacci series are : "
string_3: .asciiz " "
n: .space 4
first: .space 4
second: .space 4
next: .space 4
c: .space 4
t: .space 1
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
la $t8, n
move $t8, $t9
move $t9, $fp
sw $t8, 1($t9)
li $t8, 0
la $t9, first
move $t9, $t8
move $t8, $fp
sw $t9, 1($t8)
li $t9, 1
la $t8, second
move $t8, $t9
move $t9, $fp
sw $t8, 1($t9)
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
la $t9, c
move $t9, $t8
move $t8, $fp
sw $t9, 1($t8)
while_start_1:
la $t9, c
la $t8, n
slt $t9, $t9, $t8
beqz $t9, while_end_1
#gt last case
la $t9, c
li $t8, 1
slt $t9, $t8, $t9
#gt last case end
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_else_2
la $t8, c
la $s7, next
move $s7, $t8
move $t8, $fp
sw $s7, 1($t8)
j if_end_2
if_else_2:
la $s7, first
la $t8, second
add $s7, $s7, $t8
la $t8, next
move $t8, $s7
move $s7, $fp
sw $t8, 1($s7)
la $t8, second
la $s7, first
move $s7, $t8
move $t8, $fp
sw $s7, 1($t8)
la $s7, next
la $t8, second
move $t8, $s7
move $s7, $fp
sw $t8, 1($s7)
move $sp $fp
if_end_2:
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8, next
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
la $t8 string_3
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
la $t8, c
addi $t8, $t8, 1
la $s7, c
move $s7, $t8
move $t8, $fp
sw $s7, 1($t8)
move $sp $fp
j while_start_1
while_end_1:
move $sp $fp
li $v0, 10
syscall
