.data
string_1: .asciiz "\n Move disk 1 from rod "
string_2: .asciiz " to rod "
string_3: .asciiz "\n Move disk "
string_4: .asciiz " from rod "
string_5: .asciiz " to rod "
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
li $t9, 4
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
#precall begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $t8, $fp
lw $t8, -4($t8)
move $a0, $t8
li $t8, 65
move $a1, $t8
li $t8, 67
move $a2, $t8
li $t8, 66
move $a3, $t8
#precall ends
jal function_towerOfHanoi
move $t8, $v0
#postcall begins
addi $sp $sp 4
lw $ra 0($sp)
addi $sp $sp 4
lw $v1 0($sp)
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#postcall ends
#returning from main
li $t8, 0
move $v0 $t8
addi $sp $sp 4
li $v0, 10
syscall
function_towerOfHanoi:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
#prologue end
move $t8, $fp
lw $t8, -4($t8)
li $t9, 1
seq $t8, $t8, $t9
beqz $t8, if_end_1
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
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -8($t8)
addi $sp $sp -4
sw $t8 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
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
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -12($t8)
addi $sp $sp -4
sw $t8 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
addi $sp $sp 0
j if_end_1
if_end_1:
#precall begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $t8, $fp
lw $t8, -4($t8)
addi $t8, $t8, -1
move $a0, $t8
move $t8, $fp
lw $t8, -8($t8)
move $a1, $t8
move $t8, $fp
lw $t8, -16($t8)
move $a2, $t8
move $t8, $fp
lw $t8, -12($t8)
move $a3, $t8
#precall ends
jal function_towerOfHanoi
move $t8, $v0
#postcall begins
addi $sp $sp 4
lw $ra 0($sp)
addi $sp $sp 4
lw $v1 0($sp)
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#postcall ends
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_4
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -8($t8)
addi $sp $sp -4
sw $t8 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_5
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -12($t8)
addi $sp $sp -4
sw $t8 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
#precall begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $t8, $fp
lw $t8, -4($t8)
addi $t8, $t8, -1
move $a0, $t8
move $t8, $fp
lw $t8, -16($t8)
move $a1, $t8
move $t8, $fp
lw $t8, -12($t8)
move $a2, $t8
move $t8, $fp
lw $t8, -8($t8)
move $a3, $t8
#precall ends
jal function_towerOfHanoi
move $t8, $v0
#postcall begins
addi $sp $sp 4
lw $ra 0($sp)
addi $sp $sp 4
lw $v1 0($sp)
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#postcall ends
addi $sp $sp 0
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
