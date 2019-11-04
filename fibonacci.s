.data
string_1: .asciiz "First "
string_2: .asciiz " terms of Fibonacci series are : "
string_3: .asciiz " "
.text
function_main:
#prologue start
move $fp $sp
#prologue end
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
li $t9, 10
move $t8, $fp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $fp
sw $t8, 0($t9)
li $t8, 0
move $t9, $fp
lw $t9, -4($t9)
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
li $t9, 1
move $t8, $fp
lw $t8, -8($t8)
move $t8, $t9
move $t9, $fp
sw $t8, -8($t9)
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
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, 0($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
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
li $t8, 0
move $t9, $fp
lw $t9, -16($t9)
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
while_start_1:
move $t9, $fp
lw $t9, -16($t9)
move $t8, $fp
lw $t8, 0($t8)
slt $t9, $t9, $t8
beqz $t9, while_end_1
#gt last case
move $t9, $fp
lw $t9, -16($t9)
li $t8, 1
slt $t9, $t8, $t9
#gt last case end
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_else_2
move $t8, $fp
lw $t8, -16($t8)
move $s7, $fp
lw $s7, -12($s7)
move $s7, $t8
move $t8, $fp
sw $s7, -12($t8)
j if_end_2
if_else_2:
move $s7, $fp
lw $s7, -4($s7)
move $t8, $fp
lw $t8, -8($t8)
add $s7, $s7, $t8
move $t8, $fp
lw $t8, -12($t8)
move $t8, $s7
move $s7, $fp
sw $t8, -12($s7)
move $t8, $fp
lw $t8, -8($t8)
move $s7, $fp
lw $s7, -4($s7)
move $s7, $t8
move $t8, $fp
sw $s7, -4($t8)
move $s7, $fp
lw $s7, -12($s7)
move $t8, $fp
lw $t8, -8($t8)
move $t8, $s7
move $s7, $fp
sw $t8, -8($s7)
addi $sp $sp 0
if_end_2:
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -12($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
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
move $t8, $fp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $s7, $fp
lw $s7, -16($s7)
move $s7, $t8
move $t8, $fp
sw $s7, -16($t8)
addi $sp $sp 0
j while_start_1
while_end_1:
addi $sp $sp 24
li $v0, 10
syscall
