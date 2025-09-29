package br.inatel.cdg.boletim;

// regra simples para calcular a media apos recuperacao
// usamos mediaFinal como a media aritmetica entre a media parcial e a nota de recuperacao
// se nao houver nota de recuperacao, retorna a propria media parcial
public class PoliticaRecuperacao {

    // calcula a media final apos aplicar recuperacao
    public double aplicar(double mediaParcial, Double notaRecuperacao) {
        if (notaRecuperacao == null) {
            return mediaParcial; // sem recuperacao, mantem media
        }
        // media simples entre a media parcial e a nota de recuperacao
        return (mediaParcial + notaRecuperacao) / 2.0;
    }
}
