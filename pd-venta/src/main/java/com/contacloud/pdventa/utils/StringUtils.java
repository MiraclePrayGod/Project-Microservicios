package com.contacloud.pdventa.utils;

public class StringUtils {

    public static String VerificarEstado(String estado){
        if(!estado.equals("PAGADO")){
            throw new IllegalArgumentException("Su venta no esta completada ");
        }
        return estado;
    }
}
