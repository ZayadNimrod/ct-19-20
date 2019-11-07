.data
x: .space 4
string_1: .asciiz "\n"
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
addi $sp, $sp, -4
li $t9, 0
move $t8, $t9
la $t9 x
sw $t8 0($t9)
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
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
move $t9, $fp
sw $t8, -8($t9)
while_start_1:
la $t8, x
lw $t8, ($t8)
move $t9, $fp
lw $t9, -8($t9)
slt $t8, $t8, $t9
beqz $t8, while_end_1
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
while_start_2:
#gt last case
move $t9, $fp
lw $t9, -4($t9)
li $t8, 3
slt $t9, $t8, $t9
#gt last case end
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, while_end_2
move $t9, $fp
lw $t9, -4($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8, x
lw $t8, ($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -4($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -8($t8)
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
la $t8 string_1
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
addi $sp $sp 0
j while_start_2
while_end_2:
la $t8, x
lw $t8, ($t8)
addi $t8, $t8, 1
move $t9, $t8
la $t8 x
sw $t9 0($t8)
addi $sp $sp 0
j while_start_1
while_end_1:
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9, x
lw $t9, ($t9)
move $a0 $t9
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#returning from main
li $t9, 0
addi $sp $sp 8
li $v0, 10
syscall
