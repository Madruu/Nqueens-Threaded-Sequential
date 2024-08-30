import java.util.ArrayList;
import java.util.List;

public class AllSolutionsNQueensSequential {

    // Variável que representa o tabuleiro. Cada índice representa uma linha e o valor armazenado representa a coluna onde a rainha está posicionada.
    private int[] board;

    // Tamanho do tabuleiro (n x n).
    private int n;

    // Contador para o número total de soluções encontradas.
    private int solutionCount = 0;

    // Lista para armazenar todas as soluções encontradas.
    private List<int[]> solutions;

    // Construtor da classe que inicializa o tabuleiro, o tamanho do tabuleiro e a lista de soluções.
    public AllSolutionsNQueensSequential(int n) {
        this.n = n;
        board = new int[n];
        solutions = new ArrayList<>();
    }

    // Método principal que inicia a resolução do problema das N rainhas.
    public void solve() {
        long startTime = System.nanoTime(); // Inicia a contagem do tempo de execução.
        solve(0); // Chama o método recursivo para resolver o problema.
        long endTime = System.nanoTime(); // Termina a contagem do tempo de execução.
        long duration = endTime - startTime; // Calcula a duração total da execução.

        // printAllSolutions(); // Descomente esta linha para imprimir todas as soluções encontradas.

        printTime(duration); // Imprime o tempo total de execução.
        System.out.println("Número total de soluções: " + solutionCount); // Imprime o número total de soluções encontradas.
    }

    // Método recursivo que tenta posicionar as rainhas no tabuleiro.
    private void solve(int row) {
        // Se todas as rainhas foram posicionadas corretamente (ou seja, atingimos a última linha), contamos uma solução válida.
        if (row == n) {
            solutionCount++; // Incrementa o contador de soluções.
            solutions.add(board.clone()); // Armazena a solução encontrada (clone do tabuleiro atual).
            return;
        }

        // Tenta posicionar uma rainha em cada coluna da linha atual.
        for (int col = 0; col < n; col++) {
            // Verifica se é seguro posicionar a rainha na posição atual (linha `row`, coluna `col`).
            if (isSafe(row, col)) {
                board[row] = col; // Posiciona a rainha na coluna `col`.
                solve(row + 1); // Tenta resolver o problema para a próxima linha.
            }
        }
    }

    // Método que verifica se é seguro posicionar uma rainha na posição (row, col).
    private boolean isSafe(int row, int col) {
        // Verifica todas as linhas anteriores para garantir que não há conflito.
        for (int i = 0; i < row; i++) {
            int placedCol = board[i]; // Coluna onde a rainha está posicionada na linha `i`.

            // Verifica se há outra rainha na mesma coluna ou na mesma diagonal.
            if (placedCol == col || Math.abs(placedCol - col) == Math.abs(i - row)) {
                return false; // Não é seguro posicionar a rainha aqui.
            }
        }
        return true; // É seguro posicionar a rainha aqui.
    }

    // Método que imprime todas as soluções encontradas
    private void printAllSolutions() {
        for (int i = 0; i < solutions.size(); i++) {
            int[] solution = solutions.get(i);
            System.out.println("Solução " + (i + 1) + ":");
            for (int row : solution) {
                for (int j = 0; j < n; j++) {
                    if (j == row) {
                        System.out.print("Q "); // Imprime "Q" para a posição da rainha.
                    } else {
                        System.out.print(". "); // Imprime "." para as outras posições.
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    // Método que imprime o tempo total de execução em uma unidade apropriada (nanosegundos, milissegundos ou segundos).
    private void printTime(long duration) {
        if (duration < 1_000_000) {
            System.out.println("Tempo de execução: " + duration + " nanosegundos");
        } else if (duration < 1_000_000_000) {
            System.out.println("Tempo de execução: " + duration / 1_000_000 + " milissegundos");
        } else {
            System.out.println("Tempo de execução: " + duration / 1_000_000_000 + " segundos");
        }
    }

    // Método main que cria uma instância do problema e chama o método solve().
    public static void main(String[] args) {
        int n = 12; // Define o tamanho do tabuleiro.
        AllSolutionsNQueensSequential nQueens = new AllSolutionsNQueensSequential(n);
        nQueens.solve(); // Inicia a resolução do problema.
    }
}
