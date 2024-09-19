import br.pucrs.RNG;
import org.junit.Test;

public class testRNG {

    @Test
    public void testRNGBinsAndHistogram() {
        RNG rng = new RNG(123345, 1664525, 1013904223, Math.pow(2, 32)); // Valores exemplo

        int numBins = 10;  // Número de intervalos para a distribuição
        int[] bins = new int[numBins];
        int numSamples = 1000;  // Número de amostras

        // Gerar os números e contar quantos caem em cada intervalo
        for (int i = 0; i < numSamples; i++) {
            double randomValue = rng.nextRandom();
            int binIndex = (int) (randomValue * numBins);  // Calcula o índice do intervalo (0 a 9)
            if (binIndex == numBins) {
                binIndex = numBins - 1;  // Garante que o valor máximo entre no último intervalo
            }
            bins[binIndex]++;
        }

        // Imprime os resultados
        System.out.println("Distribuição de " + numSamples + " números aleatórios:");
        for (int i = 0; i < numBins; i++) {
            System.out.printf("Intervalo [%d - %d%%]: %d\n", i * 10, (i + 1) * 10, bins[i]);
        }
    }

    @Test
    public void testRNGBetweenXandY_BinsAndHistogram() {
        RNG rng = new RNG(123345, 1664525, 1013904223, Math.pow(2, 32)); // Valores exemplo

        int min = 10;  // Limite inferior
        int max = 50;  // Limite superior
        int numBins = max - min;  // Número de intervalos será igual à diferença entre max e min
        int[] bins = new int[numBins];
        int numSamples = 10000;  // Número de amostras

        // Gerar números e contar quantos caem em cada intervalo
        for (int i = 0; i < numSamples; i++) {
            int randomValue = rng.nextRandonBetween(min, max);
            bins[randomValue - min]++;  // Ajuste para indexar corretamente
        }

        // Imprimir a distribuição dos resultados
        System.out.println("Distribuição de " + numSamples + " números aleatórios no intervalo [" + min + ", " + max + "]:");
        for (int i = 0; i < numBins; i++) {
            System.out.printf("Valor %d: %d\n", i + min, bins[i]);
        }
    }
}
