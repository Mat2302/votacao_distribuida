package client;

public class CPFValidator {

    /**
     * Valida um CPF (aceita com ou sem pontos/traço).
     * @param cpfStr CPF como String (ex: "39423920393" ou "394.239.203-93")
     * @return true se o CPF for válido, false caso contrário
     */
    public static boolean validate(String cpfStr) {
        if (cpfStr == null) return false;

        // remove tudo que não for dígito
        String cpf = cpfStr.replaceAll("\\D", "");

        // deve ter 11 dígitos
        if (cpf.length() != 11) return false;

        // rejeita sequências como 00000000000, 11111111111, ...
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }

            // calcula primeiro dígito verificador (peso 10..2)
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int rem = sum % 11;
            int d1 = (rem < 2) ? 0 : 11 - rem;
            if (d1 != digits[9]) return false;

            // calcula segundo dígito verificador (peso 11..2)
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            rem = sum % 11;
            int d2 = (rem < 2) ? 0 : 11 - rem;
            return d2 == digits[10];

        } catch (NumberFormatException e) {
            return false; // não era número
        }
    }
}
