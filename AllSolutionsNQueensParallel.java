import java.util.ArrayList;
import java.util.List;

public class AllSolutionsNQueensParallel {

    // Tamanho do tabuleiro (n x n).
    private int n;

    // Contador para o número total de soluções encontradas.
    private int solutionCount = 0;

    // Lista para armazenar todas as soluções encontradas.
    private List<int[]> solutions;

    // Objeto utilizado para sincronizar o acesso às variáveis compartilhadas entre as threads.
    private final Object lock = new Object();

    // Construtor da classe que inicializa o tamanho do tabuleiro e a lista de soluções.
    public AllSolutionsNQueensParallel(int n) {
        this.n = n;
        solutions = new ArrayList<>();
    }

    // Método principal que inicia a resolução do problema das N rainhas de forma paralela.
    public void solve() {
        long startTime = System.nanoTime(); // Inicia a contagem do tempo de execução.
        List<Thread> threads = new ArrayList<>(); // Lista para armazenar as threads criadas.

        // Cria uma thread para cada possível posição da rainha na primeira linha.
        for (int col = 0; col < n; col++) {
            int[] initialBoard = new int[n]; // Cria um tabuleiro inicial.
            initialBoard[0] = col; // Posiciona a primeira rainha na coluna `col`.

            // Cria uma nova tarefa de NQueens para resolver a partir da segunda linha.
            Thread thread = new Thread(new NQueenTask(initialBoard, 1));
            threads.add(thread); // Adiciona a thread à lista.
            thread.start(); // Inicia a execução da thread.
        }

        // Aguarda a conclusão de todas as threads.
        for (Thread thread : threads) {
            try {
                thread.join(); // Espera a thread terminar.
            } catch (InterruptedException e) {
                e.printStackTrace(); // Trata a exceção caso a thread seja interrompida.
            }
        }

        long endTime = System.nanoTime(); // Termina a contagem do tempo de execução.
        long duration = endTime - startTime; // Calcula a duração total da execução.

        // printAllSolutions();

        printTime(duration); // Imprime o tempo total de execução.
        System.out.println("Número total de soluções: " + solutionCount); // Imprime o número total de soluções encontradas.
    }

    // Classe interna que implementa a lógica de resolução do problema para uma parte do tabuleiro.
    private class NQueenTask implements Runnable {
        private int[] board; // Tabuleiro atual da tarefa.
        private int row; // Linha a partir da qual a tarefa irá continuar a resolução.

        // Construtor da tarefa que recebe o tabuleiro e a linha atual.
        public NQueenTask(int[] board, int row) {
            this.board = board;
            this.row = row;
        }

        @Override
        public void run() {
            solve(row); // Inicia a resolução do problema a partir da linha especificada.
        }

        // Método recursivo que tenta posicionar as rainhas no tabuleiro.
        private void solve(int row) {
            // Se todas as rainhas foram posicionadas corretamente (ou seja, atingimos a última linha), contamos uma solução válida.
            if (row == n) {
                synchronized (lock) { // Sincroniza o acesso ao contador de soluções e à lista de soluções.
                    solutionCount++; // Incrementa o contador de soluções.
                    solutions.add(board.clone()); // Armazena a solução encontrada (clone do tabuleiro atual).
                }
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
    }

    // Método que imprime todas as soluções encontradas (descomente o método na função solve() para utilizá-lo).
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
        AllSolutionsNQueensParallel nQueens = new AllSolutionsNQueensParallel(n);
        nQueens.solve(); // Inicia a resolução do problema.
    }
}
