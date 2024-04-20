import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TabuSearchKnapsack {

    public static void main(String[] args) {

        File filePath = new File("src\\KNAPDATA10000.txt");

        // Lista para armazenar os itens
        Item[] items = null;

        // Variável para armazenar o número máximo de iterações
        int maxIterations = 0;
        int capacity = 0;

        // Leitura do arquivo
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Lendo o tamanho da população
            capacity  = Integer.parseInt(br.readLine());

            // Lendo a capacidade da mochila
            maxIterations = Integer.parseInt(br.readLine());
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
                int weight  = scanner.nextInt();
                int value = scanner.nextInt();
                items[i] = new Item(weight, value);
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parâmetros da Busca Tabu
        int tabuSize = 5;
        maxIterations = 1000;
        System.out.println(capacity);
        assert items != null;
        int[] solution = tabu_search_knapsack(items, capacity, tabuSize, maxIterations);
        int peso_total = 0;
        int num_itens_1 = 0;
        for (int i = 0; i < items.length; i++) {
            if (solution[i] == 1) {
                System.out.println("Item " + (i + 1) + ": Peso = " + items[i].weight + ", Valor = " + items[i].value);
            }
            if (solution[i] == 1) {
                peso_total += items[i].weight;
            }
            if (solution[i] == 1) {
                num_itens_1 += 1;
            }

        }
        System.out.println("Peso: " + peso_total);
        System.out.println("Num itens: " + num_itens_1);
    }

    // Função para implementar a Busca Tabu para o Problema da Mochila
    static int[] tabu_search_knapsack(Item[] items, int capacity, int tabuSize, int maxIterations) {
        int[] currentSolution = KnapsackHelper.generate_guided_random_solution(items, capacity);
        int[] bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
        int currentCost = KnapsackHelper.cost_function(currentSolution, items, capacity);
        while (currentCost == 0) {
            currentSolution = KnapsackHelper.generate_guided_random_solution(items, capacity);
            currentCost = KnapsackHelper.cost_function(currentSolution, items, capacity);
            System.out.println(currentCost);
        }
        System.out.println("Primeiro custo: " + currentCost);

        int bestCost = currentCost;
        List<int[]> tabuList = new LinkedList<>();

        for (int iter = 0; iter < maxIterations; iter++) {
            System.out.println(iter);
            List<int[]> neighbors = KnapsackHelper.generate_neighbors(currentSolution, items, capacity);
            int[] nextSolution = null;
            int nextCost = 0;

            for (int[] neighbor : neighbors) {
                if (!tabuList.contains(neighbor)) {
                    int neighborCost = KnapsackHelper.cost_function(neighbor, items, capacity);
                    if (neighborCost > nextCost) {
                        nextSolution = neighbor;
                        nextCost = neighborCost;
                    } else {
                        tabuList.add(neighbor);
                    }
                }
            }

            if (nextSolution == null) {
                break;
            }
            if(nextCost>currentCost){
                currentSolution = nextSolution;
                currentCost = nextCost;
            }

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

}


