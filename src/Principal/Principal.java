package Principal;


import Conversor.Conversor;
import Conversor.Moneda;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Principal {
    static Scanner teclado = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("#,##0.00");

    public static void main(String[] args) {
        Conversor conversor = new Conversor();

        int opcion = -1;
        while (opcion != 0) {
            imprimirMenu();
            System.out.print("Elige una opción: ");
            String linea = teclado.nextLine().trim();
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("⚠ Ingresa un número válido.\n");
                continue;
            }

            if (opcion == 0) {
                System.out.println("¡Chao! 👋");
                break;
            }

            Moneda from = null, to = null;
            switch (opcion) {
                case 1 -> { from = Moneda.USD; to = Moneda.ARS; }
                case 2 -> { from = Moneda.ARS; to = Moneda.USD; }
                case 3 -> { from = Moneda.CLP; to = Moneda.USD; }
                case 4 -> { from = Moneda.USD; to = Moneda.CLP; }
                case 5 -> { from = Moneda.CLP; to = Moneda.USD; }
                case 6 -> { from = Moneda.USD; to = Moneda.CLP; }
                default -> {
                    System.out.println("⚠ Opción inválida.\n");
                    continue;
                }
            }

            System.out.print("Ingresa el monto a convertir: ");
            String montoStr = teclado.nextLine().trim();
            double monto;
            try {
                monto = Double.parseDouble(montoStr.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("⚠ Ingresa un monto numérico válido.\n");
                continue;
            }

            try {
                double resultado = conversor.convertir(from, to, monto);
                System.out.printf("Resultado: %s %s ≈ %s %s%n%n",
                        df.format(monto), from, df.format(resultado), to);
            } catch (RuntimeException e) {
                System.out.println("❌ No se pudo realizar la conversión: " + e.getMessage() + "\n");
            }
        }
    }

    private static void imprimirMenu() {
        System.out.println("""
                ========================================
                 Conversor de Monedas — Alura Challenge
                ========================================
                1) USD  -> ARS
                2) ARS  -> USD
                3) BRL  -> USD
                4) USD  -> BRL
                5) CLP  -> USD
                6) USD  -> CLP
                0) Salir
                ----------------------------------------""");
    }
}
