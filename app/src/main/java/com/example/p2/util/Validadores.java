package com.example.p2.util;

import java.util.regex.Pattern;

public final class Validadores {
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");

    private Validadores() {
    }

    public static boolean nomeValido(String nome) {
        return nome != null && !nome.trim().isEmpty() && nome.trim().length() <= 100;
    }

    public static boolean emailValido(String email) {
        return email != null && email.trim().length() <= 254
                && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean notaValida(int nota) {
        return nota >= 1 && nota <= 5;
    }
}
