import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TabuSearchKnapsack {
    // Defina a estrutura de um item da mochila
    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    public static void main(String[] args) {

        ////
        File filePath = new File("src\\KNAPDATA40.txt");

        // Lista para armazenar os itens
        Item[] items = null;

        // Variável para armazenar o número máximo de iterações
        int maxIterations = 0;
        int capacity = 0;

        // Leitura do arquivo
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Lendo o tamanho da população
            maxIterations = Integer.parseInt(br.readLine());

            // Lendo a capacidade da mochila
            capacity = Integer.parseInt(br.readLine());

            int numItems = 0;
            while (br.readLine() != null) {
                numItems++;
            }

            br.close();

            BufferedReader brItems = new BufferedReader(new FileReader(filePath));
            // Ignorando as duas primeiras linhas
            brItems.readLine();
            brItems.readLine();

            items = new Item[numItems];
            for (int i = 0; i < numItems; i++) {
                String line = brItems.readLine();
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                scanner.next(); // Ignora o nome do item
                int weight = scanner.nextInt();
                int value = scanner.nextInt();
                items[i] = new Item(weight, value);
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Parâmetros da Busca Tabu
        int tabuSize = 5;

//        for(int j = 0; j < maxIterations; j++) {
//            int pesos_das_solucoes = 0;
//            for (int i = 0; i < items.length; i++) {
//
//                int[] solution = tabu_search_knapsack(items, capacity, tabuSize, maxIterations);
//
//
//                if (solution[i] == 1) {
//                    pesos_das_solucoes += items[i].weight;
//                }
//
//                if(pesos_das_solucoes>capacity){
//                    System.out.println(pesos_das_solucoes);
//
//                    System.out.println("Item " + (i+1) + ": Peso = " + items[i].weight + ", Valor = " + items[i].value);
//                }
//
//            }
//
//        }

        int[] solution = tabu_search_knapsack(items, capacity, tabuSize, maxIterations);
        int peso_total = 0;
        int num_itens_1 =0;
        for (int i = 0; i < items.length; i++) {
            if (solution[i] == 1) {
                System.out.println("Item " + (i+1) + ": Peso = " + items[i].weight + ", Valor = " + items[i].value);
            }
            if (solution[i] == 1) {
                peso_total += items[i].weight;
            }
            if (solution[i] == 1) {
                num_itens_1 += 1;
            }

        }
        System.out.println(peso_total);
        System.out.println(num_itens_1);
        System.out.println(peso_total/num_itens_1);
    }

    // Função para implementar a Busca Tabu para o Problema da Mochila
    static int[] tabu_search_knapsack(Item[] items, int capacity, int tabuSize, int maxIterations) {
        int[] currentSolution = generate_random_solution(items.length);
        int[] bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
        int currentCost = cost_function(currentSolution, items, capacity);
        int bestCost = currentCost;
        List<int[]> tabuList = new LinkedList<>();

        for (int iter = 0; iter < maxIterations; iter++) {
            List<int[]> neighbors = generate_neighbors(currentSolution);
            int[] nextSolution = null;
            int nextCost = 0;

            for (int[] neighbor : neighbors) {
                if (!tabuList.contains(neighbor)) {
                    int neighborCost = cost_function(neighbor, items, capacity);
                    if (neighborCost > nextCost) {
                        nextSolution = neighbor;
                        nextCost = neighborCost;
                    }
                }
            }

            if (nextSolution == null) {
                break;
            }

            currentSolution = nextSolution;
            currentCost = nextCost;

            if (currentCost > bestCost) {
                bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
                bestCost = currentCost;
            }

            tabuList.add(nextSolution);
            if (tabuList.size() > tabuSize) {
                tabuList.removeFirst();
            }
        }

        return bestSolution;
    }

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
