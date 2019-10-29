.data
y: .space 4
.text
function_main:
addi $sp, $sp, -4
li $t9, 12
addi $t9, $t9, 2
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
li $t8, 6
addi $t8, $t8, 4
move $t9, $sp
lw $t9, 0($t9)
move $t9, $t8
move $t8, $sp
sw $t9, 0($t8)
li $t9, 6
addi $t9, $t9, 4
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
li $t8, 1
negu $t8, $t8
addi $t8, $t8, 3
addi $t8, $t8, 4
move $t9, $sp
lw $t9, 0($t9)
move $t9, $t8
move $t8, $sp
sw $t9, 0($t8)
li $t9, 8
negu $t9, $t9
addi $t9, $t9, 0
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
move $t8, $sp
lw $t8, 0($t8)
move $a0, $t8
li $v0, 1
syscall
li $t8, 2
move $t9, $sp
lw $t9, 0($t9)
addi $t9, $t9, 3
mul $t8, $t8, $t9
move $t9, $sp
lw $t9, 0($t9)
move $t9, $t8
move $t8, $sp
sw $t9, 0($t8)
move $t9, $sp
lw $t9, 0($t9)
move $a0, $t9
li $v0, 1
syscall
li $t9, 6
li $t8, 2
move $s7, $sp
lw $s7, 0($s7)
mul $t8, $t8, $s7
addi $t8, $t8, 4
add $t9, $t9, $t8
move $a0, $t9
li $v0, 1
syscall
li $v0, 10
syscall
