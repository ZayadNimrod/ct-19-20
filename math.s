.data
y: .space 4
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
li $t9, 12
addi $t9, $t9, 2
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
li $t8, 6
addi $t8, $t8, 4
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
li $t9, 6
addi $t9, $t9, 4
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
li $t8, 1
negu $t8, $t8
addi $t8, $t8, 3
addi $t8, $t8, 4
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
li $t9, 8
negu $t9, $t9
addi $t9, $t9, 0
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
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
li $t8, 2
move $t9, $fp
lw $t9, -4($t9)
negu $t9, $t9
addi $t9, $t9, 3
mul $t8, $t8, $t9
move $t9, $t8
move $t8, $fp
sw $t9, -4($t8)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t9, $fp
lw $t9, -4($t9)
move $a0 $t9
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
li $t9, 6
li $t8, 2
move $s7, $fp
lw $s7, -4($s7)
mul $t8, $t8, $s7
addi $t8, $t8, 4
add $t9, $t9, $t8
move $a0 $t9
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
li $t9, 1
move $t8, $t9
la $t9 y
sw $t8 0($t9)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8, y
lw $t8, 0($t8)
move $t9, $fp
lw $t9, -4($t9)
negu $t9, $t9
add $t8, $t8, $t9
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#returning from main
li $t8, 0
addi $sp $sp 4
li $v0, 10
syscall
