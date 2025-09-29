package br.inatel.cdg.boletim;

import java.math.BigDecimal;
import java.math.RoundingMode;

// politica de arredondamento padrao do sistema
public class PoliticaArredondamento {

    // arredonda para uma casa decimal usando half up
    public double paraUmaCasa(double valor) {
        return new BigDecimal(valor).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    // utilitario generico caso precise no futuro
    public double paraCasas(double valor, int casas) {
        return new BigDecimal(valor).setScale(casas, RoundingMode.HALF_UP).doubleValue();
    }
}
