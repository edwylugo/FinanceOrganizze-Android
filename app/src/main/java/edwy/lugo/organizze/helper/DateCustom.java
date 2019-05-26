package edwy.lugo.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual() {

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

    public static  String mesAnoDataEscolhida(String data) {
        String retornodata[] = data.split("/");
        String dia = retornodata[0];//dia
        String mes = retornodata[1];//mes
        String ano = retornodata[2];//anos

        String mesAno = mes + ano;

        return mesAno;
    }

}
