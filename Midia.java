public abstract class Midia{
    private String titulo;
    private int anoLancamento;

    public Midia(String titulo, int anoLancamento){
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
    }

    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public int getAnoLancamento(){
        return anoLancamento;
    }
    public void setAnoLancamento(int anoLancamento){
        this.anoLancamento = anoLancamento;
    }

    //classe abstrata que será implementada em filme
    public abstract void exibeInformacoes();
}