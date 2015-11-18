package com.example.yarvis.programaventas;

/**
 * Created by noa on 11/11/15.
 */
public class StaticVariables {
    public static String ip_server = "192.168.1.109";
    public static String TAG_SUCCESS = "success";
    public static String TAG_PRODUCTS = "productos";
    public static String TAG_PID = "id";
    public static String TAG_NAME = "nombre";
    public static String TAG_PRICE = "precio";
    public static String TAG_QUANT = "cant";

    StaticVariables(){
    }

    public static String getIpServer(){ return ip_server;}

    public static String getTagSuccess(){ return TAG_SUCCESS;}

    public static String getTagProducts(){ return TAG_PRODUCTS;}

    public static String getTagPid(){ return TAG_PID;}

    public static String getTagName(){ return TAG_NAME;}

    public static String getTagPrice(){ return TAG_PRICE;}

    public static String getTagQuant(){ return TAG_QUANT;}

}
