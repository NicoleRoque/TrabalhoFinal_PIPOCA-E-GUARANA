package br.pucrs;
import java.util.ArrayList;
public interface Persistencia {

    
    public void adicionarFilmes(Filme novoFilme);

    public boolean removerFilmes(Filme filmeRemovido);

    public ArrayList<Filme> listarTodosFilmes();

    public Filme buscarTitulo(String tituloFilme);

    public int quantidadeFilmesCadastrados();

    public void limparLista();

}
