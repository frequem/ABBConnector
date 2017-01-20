MODULE MainModule
	PROC main()
		var byte data;
		
		init;
		data := receive_byte();
		TPWrite "Received: ", \Num:=data;
	ENDPROC
ENDMODULE
