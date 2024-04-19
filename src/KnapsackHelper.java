import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KnapsackHelper {
    // Função para gerar uma solução inicial aleatória
    static int[] generate_random_solution(int size) {
        int[] solution = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            solution[i] = rand.nextInt(2); // 0 ou 1 (selecionado ou não selecionado)
        }
        return solution;
    }

    // Função de custo para calcular o valor total da mochila, com penalização para excesso de peso
    static int cost_function(int[] solution, Item[] items, int capacity) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalValue += items[i].value;
                totalWeight += items[i].weight;
            }
        }
        // Penalize soluções que excedam a capacidade da mochila
        if (totalWeight > capacity) {
            // Calcule o excesso de peso
            int excessWeight = totalWeight - capacity;
            // Defina a taxa de penalização (você pode ajustar esse valor)
            int penaltyRate = 900; // Ajuste conforme necessário
            // Reduza o valor proporcionalmente ao excesso de peso
            totalValue -= penaltyRate * excessWeight;
            // Se o valor se tornar negativo, defina-o como zero para garantir não negatividade
            if (totalValue < 0) {
                totalValue = 0;
            }
        }
        return totalValue;
    }

    // Função para gerar vizinhos (opções de movimento) para uma solução dada
    static List<int[]> generate_neighbors(int[] solution) {
        List<int[]> neighbors = new ArrayList<>();
        int n = solution.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int[] neighbor = Arrays.copyOf(solution, n);
                neighbor[i] = (neighbor[i] == 0) ? 1 : 0; // Inverte o item i
                neighbor[j] = (neighbor[j] == 0) ? 1 : 0; // Inverte o item j
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
