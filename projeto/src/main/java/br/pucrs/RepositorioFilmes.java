
package br.pucrs;

import java.util.ArrayList;
public class RepositorioFilmes implements Persistencia{

    private ArrayList<Filme> listaFilmes = new ArrayList<>();

    //Variavel que pertence a classe, ou seja, não será instaciada e iniciamente seu valor é null
    private static RepositorioFilmes instance;

    //metodo para garantir que exista apenas uma lista de filmes unica, evitando assim que os dados se percam em varias listas 
    public static RepositorioFilmes getInstance() { //metodo estatico, pode ser chamado sem criar um objeto
        if (instance == null)  //se instance for igual a null(o que ela é)
            instance = new RepositorioFilmes(); //Cria o único objeto da classe e o guarda na variavel instance
        return instance; //retorna a varivavel
    }

    public void adicionarFilmes(Filme novoFilme){
        listaFilmes.add(novoFilme);

        
    }

    public boolean removerFilmes(Filme filmeRemovido){
        //se o filme passado por parametro não existir
        if (filmeRemovido.equals(null)) {
            throw new IllegalArgumentException();
        }

        listaFilmes.remove(filmeRemovido);
        return true;
    }

    public ArrayList<Filme> listarTodosFilmes(){
        return this.listaFilmes;
    }

    private RepositorioFilmes() {

        listaFilmes.add( new Filme("Romance ",  2,  4.4, " A culpa é das estrelas ", 2014));
        listaFilmes.add( new Filme("Terror ", 1, 3.6," Annabelle ", 2014) );
        listaFilmes.add( new Filme("Drama ", 1, 4.6, " Sempre ao seu lado ", 2009) );
        listaFilmes.add( new Filme("Ação ", 1, 7.1, " Velozes e furiosos 7 ", 2015));
        
    }

    public Filme buscarTitulo(String tituloFilme){
        //percorre toda lista e verifica se existe o titulo que estou procurando
        for (Filme filmeDaLista : listaFilmes) {

        if (filmeDaLista.getTitulo() == tituloFilme) {
            return filmeDaLista;  //se existir retorna o filme
        }
        
       }

       return null;
    }

    public int quantidadeFilmesCadastrados(){
        return this.listaFilmes.size();
    }

    public void limparLista(){
         this.listaFilmes.clear();
    }

    //metodo responsavel por modificar informações do cadastro do filme
    public void update(int id, Filme upd) {
        Filme f = listaFilmes.stream() //usamos a API streams do java
                        .filter( a -> a.getID() == id) //Cria um fluxo de elementos da listaFilmes
                        .findFirst() //Mantém apenas os filmes cujo ID seja igual ao ID recebido.
                        .orElse(null); //retorna o primeiro filme encontrado
        if ( f != null ) { //se a variavel filme(f) for diferente de null quer dizer que encontramos o filme que queremos modificar
            f.setGenero(upd.getGenero()); //então usamos o modificador set copiando os dados encontrados no upd get
            f.setNota(upd.getNota());//modificamos o set copiando os dados encontrados no upd get
        }
    }
}
