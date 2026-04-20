package com.hls.configuration;


import static com.hls.configuration.UserSignal.SIG_CAR_USOA_MOVE_12;
import static com.hls.configuration.UserSignal.SIG_CAR_USOA_MOVE_3;
import static com.hls.configuration.UserSignal.SIG_CAR_USOA_MOVE_6;
import static com.hls.configuration.UserSignal.SIG_CAR_USOA_MOVE_9;
import static com.hls.configuration.UserSignal.SIG_CAR_USOA_REPORT_POSITION;
import static com.hls.configuration.UserSignal.SIG_CAR_USOA_STOP;

public class BConfig {
    private final static String end="\n";
    private final static String a="sig ";
    private final static String b="at sig ";

    public final static String FORWARD=b+SIG_CAR_USOA_MOVE_12.getValue()+end;
    public final static String BACK=b+SIG_CAR_USOA_MOVE_6.getValue()+end;
    public final static String LEFT=b+SIG_CAR_USOA_MOVE_9.getValue()+end;
    public final static String RIGHT=b+SIG_CAR_USOA_MOVE_3.getValue()+end;
    public final static String LEFT_FORWARD="q"+end;
    public final static String RIGHT_FORWARD="e"+end;
    public final static String LEFT_BACK="z"+end;
    public final static String RIGHT_BACK="c"+end;
    public final static String STOP=b+SIG_CAR_USOA_STOP.getValue()+end;
    public final static String LEFT_TURN="r"+end;
    public final static String RIGHT_TURN="t"+end;
    public final static String distance=b+SIG_CAR_USOA_REPORT_POSITION.getValue()+end;

    public static String getRate(int val){
        if(val>100 || val<70){
            return null;
        }
        return "setpwm "+val+end;
    }


}
