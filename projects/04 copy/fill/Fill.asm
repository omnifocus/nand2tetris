// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
//16384
//24576
(Begin)
@24576
D=M
@WHITEN
D;JEQ
@BLACKEN
0;JMP

// 把接下来的 256 * 32 个WORD 都标黑 
// 
//
(BLACKEN)

@16384
D=A
@addr
M=D

@counter_row
M=1

(LOOP_ROW)

@256
D=A
@row
M=D

@counter_row
D=M-D

@Begin
D;JEQ

//提前++
@counter_row
M=M+1


@counter_col
M=1

(LOOP_COL)

@32
D=A

@counter_col
D=M-D

@LOOP_ROW
D;JEQ

@counter_col
M=M+1

@addr
A=M
M=-1

@addr
M=M+1

@LOOP_COL
0;JMP






(WHITEN)

@16384
D=A
@addr_W
M=D


@counter_row_w
M=1




(LOOP_ROW_W)

@counter_col_w
M=1

@256
D=A

@counter_row_w
D=M-D

@Begin
D;JEQ

@counter_row_w
M=M+1

(LOOP_COL_W)

@32
D=A

@counter_col_w
D=M-D

@LOOP_ROW_W
D;JEQ

@counter_col_w
M=M+1

@addr_W
A=M
M=0

@addr_W
M=M+1

@LOOP_COL_W
0;JMP