.data
a11: .space 4
a12: .space 4
a13: .space 4
a21: .space 4
a22: .space 4
a23: .space 4
a31: .space 4
a32: .space 4
a33: .space 4
empty: .space 4
string_1: .asciiz "
"
string_2: .asciiz "     1   2   3
"
string_3: .asciiz "   +---+---+---+
"
string_4: .asciiz "a  | "
string_5: .asciiz " | "
string_6: .asciiz " | "
string_7: .asciiz " |
"
string_8: .asciiz "   +---+---+---+
"
string_9: .asciiz "b  | "
string_10: .asciiz " | "
string_11: .asciiz " | "
string_12: .asciiz " |
"
string_13: .asciiz "   +---+---+---+
"
string_14: .asciiz "c  | "
string_15: .asciiz " | "
string_16: .asciiz " | "
string_17: .asciiz " |
"
string_18: .asciiz "   +---+---+---+
"
string_19: .asciiz "
"
string_20: .asciiz "Player "
string_21: .asciiz " has won!
"
string_22: .asciiz "Player "
string_23: .asciiz " select move (e.g. a2)>"
string_24: .asciiz "That is not a valid move!
"
string_25: .asciiz "That move is not possible!
"
string_26: .asciiz "It's a draw!
"
string_27: .asciiz "Play again? (y/n)> "
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
addi $sp, $sp, -4
li $t9, 32
move $t8, $t9
la $t9 empty
sw $t8 0($t9)
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, 0($t8)
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
#precall ends
jal function_reset
move $t9, $v0
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
#precall ends
jal function_printGame
move $t9, $v0
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
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
while_start_1:
move $t8, $fp
lw $t8, 0($t8)
beqz $t8, while_end_1
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
#precall ends
jal function_selectmove
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
#precall ends
jal function_get_mark
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
#precall ends
jal function_printGame
move $t9, $v0
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
move $t9, $fp
lw $t9, -8($t9)
move $a0, $t9
#precall ends
jal function_won
move $t9, $v0
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
beqz $t9, if_else_2
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
move $t9, $fp
lw $t9, -4($t9)
move $a0, $t9
#precall ends
jal function_printWinner
move $t9, $v0
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
li $t9, 0
move $t8, $t9
move $t9, $fp
sw $t8, 0($t9)
move $sp $fp
j if_end_2
if_else_2:
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
#precall ends
jal function_full
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
li $t9, 1
seq $t8, $t8, $t9
beqz $t8, if_else_3
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_26
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, 0($t8)
move $sp $fp
j if_end_3
if_else_3:
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
move $t9, $fp
lw $t9, -4($t9)
move $a0, $t9
#precall ends
jal function_switchPlayer
move $t9, $v0
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
move $t8, $t9
move $t9, $fp
sw $t8, -4($t9)
move $sp $fp
if_end_3:
if_end_2:
move $t8, $fp
lw $t8, 0($t8)
li $t9, 0
seq $t8, $t8, $t9
beqz $t8, if_end_4
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_27
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
#precall ends
jal function_read_c
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
sw $t9, -12($t8)
move $t9, $fp
lw $t9, -12($t9)
li $t8, 121
seq $t9, $t9, $t8
beqz $t9, if_else_5
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, 0($t9)
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
#precall ends
jal function_reset
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
move $sp $fp
j if_end_5
if_else_5:
move $t8, $fp
lw $t8, -12($t8)
li $t9, 89
seq $t8, $t8, $t9
beqz $t8, if_end_6
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, 0($t8)
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
#precall ends
jal function_reset
move $t9, $v0
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
move $sp $fp
j if_end_6
if_end_6:
move $sp $fp
if_end_5:
move $sp $fp
j if_end_4
if_end_4:
move $sp $fp
j while_start_1
while_end_1:
move $sp $fp
li $v0, 10
syscall
function_reset:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
la $t9, empty
lw $t9, ($t9)
move $t8, $t9
la $t9 a11
sw $t8 0($t9)
la $t8, empty
lw $t8, ($t8)
move $t9, $t8
la $t8 a12
sw $t9 0($t8)
la $t9, empty
lw $t9, ($t9)
move $t8, $t9
la $t9 a13
sw $t8 0($t9)
la $t8, empty
lw $t8, ($t8)
move $t9, $t8
la $t8 a21
sw $t9 0($t8)
la $t9, empty
lw $t9, ($t9)
move $t8, $t9
la $t9 a22
sw $t8 0($t9)
la $t8, empty
lw $t8, ($t8)
move $t9, $t8
la $t8 a23
sw $t9 0($t8)
la $t9, empty
lw $t9, ($t9)
move $t8, $t9
la $t9 a31
sw $t8 0($t9)
la $t8, empty
lw $t8, ($t8)
move $t9, $t8
la $t8 a32
sw $t9 0($t8)
la $t9, empty
lw $t9, ($t9)
move $t8, $t9
la $t9 a33
sw $t8 0($t9)
move $sp $fp
function_full:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
addi $sp, $sp, -4
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
la $t9, a11
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_7
move $t9, $fp
lw $t9, -16($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -16($t9)
j if_end_7
if_end_7:
la $t8, a21
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
addi $t8, $t8, -1
negu $t8, $t8
beqz $t8, if_end_8
move $t8, $fp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
j if_end_8
if_end_8:
la $t9, a31
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_9
move $t9, $fp
lw $t9, -16($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -16($t9)
j if_end_9
if_end_9:
la $t8, a12
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
addi $t8, $t8, -1
negu $t8, $t8
beqz $t8, if_end_10
move $t8, $fp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
j if_end_10
if_end_10:
la $t9, a22
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_11
move $t9, $fp
lw $t9, -16($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -16($t9)
j if_end_11
if_end_11:
la $t8, a32
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
addi $t8, $t8, -1
negu $t8, $t8
beqz $t8, if_end_12
move $t8, $fp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
j if_end_12
if_end_12:
la $t9, a13
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_13
move $t9, $fp
lw $t9, -16($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -16($t9)
j if_end_13
if_end_13:
la $t8, a23
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
addi $t8, $t8, -1
negu $t8, $t8
beqz $t8, if_end_14
move $t8, $fp
lw $t8, -16($t8)
addi $t8, $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -16($t8)
j if_end_14
if_end_14:
la $t9, a33
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
addi $t9, $t9, -1
negu $t9, $t9
beqz $t9, if_end_15
move $t9, $fp
lw $t9, -16($t9)
addi $t9, $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -16($t9)
j if_end_15
if_end_15:
move $t8, $fp
lw $t8, -16($t8)
li $t9, 9
seq $t8, $t8, $t9
beqz $t8, if_else_16
li $t8, 1
move $v0, $t8
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
j if_end_16
if_else_16:
li $t8, 0
move $v0, $t8
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
if_end_16:
move $sp $fp
function_set:
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
#prologue end
addi $sp, $sp, -4
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
move $t9, $fp
lw $t9, -4($t9)
li $t8, 97
seq $t9, $t9, $t8
beqz $t9, if_else_17
move $t9, $fp
lw $t9, -8($t9)
li $t8, 1
seq $t9, $t9, $t8
beqz $t9, if_else_18
la $t9, a11
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_19
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a11
sw $t8 0($t9)
j if_end_19
if_else_19:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_19:
move $sp $fp
j if_end_18
if_else_18:
move $t9, $fp
lw $t9, -8($t9)
li $t8, 2
seq $t9, $t9, $t8
beqz $t9, if_else_20
la $t9, a12
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_21
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a12
sw $t8 0($t9)
j if_end_21
if_else_21:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_21:
move $sp $fp
j if_end_20
if_else_20:
move $t9, $fp
lw $t9, -8($t9)
li $t8, 3
seq $t9, $t9, $t8
beqz $t9, if_else_22
la $t9, a13
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_23
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a13
sw $t8 0($t9)
j if_end_23
if_else_23:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_23:
move $sp $fp
j if_end_22
if_else_22:
li $t9, 0
move $t8, $t9
move $t9, $fp
sw $t8, -26($t9)
move $sp $fp
if_end_22:
move $sp $fp
if_end_20:
move $sp $fp
if_end_18:
move $sp $fp
j if_end_17
if_else_17:
move $t8, $fp
lw $t8, -4($t8)
li $t9, 98
seq $t8, $t8, $t9
beqz $t8, if_else_24
move $t8, $fp
lw $t8, -8($t8)
li $t9, 1
seq $t8, $t8, $t9
beqz $t8, if_else_25
la $t8, a21
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
beqz $t8, if_else_26
move $t8, $fp
lw $t8, -12($t8)
move $t9, $t8
la $t8 a21
sw $t9 0($t8)
j if_end_26
if_else_26:
li $t9, -1
move $t8, $t9
move $t9, $fp
sw $t8, -26($t9)
if_end_26:
move $sp $fp
j if_end_25
if_else_25:
move $t8, $fp
lw $t8, -8($t8)
li $t9, 2
seq $t8, $t8, $t9
beqz $t8, if_else_27
la $t8, a22
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
beqz $t8, if_else_28
move $t8, $fp
lw $t8, -12($t8)
move $t9, $t8
la $t8 a22
sw $t9 0($t8)
j if_end_28
if_else_28:
li $t9, -1
move $t8, $t9
move $t9, $fp
sw $t8, -26($t9)
if_end_28:
move $sp $fp
j if_end_27
if_else_27:
move $t8, $fp
lw $t8, -8($t8)
li $t9, 3
seq $t8, $t8, $t9
beqz $t8, if_else_29
la $t8, a23
lw $t8, ($t8)
la $t9, empty
lw $t9, ($t9)
seq $t8, $t8, $t9
beqz $t8, if_else_30
move $t8, $fp
lw $t8, -12($t8)
move $t9, $t8
la $t8 a23
sw $t9 0($t8)
j if_end_30
if_else_30:
li $t9, -1
move $t8, $t9
move $t9, $fp
sw $t8, -26($t9)
if_end_30:
move $sp $fp
j if_end_29
if_else_29:
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
move $sp $fp
if_end_29:
move $sp $fp
if_end_27:
move $sp $fp
if_end_25:
move $sp $fp
j if_end_24
if_else_24:
move $t9, $fp
lw $t9, -4($t9)
li $t8, 99
seq $t9, $t9, $t8
beqz $t9, if_else_31
move $t9, $fp
lw $t9, -8($t9)
li $t8, 1
seq $t9, $t9, $t8
beqz $t9, if_else_32
la $t9, a31
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_33
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a31
sw $t8 0($t9)
j if_end_33
if_else_33:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_33:
move $sp $fp
j if_end_32
if_else_32:
move $t9, $fp
lw $t9, -8($t9)
li $t8, 2
seq $t9, $t9, $t8
beqz $t9, if_else_34
la $t9, a32
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_35
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a32
sw $t8 0($t9)
j if_end_35
if_else_35:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_35:
move $sp $fp
j if_end_34
if_else_34:
move $t9, $fp
lw $t9, -8($t9)
li $t8, 3
seq $t9, $t9, $t8
beqz $t9, if_else_36
la $t9, a33
lw $t9, ($t9)
la $t8, empty
lw $t8, ($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_37
move $t9, $fp
lw $t9, -12($t9)
move $t8, $t9
la $t9 a33
sw $t8 0($t9)
j if_end_37
if_else_37:
li $t8, -1
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
if_end_37:
move $sp $fp
j if_end_36
if_else_36:
li $t9, 0
move $t8, $t9
move $t9, $fp
sw $t8, -26($t9)
move $sp $fp
if_end_36:
move $sp $fp
if_end_34:
move $sp $fp
if_end_32:
move $sp $fp
j if_end_31
if_else_31:
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -26($t8)
move $sp $fp
if_end_31:
move $sp $fp
if_end_24:
move $sp $fp
if_end_17:
move $t9, $fp
lw $t9, -26($t9)
move $v0, $t9
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
function_printGame:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
#prologue end
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_1
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_2
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_3
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_4
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a11
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_5
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a12
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_6
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a13
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_7
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_8
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_9
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a21
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_10
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a22
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_11
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a23
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_12
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_13
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_14
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a31
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_15
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a32
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_16
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
la $t9, a33
lw $t9, ($t9)
move $a0, $t9
#precall ends
jal function_print_c
move $t9, $v0
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_17
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_18
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_19
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
move $sp $fp
function_printWinner:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_20
move $a0, $t9
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
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t9 string_21
move $a0, $t9
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
move $sp $fp
function_switchPlayer:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
move $t9, $fp
lw $t9, -4($t9)
li $t8, 1
seq $t9, $t9, $t8
beqz $t9, if_else_38
li $t9, 2
move $v0, $t9
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
j if_end_38
if_else_38:
li $t9, 1
move $v0, $t9
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
if_end_38:
move $sp $fp
function_get_mark:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
move $t9, $fp
lw $t9, -4($t9)
li $t8, 1
seq $t9, $t9, $t8
beqz $t9, if_else_39
li $t9, 88
move $v0, $t9
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
j if_end_39
if_else_39:
li $t9, 79
move $v0, $t9
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
if_end_39:
move $sp $fp
function_selectmove:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
addi $sp, $sp, -4
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -54($t9)
while_start_40:
move $t8, $fp
lw $t8, -54($t8)
beqz $t8, while_end_40
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_22
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
la $t8 string_23
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
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
#precall ends
jal function_read_c
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
sw $t9, -46($t8)
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
sw $t8, -50($t9)
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
#precall ends
jal function_get_mark
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
sw $t9, -62($t8)
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
move $t9, $fp
lw $t9, -46($t9)
move $a0, $t9
move $t9, $fp
lw $t9, -50($t9)
move $a1, $t9
move $t9, $fp
lw $t9, -62($t9)
move $a2, $t9
#precall ends
jal function_set
move $t9, $v0
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
move $t8, $t9
move $t9, $fp
sw $t8, -58($t9)
move $t8, $fp
lw $t8, -58($t8)
li $t9, 0
seq $t8, $t8, $t9
beqz $t8, if_else_41
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_24
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
move $sp $fp
j if_end_41
if_else_41:
move $t8, $fp
lw $t8, -58($t8)
move $t9, $fp
lw $t9, -58($t9)
addi $t9, $t9, -1
seq $t8, $t8, $t9
beqz $t8, if_else_42
#print_s begins
addi $sp $sp -4
sw $a0 0($sp)
addi $sp $sp -4
sw $v0 0($sp)
la $t8 string_25
move $a0, $t8
li $v0, 4
syscall
lw $v0 0($sp)
addi $sp $sp 4
lw $a0 0($sp)
addi $sp $sp 4
#print_s ends
j if_end_42
if_else_42:
li $t8, 0
move $t9, $t8
move $t8, $fp
sw $t9, -54($t8)
if_end_42:
move $sp $fp
if_end_41:
move $sp $fp
j while_start_40
while_end_40:
move $sp $fp
function_won:
#prologue start
addi $sp $sp -4
sw $fp 0($sp)
move $fp $sp
addi $sp $sp -4
sw $a0 0($sp)
#prologue end
addi $sp, $sp, -4
li $t9, 0
move $t8, $t9
move $t9, $fp
sw $t8, -67($t9)
la $t8, a11
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_43
la $t8, a21
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_else_44
la $t8, a31
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_45
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -67($t8)
move $sp $fp
j if_end_45
if_end_45:
move $sp $fp
j if_end_44
if_else_44:
la $t9, a22
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_else_46
la $t9, a33
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_47
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -67($t9)
move $sp $fp
j if_end_47
if_end_47:
move $sp $fp
j if_end_46
if_else_46:
la $t8, a12
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_48
la $t8, a13
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_49
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -67($t8)
move $sp $fp
j if_end_49
if_end_49:
move $sp $fp
j if_end_48
if_end_48:
move $sp $fp
if_end_46:
move $sp $fp
if_end_44:
move $sp $fp
j if_end_43
if_end_43:
la $t9, a12
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_50
la $t9, a22
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_51
la $t9, a32
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_52
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -67($t9)
move $sp $fp
j if_end_52
if_end_52:
move $sp $fp
j if_end_51
if_end_51:
move $sp $fp
j if_end_50
if_end_50:
la $t8, a13
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_53
la $t8, a23
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_else_54
la $t8, a33
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_55
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -67($t8)
move $sp $fp
j if_end_55
if_end_55:
move $sp $fp
j if_end_54
if_else_54:
la $t9, a22
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_56
la $t9, a31
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_57
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -67($t9)
move $sp $fp
j if_end_57
if_end_57:
move $sp $fp
j if_end_56
if_end_56:
move $sp $fp
if_end_54:
move $sp $fp
j if_end_53
if_end_53:
la $t8, a21
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_58
la $t8, a22
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_59
la $t8, a23
lw $t8, ($t8)
move $t9, $fp
lw $t9, -4($t9)
seq $t8, $t8, $t9
beqz $t8, if_end_60
li $t8, 1
move $t9, $t8
move $t8, $fp
sw $t9, -67($t8)
move $sp $fp
j if_end_60
if_end_60:
move $sp $fp
j if_end_59
if_end_59:
move $sp $fp
j if_end_58
if_end_58:
la $t9, a31
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_61
la $t9, a32
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_62
la $t9, a33
lw $t9, ($t9)
move $t8, $fp
lw $t8, -4($t8)
seq $t9, $t9, $t8
beqz $t9, if_end_63
li $t9, 1
move $t8, $t9
move $t9, $fp
sw $t8, -67($t9)
move $sp $fp
j if_end_63
if_end_63:
move $sp $fp
j if_end_62
if_end_62:
move $sp $fp
j if_end_61
if_end_61:
move $t8, $fp
lw $t8, -67($t8)
move $v0, $t8
#returning from function
#epilogue start
move $sp $fp
lw $fp 0($sp)
addi $sp $sp 4
#epilogue end
jr $ra
move $sp $fp
