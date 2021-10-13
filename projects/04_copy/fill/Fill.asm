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
//统一不变的东西放最前面
//BEGIN是一个大循环
//每次BLACK LIGHT开时都分别要把rowi rowi_light清空
@256
D=A

@row
M=D

@32
D=A

@col
M=D





(BEGIN)
@24576
D=M

@SCREEN_BLACK
D;JNE



@SCREEN_LIGHT
D;JEQ

(SCREEN_BLACK)

@0
D=A
@rowi
M=D

@16384
D=A
@current_register
M=D

(LOOP_ROW)

@rowi
D=M
@row
D=M-D

@BEGIN
D-1;JLE

//自增只能在这写
@rowi
M=M+1

@0
D=A
@coli
M=D

(LOOP_COL)


@coli
D=M
@col
D=M-D

@LOOP_ROW
D-1;JLE

//把当前的register的值全设为-1
@current_register
A=M
M=-1

//指向下一个register
@current_register
M=M+1

// coli ++
@coli
M=M+1

@LOOP_COL
0;JMP



(SCREEN_LIGHT)

@0
D=A
@rowi_light
M=D


@16384
D=A
@current_register_light
M=D

(LOOP_ROW_LIGHT)

@rowi_light
D=M
@row
D=M-D

@BEGIN
D-1;JLE

//自增只能在这写
@rowi_light
M=M+1

@0
D=A
@coli_light
M=D

(LOOP_COL_LIGHT)


@coli_light
D=M
@col
D=M-D

@LOOP_ROW_LIGHT
D-1;JLE

//把当前的register的值全设为-1
@current_register_light
A=M
M=0

//指向下一个register
@current_register_light
M=M+1

// coli ++
@coli_light
M=M+1

@LOOP_COL_LIGHT
0;JMP


