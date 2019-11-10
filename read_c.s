.data
c: .space 4
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
#read_c
addi $sp $sp -4
sw $v0 0($sp)
li $v0, 12
syscall
move $t9, $v0
lw $v0 0($sp)
addi $sp $sp 4
#read_c ends
move $t8, $t9
la $t9 c
sw $t8 0($t9)
#read_c
addi $sp $sp -4
sw $v0 0($sp)
li $v0, 12
syscall
move $t8, $v0
lw $v0 0($sp)
addi $sp $sp 4
#read_c ends
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9, c
lw $t9, 0($t9)
addi $sp $sp -4
sw $t9 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t9, $fp
lw $t9, -4($t9)
addi $sp $sp -4
sw $t9 0($sp)
move $a0 $sp
li $v0 4
syscall
addi $sp $sp 4
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_c over
addi $sp $sp 4
#returning from main
li $v0, 10
syscall
