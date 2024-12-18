public class Main {
    public static void main(String[] args) {
        int quantidadeTarefas = 100000;

        Escalonador escalonador = new Escalonador();

        // Geração de tarefas
        escalonador.gerarTarefasAleatorias(quantidadeTarefas);

        // Escalonamento
        escalonador.escalonar();

        // Exibindo resultados
        escalonador.exibirMetricas();

        // Salvando métricas em CSV
        String caminhoArquivo = "resultados5.csv";
        escalonador.salvarMetricasCSV(caminhoArquivo);
    }
}
