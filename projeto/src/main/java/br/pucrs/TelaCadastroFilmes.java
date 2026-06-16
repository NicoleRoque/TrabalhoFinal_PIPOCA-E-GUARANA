package br.pucrs;

//todas essas importações são partes do vaadin, são feitas para que possamos usar a maior parte do codigo
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout; //importa a classe vertical
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle; //importa o titulo
import com.vaadin.flow.router.Route; //importa o sub titulo
import com.vaadin.flow.component.grid.Grid; //importa a tabela 
import com.vaadin.flow.component.notification.Notification;//importa a notificação
import com.vaadin.flow.component.Key; //importa o atalho para salvar

@PageTitle("Tela de cadastro de filmes")//titulo da pagina
@Route("cadastro") //sub titulo

public class TelaCadastroFilmes extends VerticalLayout { //declaração da classe que faremos o codigo
    // Instância do cadastro de filmes
    private final RepositorioFilmes cadFilmes;
    // Campos do formulário, são as informações basicas de filme que apareceram no formulario
    private final TextField titulo; //texto 
    private final TextField duracao; //texto 
    private final TextField nota; //texto 
    private final TextField anoLancamento; //texto 
    private final ComboBox<String> genero; //caixa de seleção, vai aparecer todos os generos para selecionar

    // Botoes
    private final Button salvarButton; //botão de salvar
    private final Button cancelarButton; //botão de cancelar
    // Grid para exibir os filmes(tabela)
    private final Grid<Filme> grid;
    // Referencia para o filme selecionado
    Filme filmeselecionado;


     public TelaCadastroFilmes() {
        // Inicializando o cadastro de filmes
        cadFilmes = RepositorioFilmes.getInstance();
        // inicializando os campos do formulário
        titulo = new TextField("Titulo");
        titulo.setReadOnly(true); // O Titulo não pode ser mudado(atualizado)
        duracao = new TextField("Duração");
        anoLancamento = new TextField("Ano de lançamento");
        nota = new TextField(" Nota");
        genero = new ComboBox<>("Genero");
        genero.setItems("Romance", "Terror", "Suspense", "Comédia", "Ação", "Drama");
        // inicializando o Grid para exibir os filmes
        grid = new Grid<>(Filme.class);

        // Definindo as características do layout básico
        setSpacing(true);
        setPadding(true);

        // Define título do formulário
        add(new H2(" - Cadastro de filmes - Edição"));

        // Configuração do formulário
        FormLayout formLayout = new FormLayout( genero,  duracao,  nota,  titulo,  anoLancamento);

        // Definição dos botões de ação
        salvarButton = new Button("Atualizar", VaadinIcon.CHECK.create());
        salvarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        salvarButton.addClickShortcut(Key.ENTER); //Cria um atalho de teclado para o botão. Quando o usuário pressionar Enter, será como se tivesse clicado em Salvar.
        salvarButton.addClickListener(click -> this.atualizarFormulario()); //clicou, atualizou o formulario

        cancelarButton = new Button("Cancelar");
        Dialog dialogoCancelamento = criaDialogoDeCancelamento();
        cancelarButton.addClickListener(click -> dialogoCancelamento.open());

        // Adiciona botoes de ação em um layout horizontal
        HorizontalLayout botoesLayout = new HorizontalLayout(salvarButton, cancelarButton);

        // Configuração do Grid
        grid.setItems(cadFilmes.listarTodosFilmes());
        grid.setColumns("titulo", "genero",  "duracao",  "nota",  "anoLancamento"); //colunas que fazem parte da estrutura
        grid.asSingleSelect().addValueChangeListener(event -> preparaEdicaoPessoa(event));

        // Monta todos os elementos na janela
        add(formLayout, botoesLayout, new H2("Filmes Cadastrados"), grid);
        add(new Hr());

        // Define o botão de retorno à página principal
        Button backButton = new Button("Voltar");
        backButton.addClickListener(e -> UI.getCurrent().navigate(""));//botão de voltar
        add(backButton);

        // deixa formulário desabilitado no início
        habilitarFormulario(false);

    }

    
    // Atualiza o filme selecionado
    private void atualizarFormulario() {
        Filme f = new Filme( //instanciamos um objeto filme e acessamos os valores dele
                genero.getValue(),
                Integer.parseInt(duracao.getValue()),
                Double.parseDouble(nota.getValue()),
                titulo.getValue(),
                Integer.parseInt(anoLancamento.getValue())

        );
        

        cadFilmes.update(filmeselecionado.getID(), f);

        String mensagem = "Filme " + f.getTitulo() + " atualizado com sucesso!";
        Notification.show(mensagem, 3000, Notification.Position.BOTTOM_STRETCH);

        grid.getDataProvider().refreshAll();
        limparFormulario();
        habilitarFormulario(false); // Desabilita o form após salvar
    }

    // Preenche o formulário a partir do grid
    private void preencherFormulario(Filme filme) {
        titulo.setValue(filme.getTitulo());
        duracao.setValue(String.valueOf(filme.getDuracao())); //converte o int para string 
        anoLancamento.setValue(String.valueOf(filme.getAnoLancamento())); //converte o int para string
        genero.setValue(filme.getGenero());
    }

    // Habilitar/desabilitar os campos do formulário
    private void habilitarFormulario(boolean opcao) {
        // 'Titulo' é readonly, então não mexemos no 'enabled' dele
        duracao.setEnabled(opcao);
        anoLancamento.setEnabled(opcao);
        genero.setEnabled(opcao);
        salvarButton.setEnabled(opcao); //botão de salvar
        cancelarButton.setEnabled(opcao); //botão de cancelar
    }

    private void preparaEdicaoPessoa(ComponentValueChangeEvent<Grid<Filme>, Filme> event) {
        {
            filmeselecionado = event.getValue(); // a variavel filme selecionado vai receber 

            if (filmeselecionado != null) { //se o filme selecionado or diferente de null
                // Se um filme for selecionado, preenche o formulário
                preencherFormulario(filmeselecionado);
                habilitarFormulario(true); //mostra que foi preenchido
            } else {
                // Se a seleção foi limpa, limpa o formulário
                limparFormulario();
                habilitarFormulario(false);
            }
        }
    }

    // Limpa seleção do grid
    private void limparFormulario() {
        // Desseleciona qualquer item na grid (isso evita loops de eventos)
        grid.asSingleSelect().clear();
        // Limpa os campos
        titulo.clear();
        anoLancamento.clear();
        genero.clear();
        duracao.clear();
        // Coloca o foco no campo nome
        titulo.focus();
    }

    private Dialog criaDialogoDeCancelamento() {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar cancelamento");
        dialogo.add(new Paragraph("Você tem certeza que deseja cancelar e limpar o formulário?"));
        Button confirmarCancelamento = new Button("Sim, cancelar", e -> {
            limparFormulario();
            dialogo.close();
        });
        confirmarCancelamento.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        Button fecharDialogo = new Button("Não", e -> dialogo.close());
        dialogo.getFooter().add(fecharDialogo, confirmarCancelamento);
        return dialogo;
    }
}
