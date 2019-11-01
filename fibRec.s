.data
.text
function_main:
move $fp $sp
addi $sp, $sp, -4
li $v0, 5
syscall
move $t9, $v0
move $t8, $sp
lw $t8, 0($t8)
move $t8, $t9
move $t9, $sp
sw $t8, 0($t9)
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $t8, $sp
lw $t8, 0($t8)
move $a0, $t8
jal function_fib
#postcall begins
lw $ra 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
#postcall ends
move $t8, $v0
move $a0, $t8
li $v0, 1
syscall
#returning from main
li $t8, 0
li $v0, 10
syscall
function_fib:
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
move $t9, $sp
lw $t9, 0($t9)
slti $t9, $t9, 1
addi $t9, $t9, -1
negu $t9, $t9
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_1
move $s7, $sp
lw $s7, 0($s7)
move $v0, $s7
#returning from function
#epilogue start
addi $sp $sp 4
addi $sp $sp 4
move $sp $fp
#epilogue end
jr $ra
j if_end_1
if_end_1:
addi $sp $sp -4
sw $t8 0($sp)
addi $sp $sp -4
sw $t9 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $s7, $sp
lw $s7, 0($s7)
addi $s7, $s7, -1
move $a0, $s7
jal function_fib
#postcall begins
lw $ra 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
lw $t8 0($sp)
addi $sp $sp 4
lw $t9 0($sp)
addi $sp $sp 4
#postcall ends
move $s7, $v0
addi $sp $sp -4
sw $s7 0($sp)
addi $sp $sp -4
sw $t8 0($sp)
addi $sp $sp -4
sw $t9 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $s6, $sp
lw $s6, 0($s6)
addi $s6, $s6, -2
move $a0, $s6
jal function_fib
#postcall begins
lw $ra 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
addi $sp $sp 4
lw $s7 0($sp)
addi $sp $sp 4
lw $t8 0($sp)
addi $sp $sp 4
lw $t9 0($sp)
addi $sp $sp 4
#postcall ends
move $s6, $v0
add $s7, $s7, $s6
move $v0, $s7
#returning from function
#epilogue start
addi $sp $sp 4
addi $sp $sp 4
move $sp $fp
#epilogue end
jr $ra
