public class Filme extends Midia {
    private Genero genero;
    private int duracao;
    private double nota;
    
    public Filme(Genero genero, int duracao, double nota, String titulo, int anoLancamento){
        super(titulo, anoLancamento);
        this.genero = genero;
        this.duracao = duracao;
        this.nota = nota;
    }

    public Genero getGenero(){
        return genero;
    }
    public void setGenero(Genero genero){
        this.genero = genero;
    }
    public int getDuracao(){
        return duracao;
    }
    public void setDuracao(int duracao){
        this.duracao = duracao;
    }

    public double getNota(){
        return nota;
    }
    public void setNota(double nota){
        this.nota = nota;
    }

    @Override
    public void exibeInformacoes(){
        //verificar o que vou colocar aqui 
    }
    
    @Override
    public String toString(){
        return super.toString() + "Genero: " + genero + " duração " + duracao + " nota " + nota;
    }
}
