.data
string_1: .asciiz "First "
string_2: .asciiz " terms of Fibonacci series are : "
string_3: .asciiz " "
.text
function_main:
move $fp $sp
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
li $t9, 10
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
li $t8, 0
move $t9, $sp
lw $t9, -4($t9)
move $t9, $t8
move $t8, $sp
sw $t9, -4($t8)
li $t9, 1
move $t8, $sp
lw $t8, -8($t8)
move $t8, $t9
move $t9, $sp
sw $t8, -8($t9)
la $t8 string_1
move $a0, $t8
li $v0, 4
syscall
move $t8, $sp
lw $t8, 0($t8)
move $a0, $t8
li $v0, 1
syscall
la $t8 string_2
move $a0, $t8
li $v0, 4
syscall
li $t8, 0
move $t9, $sp
lw $t9, -16($t9)
move $t9, $t8
move $t8, $sp
sw $t9, -16($t8)
while_start_1:
move $t9, $sp
lw $t9, -16($t9)
move $t8, $sp
lw $t8, 0($t8)
slt $t9, $t9, $t8
beqz $t9, while_end_1
move $t9, $sp
lw $t9, -16($t9)
slti $t9, $t9, 1
addi $t9, $t9, -1
negu $t9, $t9
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_else_2
move $t8, $sp
lw $t8, -16($t8)
move $s7, $sp
lw $s7, -12($s7)
move $s7, $t8
move $t8, $sp
sw $s7, -12($t8)
j if_end_2
if_else_2:
move $s7, $sp
lw $s7, -4($s7)
move $t8, $sp
lw $t8, -8($t8)
add $s7, $s7, $t8
move $t8, $sp
lw $t8, -12($t8)
move $t8, $s7
move $s7, $sp
sw $t8, -12($s7)
move $t8, $sp
lw $t8, -8($t8)
move $s7, $sp
lw $s7, -4($s7)
move $s7, $t8
move $t8, $sp
sw $s7, -4($t8)
move $s7, $sp
lw $s7, -12($s7)
move $t8, $sp
lw $t8, -8($t8)
move $t8, $s7
move $s7, $sp
sw $t8, -8($s7)
if_end_2:
move $t8, $sp
lw $t8, -12($t8)
move $a0, $t8
li $v0, 1
syscall
la $t8 string_3
move $a0, $t8
li $v0, 4
syscall
move $t8, $sp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $s7, $sp
lw $s7, -16($s7)
move $s7, $t8
move $t8, $sp
sw $s7, -16($t8)
j while_start_1
while_end_1:
li $v0, 10
syscall
