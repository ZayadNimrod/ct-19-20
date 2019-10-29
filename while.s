.data
.text
function_main:
addi $sp, $sp, -4
addi $sp, $sp, -4
li $t9, 97
move $t8, $sp
lw $t8, -4($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 4($t9)
li $t8, 0
move $t9, $sp
lw $t9, 0($t9)
move $t9, $t8
move $t8, $sp
sw $t9, 0($t8)
while_start_1:
move $t9, $sp
lw $t9, 0($t9)
slti $t9, $t9, 7
addi $t9, $t9, -1
negu $t9, $t9
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, while_end_1
move $t9, $sp
lw $t9, 0($t9)
move $a0, $t9
li $v0, 1
syscall
move $t9, $sp
lw $t9, 0($t9)
addi $t9, $t9, 1
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
j while_start_1
while_end_1:
li $v0, 10
syscall
