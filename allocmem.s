.data
.text
function_main:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
#mcmalloc
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
li $t9, 4
move $a0 $t9
li $v0 9
syscall
move $t9 $v0
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#mcmalloc over
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
#mcmalloc
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
li $t8, 4
move $a0 $t8
li $v0 9
syscall
move $t8 $v0
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#mcmalloc over
move $t9, $t8
move $t8, $fp
sw $t9, -8($t8)
#mcmalloc
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
li $t9, 4
move $a0 $t9
li $v0 9
syscall
move $t9 $v0
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#mcmalloc over
move $t8, $t9
move $t9, $fp
sw $t8, -12($t9)
li $t8, 7
move $t9, $t8
move $t8, $fp
lw $t8, -4($t8)
sw $t9, 0($t8)
li $t9, 97
move $t8, $t9
move $t9, $fp
lw $t9, -8($t9)
sw $t8, 0($t9)
li $t8, 122
move $t9, $t8
move $t8, $fp
lw $t8, -12($t8)
sw $t9, 0($t8)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t9, $fp
lw $t9, -4($t9)
lw $t9 0($t9)
move $a0 $t9
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t9, $fp
lw $t9, -8($t9)
lw $t9 0($t9)
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
lw $t9, -12($t9)
lw $t9 0($t9)
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
move $t9, $fp
lw $t9, -12($t9)
lw $t9 0($t9)
move $t8, $t9
move $t9, $fp
lw $t9, -8($t9)
sw $t8, 0($t9)
#print_i
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -4($t8)
lw $t8 0($t8)
move $a0 $t8
li $v0 1
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_i over
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -8($t8)
lw $t8 0($t8)
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
#print_c
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
move $t8, $fp
lw $t8, -12($t8)
lw $t8 0($t8)
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
#returning from main
li $t8, 0
move $v0 $t8
addi $sp $sp 12
li $v0, 10
syscall
