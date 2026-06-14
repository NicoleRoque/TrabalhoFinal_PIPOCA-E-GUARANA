public interface Persistencia {
    //interface feita apenas para declara os dois metodos que serão implementados 
    
    public Filme[] salvarFilmes();

    public void carregar(Filme[] listaFilmes);
}
