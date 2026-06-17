
package br.pucrs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

        if (filmeDaLista.getTitulo().equalsIgnoreCase(tituloFilme)) {
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

 // FILTROS (streams + lambda)
 // Streams é uma função curta e sem nome. No Java, é representada pelo operador de seta ->. Exemplo: (x) -> x * 2. 
 // Lambdas são funções anônimas (sem nome), passadas como argumentos para essas operações. É um bloco de código super curto que faz uma tarefa específica sem precisar de um nome.

    // Filtra filmes por gênero (ignora maiúsculas/minúsculas e espaços extras)
    public List<Filme> filtrarPorGenero(String genero) {
        return listaFilmes.stream()
                .filter(f -> f.getGenero().trim().equalsIgnoreCase(genero.trim()))
                .collect(Collectors.toList());
    }

    // Filtra filmes lançados a partir de um determinado ano (inclusive)
    public List<Filme> filtrarPorAnoAPartirDe(int ano) {
        return listaFilmes.stream()
                .filter(f -> f.getAnoLancamento() >= ano)
                .collect(Collectors.toList());
    }

    // Filtra filmes com nota acima da média geral de todos os filmes cadastrados
    public List<Filme> filtrarNotaAcimaDaMedia() {
        double media = calcularMediaNotaGeral();
        return listaFilmes.stream()
                .filter(f -> f.getNota() > media)
                .collect(Collectors.toList());
    }

    // Filtro combinado: gênero (opcional), ano mínimo (opcional) e nota mínima (opcional)
    // Passe null/valor-sentinela para ignorar um critério específico.
    public List<Filme> filtrar(String genero, Integer anoMinimo, Double notaMinima) {
        return listaFilmes.stream()
                .filter(f -> genero == null || genero.isBlank()
                        || f.getGenero().trim().equalsIgnoreCase(genero.trim()))
                .filter(f -> anoMinimo == null || f.getAnoLancamento() >= anoMinimo)
                .filter(f -> notaMinima == null || f.getNota() >= notaMinima)
                .collect(Collectors.toList());
    }

    // ESTATÍSTICAS (streams + collectors)
    // Stream é um canal que transporta e processa dados sequencialmente sem modificar a coleção original.
    // Collectors são operações finais que agrupam, somam ou convertem os dados desse fluxo em um resultado útil (como listas ou estatísticas).

    // Média geral de notas de todos os filmes cadastrados
    public double calcularMediaNotaGeral() {
        return listaFilmes.stream()
                .mapToDouble(Filme::getNota)
                .average()
                .orElse(0.0);
    }

    // Média de duração de todos os filmes cadastrados
    public double calcularDuracaoMedia() {
        return listaFilmes.stream()
                .mapToInt(Filme::getDuracao)
                .average()
                .orElse(0.0);
    }

    // Média de notas agrupada por gênero (ex: {"Terror"=3.6, "Romance"=4.4, ...})
    public Map<String, Double> calcularMediaNotaPorGenero() {
        return listaFilmes.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getGenero().trim(),
                        Collectors.averagingDouble(Filme::getNota)
                ));
    }

    // Contagem de filmes por gênero (ex: {"Terror"=1, "Romance"=1, ...})
    public Map<String, Long> contarFilmesPorGenero() {
        return listaFilmes.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getGenero().trim(),
                        Collectors.counting()
                ));
    }

    // Duração média por gênero
    public Map<String, Double> calcularDuracaoMediaPorGenero() {
        return listaFilmes.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getGenero().trim(),
                        Collectors.averagingInt(Filme::getDuracao)
                ));
    }

    // Filme com a maior nota cadastrada
    public Filme filmeComMaiorNota() {
        return listaFilmes.stream()
                .max(Comparator.comparingDouble(Filme::getNota))
                .orElse(null);
    }

    // Filme com a menor nota cadastrada
    public Filme filmeComMenorNota() {
        return listaFilmes.stream()
                .min(Comparator.comparingDouble(Filme::getNota))
                .orElse(null);
    }
}
