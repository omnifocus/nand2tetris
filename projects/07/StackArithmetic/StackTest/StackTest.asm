@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE0
D;JNE
(CMD_TRUE0)
@SP
A=M
M=-1
@CUR_CMD0
0;JMP
(CMD_FALSE0)
@SP
A=M
M=0
(CUR_CMD0)
@SP
M=M+1
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE1
D;JNE
(CMD_TRUE1)
@SP
A=M
M=-1
@CUR_CMD1
0;JMP
(CMD_FALSE1)
@SP
A=M
M=0
(CUR_CMD1)
@SP
M=M+1
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE2
D;JNE
(CMD_TRUE2)
@SP
A=M
M=-1
@CUR_CMD2
0;JMP
(CMD_FALSE2)
@SP
A=M
M=0
(CUR_CMD2)
@SP
M=M+1
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE3
D;JGE
(CMD_TRUE3)
@SP
A=M
M=-1
@CUR_CMD3
0;JMP
(CMD_FALSE3)
@SP
A=M
M=0
(CUR_CMD3)
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE4
D;JGE
(CMD_TRUE4)
@SP
A=M
M=-1
@CUR_CMD4
0;JMP
(CMD_FALSE4)
@SP
A=M
M=0
(CUR_CMD4)
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE5
D;JGE
(CMD_TRUE5)
@SP
A=M
M=-1
@CUR_CMD5
0;JMP
(CMD_FALSE5)
@SP
A=M
M=0
(CUR_CMD5)
@SP
M=M+1
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE6
D;JLE
(CMD_TRUE6)
@SP
A=M
M=-1
@CUR_CMD6
0;JMP
(CMD_FALSE6)
@SP
A=M
M=0
(CUR_CMD6)
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE7
D;JLE
(CMD_TRUE7)
@SP
A=M
M=-1
@CUR_CMD7
0;JMP
(CMD_FALSE7)
@SP
A=M
M=0
(CUR_CMD7)
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
D=M-D
@CMD_FALSE8
D;JLE
(CMD_TRUE8)
@SP
A=M
M=-1
@CUR_CMD8
0;JMP
(CMD_FALSE8)
@SP
A=M
M=0
(CUR_CMD8)
@SP
M=M+1
@57
D=A
@SP
A=M
M=D
@SP
M=M+1
@31
D=A
@SP
A=M
M=D
@SP
M=M+1
@53
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
M=D+M
@SP
M=M+1
@112
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
M=M-D
@SP
M=M+1
@SP
AM=M-1
M=-M
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
M=D&M
@SP
M=M+1
@82
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
AM=M-1
M=D|M
@SP
M=M+1
@SP
AM=M-1
M=!M
@SP
M=M+1
