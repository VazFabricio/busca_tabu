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
            //System.out.println(totalWeight +"/"+totalValue);
            return 0;
        }
        //System.out.println(totalWeight +"/"+totalValue);
        return totalValue;
    }
    static List<int[]> generate_neighbors(int[] solution, Item[] items, int capacity) {
        List<int[]> neighbors = new ArrayList<>();
        int n = solution.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int[] neighbor = Arrays.copyOf(solution, n);
                neighbor[i] = (neighbor[i] == 0) ? 1 : 0; // Inverte o item i
                neighbor[j] = (neighbor[j] == 0) ? 1 : 0; // Inverte o item j
                if (isValidNeighbor(neighbor, items, capacity)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    // Função auxiliar para verificar se um vizinho é válido
    static boolean isValidNeighbor(int[] neighbor, Item[] items, int capacity) {
        int totalWeight = 0;
        for (int i = 0; i < neighbor.length; i++) {
            if (neighbor[i] == 1) {
                totalWeight += items[i].weight;
            }
        }
        return totalWeight <= capacity;
    }
}