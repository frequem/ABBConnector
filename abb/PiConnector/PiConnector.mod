MODULE PiConnector
    VAR signaldi diSync;
    VAR signaldi diParity;
    VAR signaldi diData1;
    VAR signaldi diData2;
    VAR signaldi diData3;
    VAR signaldi diData4;
    
    VAR signaldo doRelais;
    VAR signaldo doReady;
    VAR signaldo doData1;
    VAR signaldo doData2;
    VAR signaldo doData3;
    VAR signaldo doData4;
    
    PROC init()
        AliasIO DI5, diData1;
        AliasIO DI6, diData2;
        AliasIO DI7, diData3;
        AliasIO DI8, diData4;
        AliasIO DI9, diParity;
        AliasIO DI10, diSync;
        
        AliasIO DO3, doRelais;
        AliasIO DO4, doReady;
        AliasIO DO5, doData1;
        AliasIO DO6, doData2;
        AliasIO DO7, doData3;
        AliasIO DO8, doData4;
    ENDPROC

    FUNC byte receive_byte()
        RETURN BitOr(BitLSh(receive_nibble(1), 4), receive_nibble(0));
    ENDFUNC
    
    FUNC byte receive_nibble(dionum state)
		var byte data:=0;
		var byte parity:=0;
		parity:=BitNeg(calc_parity(data));
		
		WHILE parity <> diParity DO
			SetDO doReady, state;
			WaitDI diSync, state;
			
			data:=0;
			data:=BitLSh(diData3, 3);
			data:=BitOr(data, BitLSh(diData3, 2));
			data:=BitOr(data, BitLSh(diData2, 1));
			data:=BitOr(data, diData1);
			
		parity:=calc_parity(data);
        ENDWHILE
        
        RETURN data;
    ENDFUNC
    
    FUNC byte calc_parity(byte data)
		RETURN BitAnd((BitAnd(data, 1) +
			   (BitAnd(BitRSh(data, 1), 1)) + 
			   (BitAnd(BitRSh(data, 2), 1)) +
			   (BitAnd(BitRSh(data, 3), 1)) + 1), 1);
    ENDFUNC
    
ENDMODULE
