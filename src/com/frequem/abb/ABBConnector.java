package com.frequem.abb;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.GpioUtil;

/**
 *
 * @author frequem (+florian)
 */
public class ABBConnector {
    
    private final static int DEBUGMODE_NODEBUG = 0;
    private final static int DEBUGMODE_SHOWMESSAGES = 1;
    private final static int DEBUGMODE_NORASPI = 2;
    
    private final static int DEBUG = DEBUGMODE_NORASPI;
    
    private final static int DATA1 = 0;
    private final static int DATA2 = 1;
    private final static int DATA3 = 2;
    private final static int DATA4 = 3;
    private final static int PARITY = 4;
    private final static int SYNC = 5;
    
    private final GpioController controller;
    
    private final GpioPinDigitalOutput OUT_GPIO24_DI5; //DATA1
    private final GpioPinDigitalOutput OUT_GPIO25_DI6; //DATA2
    private final GpioPinDigitalOutput OUT_GPIO26_DI7; //DATA3
    private final GpioPinDigitalOutput OUT_GPIO27_DI8; //DATA4
    private final GpioPinDigitalOutput OUT_GPIO28_DI9; //PARITY
    private final GpioPinDigitalOutput OUT_GPIO29_DI10; //SYNC
    
    private final GpioPinDigitalOutput OUT_GPIO11_LED;
    private final GpioPinDigitalOutput OUT_GPIO10_LED;
    private final GpioPinDigitalOutput OUT_GPIO6_LED;
       
    private final GpioPinDigitalInput IN_GPIO12_DO3; //ABB_RELAIS_IN
    private final GpioPinDigitalInput IN_GPIO3_DO4; //ABB_READY
    private final GpioPinDigitalInput IN_GPIO2_DO5;
    private final GpioPinDigitalInput IN_GPIO0_DO6;
    private final GpioPinDigitalInput IN_GPIO22_DO7;
    private final GpioPinDigitalInput IN_GPIO23_DO8;
    
    public ABBConnector(){
        if(DEBUG >= DEBUGMODE_NORASPI){
            controller = null;
            OUT_GPIO24_DI5  = null;
            OUT_GPIO25_DI6  = null;
            OUT_GPIO26_DI7  = null;
            OUT_GPIO27_DI8  = null;
            OUT_GPIO28_DI9  = null;
            OUT_GPIO29_DI10 = null; 
            OUT_GPIO11_LED  = null;
            OUT_GPIO10_LED  = null;
            OUT_GPIO6_LED = null;
            IN_GPIO12_DO3 = null;
            IN_GPIO3_DO4  = null;
            IN_GPIO2_DO5  = null;
            IN_GPIO0_DO6  = null;
            IN_GPIO22_DO7 = null;
            IN_GPIO23_DO8 = null;
            return;
        }
        
        if(!GpioUtil.isPrivilegedAccessRequired())
              GpioUtil.enableNonPrivilegedAccess();
        
        controller = GpioFactory.getInstance();
        
        OUT_GPIO24_DI5  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_24, "DI5", PinState.LOW);   
        OUT_GPIO25_DI6  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_25, "DI6", PinState.LOW);  
        OUT_GPIO26_DI7  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_26, "DI7", PinState.LOW);  
        OUT_GPIO27_DI8  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_27, "DI8", PinState.LOW);  
        OUT_GPIO28_DI9  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_28, "DI9", PinState.LOW);  
        OUT_GPIO29_DI10 = controller.provisionDigitalOutputPin(RaspiPin.GPIO_29, "DI10", PinState.LOW);  
        
        
        OUT_GPIO24_DI5.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO25_DI6.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO26_DI7.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO27_DI8.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO28_DI9.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO29_DI10.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        OUT_GPIO11_LED  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_11, "LED_AUSGANG_GPIO11", PinState.LOW);  
        OUT_GPIO10_LED  = controller.provisionDigitalOutputPin(RaspiPin.GPIO_10, "LED_AUSGANG_GPIO10", PinState.LOW);  
        OUT_GPIO6_LED = controller.provisionDigitalOutputPin(RaspiPin.GPIO_06, "LED_AUSGANG_GPIO6", PinState.LOW);  
        
        OUT_GPIO11_LED.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO10_LED.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        OUT_GPIO6_LED.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        
        
        IN_GPIO12_DO3 = controller.provisionDigitalInputPin(RaspiPin.GPIO_12, "DO3", PinPullResistance.OFF);
        IN_GPIO3_DO4  = controller.provisionDigitalInputPin(RaspiPin.GPIO_03, "DO4", PinPullResistance.OFF);
        IN_GPIO2_DO5  = controller.provisionDigitalInputPin(RaspiPin.GPIO_02, "DO5", PinPullResistance.OFF);
        IN_GPIO0_DO6  = controller.provisionDigitalInputPin(RaspiPin.GPIO_00, "DO6", PinPullResistance.OFF);
        IN_GPIO22_DO7 = controller.provisionDigitalInputPin(RaspiPin.GPIO_22, "DO7", PinPullResistance.OFF);
        IN_GPIO23_DO8 = controller.provisionDigitalInputPin(RaspiPin.GPIO_23, "DO8", PinPullResistance.OFF);
        
    }
    
    public void close(){
        if(DEBUG >= DEBUGMODE_NORASPI)
            return;
        
        this.controller.shutdown();
    }
    
    public void setOutputPinState(byte data) {
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES){
            System.out.println("DEBUG: setOutputPinState(data=" + data + ")");
            System.out.println("DATA1:" + ((data >>> DATA1) & 1)); //DATA1
            System.out.println("DATA2:" + ((data >>> DATA2) & 1)); //DATA2
            System.out.println("DATA3:" + ((data >>> DATA3) & 1)); //DATA3
            System.out.println("DATA4:" + ((data >>> DATA4) & 1)); //DATA4
            System.out.println("PARITY:" + ((data >>> PARITY) & 1)); //PARITY
            System.out.println("SYNC:" + ((data >>> SYNC) & 1)); //SYNC
            
            if(DEBUG >= DEBUGMODE_NORASPI) return;
        }
        OUT_GPIO24_DI5.setState(((data >>> DATA1) & 1) == 1); //DATA1
        OUT_GPIO25_DI6.setState(((data >>> DATA2) & 1) == 1); //DATA2
        OUT_GPIO26_DI7.setState(((data >>> DATA3) & 1) == 1); //DATA3
        OUT_GPIO27_DI8.setState(((data >>> DATA4) & 1) == 1); //DATA4
        OUT_GPIO28_DI9.setState(((data >>> PARITY) & 1) == 1); //PARITY
        OUT_GPIO29_DI10.setState(((data >>> SYNC) & 1) == 1); //SYNC
    }
    
    public int getInputPinState() {
        if(DEBUG >= DEBUGMODE_NORASPI)
            return 0;
        
        int pins = 0;
        pins |= ((IN_GPIO12_DO3.getState() == PinState.LOW)?1:0) << 5;
        pins |= ((IN_GPIO3_DO4.getState() == PinState.LOW)?1:0) << 4;
        pins |= ((IN_GPIO2_DO5.getState() == PinState.LOW)?1:0) << 3;
        pins |= ((IN_GPIO0_DO6.getState() == PinState.LOW)?1:0) << 2;
        pins |= ((IN_GPIO22_DO7.getState() == PinState.LOW)?1:0) << 1;
        pins |= ((IN_GPIO23_DO8.getState() == PinState.LOW)?1:0);
        return pins;
    }
    
    /**
     * sends one byte of data (unsigned)
     * @param udata data to send represented as unsigned byte
     */
    public void sendByte(byte udata){ 
        int data = ubyteToInt(udata);
        
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES)
            System.out.println("DEBUG: sendByte(data="+data+")");
        
        if(DEBUG <= DEBUGMODE_SHOWMESSAGES)
            while(IN_GPIO3_DO4.isHigh()) //ABB READY
                try{ Thread.sleep(100); }catch(InterruptedException e){}
        
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES)
            System.out.println("ABB listening...");
        
        this.setOutputPinState(genParity((byte) (data >>> 4))); //SYNC low
        
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES)
            System.out.println("First nibble sent...");
        
        if(DEBUG <= DEBUGMODE_SHOWMESSAGES)
            while(IN_GPIO3_DO4.isLow()) //ABB READY
                try{ Thread.sleep(100); }catch(InterruptedException e){}
        
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES)
            System.out.println("ABB listening... (again)");
        
        this.setOutputPinState(genParity((byte)(data & 0b1111 | (1 << SYNC)))); //SYNC high
        
        if(DEBUG >= DEBUGMODE_SHOWMESSAGES)
            System.out.println("Second nibble sent...");
    }
    
    private static int ubyteToInt(byte b){
        return b & 0xFF;
    }
    
    private byte genParity(byte data){
        return (byte) (data | (((
         ((data >>> DATA1) & 1) +
         ((data >>> DATA2) & 1) +
         ((data >>> DATA3) & 1) +
         ((data >>> DATA4) & 1) + 1) & 1) << 4));
    }
}
