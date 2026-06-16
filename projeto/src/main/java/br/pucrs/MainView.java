package br.pucrs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("home")
public class MainView extends VerticalLayout {
    public MainView() {
        Button sayHelloButton = new Button("Say hello");
       sayHelloButton.addClickListener(e -> {
             Notification.show("Bem Vindo(a)!");
       });
       add(sayHelloButton);

       add(new Hr());

        RouterLink link_Para_Tela_Cadastro = new RouterLink("Ir para a tela de cadastro", TelaCadastroFilmes.class);
        add(link_Para_Tela_Cadastro);
    }
}
