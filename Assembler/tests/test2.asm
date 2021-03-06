; Tests multiple instructions of the same type.
; (see the two LEA instructions)
Test2    .ORIG   x30B0
count    .FILL   #4
Begin    LD      ACC,count    ;R1 <- 4
         LEA     R0,msg
loop     TRAP    x22        ;print "hi! "
         ADD     ACC,ACC,#-1 ;R1--
         BRP     loop
         JMP     Next
msg      .STRZ   "hi! "
Next     AND     R0,R0,x0   ;R0 <- 0
         NOT     R0,R0      ;R0 <- xFFFF
         ST      R0,Array    ;M[Array] <-xFFFF
         LEA     R5,Array
         ST      R0,Array
         LD      R6,=#100    ;R6 <= #100
         STR     R0,R5,#1
         TRAP    x25
ACC      .EQU    #1
; ----- Scratch Space -----
Array    .BLKW   #3
         .FILL   x10 ; No .END operand
         .END