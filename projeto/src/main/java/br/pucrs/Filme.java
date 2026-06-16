package br.pucrs;

public class Filme extends Midia {
    private int ID; //VARIAVEL UTILIZADA PARA IDENTIFICAR CADA FILME QUE SERÁ CADASTRADO COM UM CODIGO UNICO DELE
    private String genero;
    private int duracao;
    private double nota;
    
    //VARIAVEL ESTATICA, PERTENCE APENAS A CLASSE FILME(controla qual será o proximo id disponivel)
    private static int proximoID = 1;

    public Filme(String genero, int duracao, double nota, String titulo, int anoLancamento){
        super(titulo, anoLancamento);
        this.genero = genero;
        this.duracao = duracao;
        this.nota = nota;
        this.ID = proximoID++;
    }

    public int getID(){
        return ID;//não tem set pois o id não pode ser alterado apenas acessado
    }
    public String getGenero(){
        return genero;
    }
    public void setGenero(String genero){
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
        System.out.println(" #### INFORMAÇÕES SOBRE O FILME ####");
        System.out.println("Titulo " + getTitulo()  + "/nGenero " + getGenero() + "/nDuração " + getDuracao() 
        + "/nAno de lançamento " + getAnoLancamento() );
    }
    
    @Override
    public String toString(){
        return super.toString() + "Genero: " + genero + " duração " + duracao + " nota " + nota;
    }
}
