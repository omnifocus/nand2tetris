// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.
// R2=0;
//for (i=0;i<=r0;i++) {R2 += r1;}
@R0
D=M

@END
M[R2]=D;JEQ

@R1
D=M
@END
M[R2]=D;JEQ

@0
D=A
@R2 //R2=0
M=D
@1
D=A
@i //i=0
M=D
(LOOP)
@R0 // 
D=M
@i
D=D-M
@END
D;JLT
@R2
D=M
D=D+M[R1]
@R2
M=D
@i
M=M+1
@LOOP
0;JMP

(END)
@END
0;JMP





