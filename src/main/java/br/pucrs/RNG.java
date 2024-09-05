package br.pucrs;

public class RNG {
    private double seed;
    private double a;
    private double c;
    private double m;

    public RNG(double a, double c, double m) {
        if (m <= 0) {
            throw new IllegalArgumentException("Módulo tem que ser positivo");
        }
        if (a <= 0 || a >= m) {
            throw new IllegalArgumentException("Multiplicador tem que ser positivo e menor que o módulo");
        }
        if (c < 0 || c >= m) {
            throw new IllegalArgumentException("Incremento tem que ser positivo e menor que o módulo");
        }
        if (seed < 0 || seed >= m) {
            throw new IllegalArgumentException("Semente tem que ser positivo e menor que o módulo");
        }

        this.a = a;
        this.c = c;
        this.m = m;
        this.seed = 65419518;


    }

    public double nextRandom() {
        seed = (a * seed + c) % m;
        Main.quantidade--;
        return seed / m ;
    }

    public int nextRandonBetween(int min, int max) {
        Main.quantidade--;
        return (int) (min + (max - min) * nextRandom());
    }
}
