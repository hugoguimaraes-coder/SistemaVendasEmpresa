package br.com.empresa.util;

public class ValidadorDocumento {

    public static boolean isValido(String documento) {
        if (documento == null) return false;

        String limpo = documento.replaceAll("\\D", "");

        if (limpo.equals("00000000000")) {
            return true;
        }

        if (limpo.length() == 11) {
            return validarCPF(limpo);
        } else if (limpo.length() == 14) {
            return validarCNPJ(limpo);
        }
        return false;
    }

    private static boolean validarCPF(String cpf) {

        if (cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int d1 = 0, d2 = 0;
            int digito1, digito2, resto;
            int num;

            for (int i = 1; i <= 9; i++) {
                num = Character.getNumericValue(cpf.charAt(i - 1));
                d1 = d1 + (num * (11 - i));
                d2 = d2 + (num * (12 - i));
            }

            resto = (d1 % 11);
            digito1 = (resto < 2) ? 0 : 11 - resto;
            d2 += 2 * digito1;
            resto = (d2 % 11);
            digito2 = (resto < 2) ? 0 : 11 - resto;

            return (digito1 == Character.getNumericValue(cpf.charAt(9))) &&
                    (digito2 == Character.getNumericValue(cpf.charAt(10)));
        } catch (Exception e) { return false; }
    }

    private static boolean validarCNPJ(String cnpj) {
        // Lógica de cálculo dos dígitos verificadores do CNPJ
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++)
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];

            int d1 = 11 - (soma % 11);
            if (d1 >= 10) d1 = 0;

            soma = 0;
            for (int i = 0; i < 13; i++)
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];

            int d2 = 11 - (soma % 11);
            if (d2 >= 10) d2 = 0;

            return (d1 == Character.getNumericValue(cnpj.charAt(12))) &&
                    (d2 == Character.getNumericValue(cnpj.charAt(13)));
        } catch (Exception e) { return false; }
    }
}