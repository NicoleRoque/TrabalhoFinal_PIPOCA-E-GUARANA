package br.pucrs;
import java.util.ArrayList;

public class RepositorioFilmes{

    private ArrayList<Filme> listaFilmes = new ArrayList<>();

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


}
