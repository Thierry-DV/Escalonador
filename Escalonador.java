import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Escalonador {
    private List<Tarefa> tarefas;
    private Random random;
    private long tempoTotalExecucao; // Tempo total para escalonar e processar
    private long memoriaUsada;       // Memória utilizada pelo processo
    private int tempoProcessamentoAtivo; // Tempo total de execução das tarefas
    private int tempoTotalSistema; // Tempo entre chegada da primeira e conclusão da última tarefa

    public Escalonador() {
        this.tarefas = new ArrayList<>();
        this.random = new Random();
        this.tempoTotalExecucao = 0;
        this.memoriaUsada = 0;
        this.tempoProcessamentoAtivo = 0;
        this.tempoTotalSistema = 0;
    }

    public void gerarTarefasAleatorias(int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            int tempoChegada = random.nextInt(100);
            int tempoExecucao = 1 + random.nextInt(60);
            Tarefa tarefa = new Tarefa(i, tempoChegada, tempoExecucao);
            tarefas.add(tarefa);
        }
    }

    public void escalonar() {
        // Medindo o tempo inicial
        long inicio = System.nanoTime();

        // Ordena tarefas por tempo de execução (SJF)
        tarefas = tarefas.stream()
                .sorted((t1, t2) -> Integer.compare(t1.getTempoExecucao(), t2.getTempoExecucao()))
                .collect(Collectors.toList());

        int tempoAtual = 0;
        int tempoPrimeiraChegada = tarefas.get(0).getTempoChegada();

        for (Tarefa tarefa : tarefas) {
            if (tempoAtual < tarefa.getTempoChegada()) {
                tempoAtual = tarefa.getTempoChegada();
            }

            tarefa.setTempoInicio(tempoAtual);
            int interrupcao = random.nextInt(5);
            tarefa.setInterrupcao(interrupcao);

            tempoAtual += tarefa.getTempoExecucao();
            tarefa.setTempoConclusao(tempoAtual + interrupcao);
            tarefa.calcularEsperaETurnaround();

            tempoProcessamentoAtivo += tarefa.getTempoExecucao();
        }

        // Define o tempo total do sistema
        tempoTotalSistema = tarefas.get(tarefas.size() - 1).getTempoConclusao() - tempoPrimeiraChegada;

        // Medindo o tempo final
        long fim = System.nanoTime();
        tempoTotalExecucao = fim - inicio;

        // Calculando memória usada
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Força garbage collection para medir memória usada com precisão
        memoriaUsada = runtime.totalMemory() - runtime.freeMemory();
    }

    public void exibirMetricas() {
        System.out.println("\nMétricas de Avaliação do Sistema:");
        System.out.printf("Memória utilizada: %.2f MB%n", memoriaUsada / (1024.0 * 1024.0));
        System.out.printf("Uso da CPU: %.2f%%%n", calcularUsoCPU());
        System.out.printf("Média do tempo de espera: %.2f ms%n", calcularMediaTempoEspera());
        System.out.printf("Média do tempo de turnaround: %.2f ms%n", calcularMediaTempoTurnaround());
    }

    public void salvarMetricasCSV(String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Cabeçalho para métricas gerais
            writer.append("Memória Usada (MB),Uso da CPU (%),Tempo Médio de Espera (ms),Tempo Médio de Turnaround (ms)\n");
            
            // Escrevendo métricas gerais com formatação apropriada
            writer.append(String.format("%.2f,%.2f,%.2f,%.2f\n",
                    memoriaUsada / (1024.0 * 1024.0), // Convertendo para MB
                    calcularUsoCPU(),
                    calcularMediaTempoEspera(),
                    calcularMediaTempoTurnaround()));
            
            // Cabeçalho para dados de cada processo
            writer.append("\nID,Tempo de Chegada,Tempo de Execução,Tempo de Início,Tempo de Conclusão,Tempo de Espera,Turnaround,Interrupção\n");
    
            // Dados de cada processo
            for (Tarefa tarefa : tarefas) {
                writer.append(String.format("%d,%d,%d,%d,%d,%d,%d,%d\n",
                        tarefa.getId(),
                        tarefa.getTempoChegada(),
                        tarefa.getTempoExecucao(),
                        tarefa.getTempoInicio(),
                        tarefa.getTempoConclusao(),
                        tarefa.getTempoEspera(),
                        tarefa.getTempoTurnaround(),
                        tarefa.getInterrupcao()));
            }
    
            System.out.println("Métricas e dados de processos salvos no arquivo CSV: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar métricas no CSV: " + e.getMessage());
        }
    }
    

    

    // Funções existentes para cálculo de métricas relacionadas a tarefas
    public double calcularMediaTempoEspera() {
        return tarefas.stream()
                .mapToInt(Tarefa::getTempoEspera)
                .average()
                .orElse(0.0);
    }

    public double calcularMediaTempoTurnaround() {
        return tarefas.stream()
                .mapToInt(Tarefa::getTempoTurnaround)
                .average()
                .orElse(0.0);
    }

    public double calcularUsoCPU() {
        if (tempoTotalSistema == 0) return 0.0;
        return (tempoProcessamentoAtivo / (double) tempoTotalSistema) * 100;
    }
}

