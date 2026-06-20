package br.pucrs;

import com.vaadin.flow.component.UI;// Abre portais: Controla o navegador e serve para mudar de página.
import com.vaadin.flow.component.button.Button;// Botão clássico: Cria um botão clicável (ex: "Salvar" ou "Cancelar").
import com.vaadin.flow.component.button.ButtonVariant;// Estilo do botão: Serve para mudar a cor do botão (ex: azul, vermelho).
import com.vaadin.flow.component.combobox.ComboBox;// Caixa de seleção: Abre uma lista de opções para escolher uma (ex: Gênero).
import com.vaadin.flow.component.formlayout.FormLayout;// Organizador de formulário: Alinha os campos de texto bonitinhos na tela.
import com.vaadin.flow.component.grid.Grid;// Tabela: Mostra a lista de filmes organizada em linhas e colunas.
import com.vaadin.flow.component.html.H2;// Título Grande: Cria um texto de título principal na página (<h2>).
import com.vaadin.flow.component.html.H3;// Título Médio: Cria um subtítulo um pouco menor (<h3>).
import com.vaadin.flow.component.html.Hr;// Linha divisória: Desenha uma linha reta cinza para separar as seções.
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;// Fila Horizontal: Coloca os componentes um ao lado do outro.
import com.vaadin.flow.component.orderedlayout.VerticalLayout;// Fila Vertical: Empilha os componentes um abaixo do outro (base da tela).
import com.vaadin.flow.component.textfield.NumberField;// Campo numérico: Caixa de texto que só aceita números.
import com.vaadin.flow.router.PageTitle;// Nome da Aba: Define o texto que aparece na aba do navegador.
import com.vaadin.flow.router.Route;// Endereço (Rota): Define o link/URL que abre esta página (ex: /cadastro).
import java.util.List;// Lista (Java): Guarda vários objetos juntos numa fila (ex: lista de filmes).
import java.util.Map;// Mapa (Java): Guarda dados em pares de "Chave e Valor" (tipo um dicionário).

@PageTitle("Relatório de filmes")
@Route("relatorio")

public class TelaRelatorioFilmes extends VerticalLayout {

    // Referência ao "banco de dados" da aplicação (na verdade é uma lista e memória, dentro da classe RepositorioFilmes).
    // É através dele que essa tela consegue ler, filtrar e calcular estatísticas dos filmes.
    private final RepositorioFilmes cadFilmes;

    // Campos de filtro (o que o usuário vê e preenche na tela)
    private final ComboBox<String> filtroGenero;
    private final NumberField filtroAnoMinimo;
    private final NumberField filtroNotaMinima;

    // Botões
    private final Button aplicarFiltroButton;
    private final Button limparFiltroButton;
    private final Button filtrarNotaAcimaMediaButton;

    // Tabela (grid) que mostra o resultado do filtro aplicado. Toda vez que o usuário filtra, é essa grid que é atualizada com a nova lista de filmes
    private final Grid<Filme> gridResultado;

    // Um "container" vertical vazio, que vai ser preenchido dinamicamente com os textos de estatística (nota média, duração média, etc.)
    private final VerticalLayout areaEstatisticas;

    // CONSTRUTOR (É aqui que tudo é montado: campos, botões, grid e a ordem em que aparecem na tela.)
    public TelaRelatorioFilmes() {
        
        cadFilmes = RepositorioFilmes.getInstance();

        // Configurações visuais básicas do layout: adiciona espaçamento entre os elementos e uma margem/padding ao redor de tudo
        setSpacing(true);
        setPadding(true);

        // Adiciona um título grande (H2) no topo da página
        add(new H2("- Relatório de Filmes -"));

        // Construção dos filtros 
        filtroGenero = new ComboBox<>("Gênero");
        filtroGenero.setItems("Romance", "Terror", "Suspense", "Comédia", "Ação", "Drama");
        // Mostra um "x" no campo para o usuário poder limpar a seleção facilmente
        filtroGenero.setClearButtonVisible(true);

        filtroAnoMinimo = new NumberField("Lançados a partir do ano");
        filtroAnoMinimo.setClearButtonVisible(true);

        filtroNotaMinima = new NumberField("Nota mínima");
        filtroNotaMinima.setClearButtonVisible(true);

        // Agrupa os três campos de filtro em um único layout de formulário, que organiza eles lado a lado
        FormLayout filtrosLayout = new FormLayout(filtroGenero, filtroAnoMinimo, filtroNotaMinima);

        // Cria o botão "Filtrar". addThemeVariants(LUMO_PRIMARY) deixa ele com a cor de destaque (geralmente azul), indicando ser a ação principal
        aplicarFiltroButton = new Button("Filtrar");
        aplicarFiltroButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // "e -> aplicarFiltros()" é uma expressão lambda: significa "quando esse evento de clique (e) acontecer, chame o método aplicarFiltros"
        aplicarFiltroButton.addClickListener(e -> aplicarFiltros());

        // Cria o botão "Limpar filtros", que ao ser clicado chama limparFiltros()
        limparFiltroButton = new Button("Limpar filtros");
        limparFiltroButton.addClickListener(e -> limparFiltros());

        // Cria um botão de atalho: filtra direto os filmes com nota acima damédia geral, sem precisar preencher nenhum campo
        filtrarNotaAcimaMediaButton = new Button("Nota acima da média geral");
        filtrarNotaAcimaMediaButton.addClickListener(e -> filtrarPorNotaAcimaDaMedia());

        // Agrupa os três botões de filtro lado a lado, em uma linha horizontal
        HorizontalLayout botoesFiltro = new HorizontalLayout(
                aplicarFiltroButton, limparFiltroButton, filtrarNotaAcimaMediaButton);

        // Construção da grid de resultado

        // Cria a tabela que vai exibir os filmes. O "Filme.class" diz ao Vaadin qual classe de objeto será exibida (ele usa isso para criar colunas
        // automaticamente, embora a gente sobrescreva isso na linha abaixo)
        gridResultado = new Grid<>(Filme.class);
        // Define manualmente quais colunas (atributos do Filme) aparecem e em que ordem: título, gênero, duração, nota e ano de lançamento
        gridResultado.setColumns("titulo", "genero", "duracao", "nota", "anoLancamento");
        // Ao abrir a tela por padrão, mostra TODOS os filmes (sem filtro nenhum)
        gridResultado.setItems(cadFilmes.listarTodosFilmes());

        // Construção da área de estatísticas 
        // Cria um layout vertical vazio que vai servir de "caixa" onde os textos de estatística serão inseridos dinamicamente depois
        areaEstatisticas = new VerticalLayout();
        areaEstatisticas.setSpacing(false); // sem espaço extra entre as linhas
        areaEstatisticas.setPadding(false); // sem margem interna

        // Botão para recalcular as estatísticas manualmente (útil se o usuário cadastrar/editar filmes em outra aba e quiser atualizar os números)
        Button atualizarEstatisticasButton = new Button("Atualizar estatísticas");
        atualizarEstatisticasButton.addClickListener(e -> atualizarEstatisticas());

        // Aqui é onde TUDO é efetivamente colocado na tela, na ordem em que vai aparecer de cima para baixo: 
        // filtros -> botões de filtro -> linha divisória -> título "Filmes" -> grid de resultado -> linha divisória ->
        // título "Estatísticas" -> botão de atualizar -> área de estatísticas -> linha divisória
        add(filtrosLayout, botoesFiltro, new Hr(),
                new H2("Filmes (resultado do filtro)"), gridResultado, new Hr(),
                new H2("Estatísticas"), atualizarEstatisticasButton, areaEstatisticas, new Hr());

        // Cria o botão "Voltar", leva o usuário de volta à página inicial do sistema
        Button backButton = new Button("Voltar");
        backButton.addClickListener(e -> UI.getCurrent().navigate(""));
        add(backButton);

        // Chama atualizarEstatisticas() já no início, para que os números apareçam imediatamente quando a tela é aberta, sem o usuário precisar clicar em nada
        atualizarEstatisticas();
    }

    // MÉTODOS DE FILTRO 

    // Lê o que o usuário preencheu nos três campos de filtro e pede ao repositório a lista de filmes que atende a essas condições
    private void aplicarFiltros() {
        // Pega o gênero escolhido no ComboBox. Se o usuário não escolheu nada, getValue() retorna null
        String genero = filtroGenero.getValue();
        // NumberField sempre retorna Double, mas filtrar() espera Integer.
        // O if verifica se o campo não está vazio antes de converter, evitando NullPointerException
        Integer ano;
        if (filtroAnoMinimo.getValue() != null) {
            ano = filtroAnoMinimo.getValue().intValue();// converte Double para Integer
        } else {
            ano = null; // campo vazio = sem filtro de ano
        }
        // Pega o valor digitado no campo de nota mínima (já é Double, então não precisa de conversão)
        Double nota = filtroNotaMinima.getValue();

        // Chama o método filtrar do repositório, passando os três critérios de uma vez. Esse método (lá no RepositorioFilmes) usa Stream para percorrer a lista e aplicar os filtros com lambdas ( public List<Filme> filtrar)
        List<Filme> resultado = cadFilmes.filtrar(genero, ano, nota);
        // Atualiza a grid para mostrar apenas os filmes que passaram pelo filtro
        gridResultado.setItems(resultado);
    }

    // Atalho: busca direto os filmes com nota acima da média geral, sem usar os campos de filtro manual
    private void filtrarPorNotaAcimaDaMedia() {
        List<Filme> resultado = cadFilmes.filtrarNotaAcimaDaMedia();
        gridResultado.setItems(resultado);
    }

    // Limpa todos os campos de filtro e volta a grid para mostrar todos os filmes cadastrados (sem filtro nenhum aplicado)
    private void limparFiltros() {
        filtroGenero.clear();
        filtroAnoMinimo.clear();
        filtroNotaMinima.clear();
        gridResultado.setItems(cadFilmes.listarTodosFilmes());
    }

    // MÉTODO DE ESTATÍSTICAS 

    // Esse método recalcula tudo do zero e reconstrói o conteúdo visual da área de estatísticas.
    //  É chamado tanto na abertura da tela quanto quando o usuário clica em "Atualizar estatísticas"
    private void atualizarEstatisticas() {
        // Remove TODOS os componentes que estavam dentro de areaEstatisticas antes de adicionar os novos.
        //  Sem isso, cada clique em "Atualizar" ia empilhar textos repetidos, um em cima do outro
        areaEstatisticas.removeAll();

        // Chama os métodos do repositório que fazem os cálculos via Stream:
        double mediaGeral = cadFilmes.calcularMediaNotaGeral();
        double duracaoMedia = cadFilmes.calcularDuracaoMedia();
        Map<String, Double> mediaPorGenero = cadFilmes.calcularMediaNotaPorGenero();
        Map<String, Long> contagemPorGenero = cadFilmes.contarFilmesPorGenero();
        Map<String, Double> duracaoMediaPorGenero = cadFilmes.calcularDuracaoMediaPorGenero();
        Filme maiorNota = cadFilmes.filmeComMaiorNota();
        Filme menorNota = cadFilmes.filmeComMenorNota();

        // Adiciona um texto (H3) mostrando a nota média geral
        areaEstatisticas.add(new H3("Nota média geral: " + mediaGeral));
        // Mesma coisa, mas para a duração média geral
        areaEstatisticas.add(new H3("Duração média geral: " + duracaoMedia));

        // Se existir um filme com maior nota (a lista não está vazia), mostra o título dele 
        // (.trim() remove espaços extras no início/fim do título, já que no cadastro original os títulos vêm com espaços) e a nota
        if (maiorNota != null) {
            areaEstatisticas.add(new H3("Maior nota: " + maiorNota.getTitulo().trim()
                    + " (" + maiorNota.getNota() + ")"));
        }
        // Mesma lógica, só que para o filme de menor nota
        if (menorNota != null) {
            areaEstatisticas.add(new H3("Menor nota: " + menorNota.getTitulo().trim()
                    + " (" + menorNota.getNota() + ")"));
        }

       areaEstatisticas.add(new H3("Por gênero:"));

        // keySet() retorna todos os gêneros cadastrados no Map (ex: "Terror", "Romance", "Drama", "Ação")
        // o for percorre cada um desses gêneros, um por vez
        for (String genero : contagemPorGenero.keySet()) {

        // monta cada parte da linha separadamente para ficar mais fácil de ler
        String quantidade = contagemPorGenero.get(genero) + " filme(s)";
        String nota = mediaPorGenero.getOrDefault(genero, 0.0) + " nota média";
        String duracao = duracaoMediaPorGenero.getOrDefault(genero, 0.0) + " duração média";

        // junta tudo em uma linha só
        // ex: "Terror — 1 filme(s) | 3.6 nota média | 1.0 duração média"
        String linha = genero + " — " + quantidade + " | " + nota + " | " + duracao;

        // Transforma a linha de texto em um parágrafo visual e adiciona na tela
        areaEstatisticas.add(new com.vaadin.flow.component.html.Paragraph(linha));
       }
    }
}
