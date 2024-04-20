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

    // Função de custo para calcular o valor total da mochila
    static int cost_function(int[] solution, Item[] items, int capacity) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                //System.out.println(items[i].value+"/"+items[i].weight);
                totalValue += items[i].value;
                totalWeight += items[i].weight;
            }
        }

        if (totalWeight > capacity) {
            System.out.println(totalWeight +"/"+totalValue);
            return 0;
        }
        return totalValue;
    }

    // Função para gerar vizinhos (opções de movimento) para uma solução dada
    static List<int[]> generate_similar_neighbors(int[] solution, int maxChanges) {
        List<int[]> neighbors = new ArrayList<>();
        int n = solution.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int[] neighbor = Arrays.copyOf(solution, n);
                int changes = 0; // Contador de alterações feitas no vizinho
                if (changes < maxChanges) {
                    neighbor[i] = (neighbor[i] == 0) ? 1 : 0; // Inverte o item i
                    changes++;
                }
                if (changes < maxChanges) {
                    neighbor[j] = (neighbor[j] == 0) ? 1 : 0; // Inverte o item j
                    changes++;
                }
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // Função para gerar todos os vizinhos possíveis
    static List<int[]> generate_neighbors(int[] solution) {
        // Chamamos generate_similar_neighbors com maxChanges = 2 para obter todos os vizinhos possíveis
        return generate_similar_neighbors(solution, 2);
    }
}