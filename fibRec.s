.data
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
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
sw $t8, 0($t9)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
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
lw $t8, 0($t8)
move $a0, $t8
#precall ends
jal function_fib
move $t8, $v0
#postcall begins
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
move $sp $fp
li $v0, 10
syscall
function_fib:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
addi $sp, $sp, -4
#gt last case
move $t8, $fp
lw $t8, -4($t8)
li $t9, 1
slt $t8, $t9, $t8
#gt last case end
addi $t8, $t8, -1
negu $t8, $t8
beqz $t8, if_end_1
move $t8, $fp
lw $t8, -4($t8)
move $v0, $t8
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
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
addi $t8, $t8, -2
move $a0, $t8
#precall ends
jal function_fib
move $t8, $v0
#postcall begins
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
move $t9, $t8
move $t8, $fp
sw $t9, -8($t8)
move $t9, $fp
lw $t9, -8($t9)
#precall begins
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
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $t8, $fp
lw $t8, -4($t8)
addi $t8, $t8, -1
move $a0, $t8
#precall ends
jal function_fib
move $t8, $v0
#postcall begins
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
lw $t9 0($sp)
addi $sp $sp 4
#postcall ends
add $t9, $t9, $t8
move $t8, $t9
move $t9, $fp
sw $t8, -8($t9)
move $t8, $fp
lw $t8, -8($t8)
move $v0, $t8
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
