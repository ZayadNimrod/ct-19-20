.data
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
addi $sp $sp -4
sw $v0 0($sp)
li $v0, 5
syscall
move $t9, $v0
lw $v0 0($sp)
addi $sp $sp 4
move $t8, $fp
lw $t8, 0($t8)
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
sw $a3 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
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
lw $a0 0($sp)
addi $sp $sp 4
lw $a1 0($sp)
addi $sp $sp 4
lw $a2 0($sp)
addi $sp $sp 4
lw $a3 0($sp)
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
move $t9, $fp
lw $t9, -4($t9)
li $s7, 1
slt $t9, $s7, $t9
#gt last case end
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_1
li $s7, 1
move $v0, $s7
#returning from function
move $sp $fp
#epilogue start
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
j if_end_1
if_end_1:
#precall begins
addi $sp $sp -4
sw $t8 0($sp)
addi $sp $sp -4
sw $t9 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $s7, $fp
lw $s7, -4($s7)
addi $s7, $s7, -2
move $a0, $s7
#precall ends
jal function_fib
move $s7, $v0
#postcall begins
lw $ra 0($sp)
addi $sp $sp 4
lw $v1 0($sp)
addi $sp $sp 4
lw $v0 0($sp)
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
#precall begins
addi $sp $sp -4
sw $s7 0($sp)
addi $sp $sp -4
sw $t8 0($sp)
addi $sp $sp -4
sw $t9 0($sp)
addi $sp $sp -4
sw $a3 0($sp)
addi $sp $sp -4
sw $a2 0($sp)
addi $sp $sp -4
sw $a1 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
addi $sp $sp -4
sw $v1 0($sp)
addi $sp $sp -4
sw $ra 0($sp)
move $s6, $fp
lw $s6, -4($s6)
addi $s6, $s6, -1
move $a0, $s6
#precall ends
jal function_fib
move $s6, $v0
#postcall begins
lw $ra 0($sp)
addi $sp $sp 4
lw $v1 0($sp)
addi $sp $sp 4
lw $v0 0($sp)
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
add $s7, $s7, $s6
move $s6, $fp
lw $s6, -4($s6)
move $s6, $s7
move $s7, $fp
sw $s6, -4($s7)
move $s6, $fp
lw $s6, -4($s6)
move $v0, $s6
#returning from function
move $sp $fp
#epilogue start
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
