package com.hls.configuration;


public class PackageConfig {
    private final static String end="\n";
    private final static String a="sig ";
    private final static String b="at sig ";

    public final static String FORWARD="w"+end;
    public final static String BACK="x"+end;
    public final static String LEFT="a"+end;
    public final static String RIGHT="d"+end;
    public final static String LEFT_FORWARD="q"+end;
    public final static String RIGHT_FORWARD="e"+end;
    public final static String LEFT_BACK="z"+end;
    public final static String RIGHT_BACK="c"+end;
    public final static String STOP="stop"+end;
    public final static String LEFT_TURN="r"+end;
    public final static String RIGHT_TURN="t"+end;

    public static String getRate(int val){
        if(val>100 || val<70){
            return null;
        }
        return "setpwm "+val+end;
    }


}
