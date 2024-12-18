public class Tarefa {
    private int id;
    private int tempoChegada;
    private int tempoExecucao;
    private int tempoInicio;
    private int tempoConclusao;
    private int tempoEspera;
    private int tempoTurnaround;
    private int interrupcao;

    public Tarefa(int id, int tempoChegada, int tempoExecucao) {
        this.id = id;
        this.tempoChegada = tempoChegada;
        this.tempoExecucao = tempoExecucao;
    }

    public void calcularEsperaETurnaround() {
        tempoEspera = tempoInicio - tempoChegada;
        tempoTurnaround = tempoConclusao - tempoChegada;
    }

    // Getters e Setters
    public int getId() { return id; }
    public int getTempoChegada() { return tempoChegada; }
    public int getTempoExecucao() { return tempoExecucao; }
    public int getTempoInicio() { return tempoInicio; }
    public int getTempoConclusao() { return tempoConclusao; }
    public int getTempoEspera() { return tempoEspera; }
    public int getTempoTurnaround() { return tempoTurnaround; }
    public int getInterrupcao() { return interrupcao; }

    public void setTempoInicio(int tempoInicio) { this.tempoInicio = tempoInicio; }
    public void setTempoConclusao(int tempoConclusao) { this.tempoConclusao = tempoConclusao; }
    public void setInterrupcao(int interrupcao) { this.interrupcao = interrupcao; }
}

