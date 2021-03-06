package ch.tichuana.tichu.client.chat;

import ch.tichuana.tichu.client.services.ServiceLocator;
import ch.tichuana.tichu.client.services.Translator;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Dominik
 */
public class ChatView extends Stage{

    private VBox scrollBox;
    private Button btnSend;
    private TextField txt;
    private static ChatView view;
    private Translator translator;

    public ChatView(){

        this.translator = ServiceLocator.getServiceLocator().getTranslator();

        //Minimal size for chat window
        this.setMinWidth(300);
        this.setMinHeight(300);

        //VBox as main overlay
        VBox box = new VBox();

        //ScrollPane for Messages
        ScrollPane pane = new ScrollPane();
        pane.getStyleClass().add("chat");
        VBox.setVgrow(pane, Priority.ALWAYS);


        //Vbox for showing seperate Messages in Scrollpane
        scrollBox = new VBox();
        pane.setContent(scrollBox);


        //HBox for TextArea and Send Button
        HBox bottomBox = new HBox();
        txt = new TextField();
        HBox.setHgrow(txt, Priority.ALWAYS);
        bottomBox.getStyleClass().add("chat");

        btnSend = new Button(translator.getString("chat.send"));
        bottomBox.getChildren().addAll(txt, btnSend);
        box.getChildren().addAll(pane,bottomBox);

        pane.vvalueProperty().bind(scrollBox.heightProperty());

        Scene scene = new Scene(box);
        scene.getStylesheets().add(
                getClass().getClassLoader().getResource(ServiceLocator.getServiceLocator()
                        .getConfiguration().getProperty("chatStyle")).toExternalForm());
        this.setScene(scene);
        this.setWidth(500);
        this.setHeight(750);
        this.getIcons().add(new Image(ServiceLocator.getServiceLocator().getConfiguration().getProperty("tichu-icon")));
        this.setTitle(translator.getString("chat"));

    }

    public void addMessage(String name, String content){
        MessageView view = new MessageView(name, content);
        this.scrollBox.getChildren().add(view);
    }

    public Button getBtnSend() {
        return btnSend;
    }

    public TextField getTxt(){ return txt;}

    public void clearText(){
        txt.clear();
    }

    public String getContent(){
        return txt.getText();
    }

    public static ChatView getView(){
        if(view == null)
            view = new ChatView();
        return view;
    }

    public void update() {
        this.translator = ServiceLocator.getServiceLocator().getTranslator();
        this.btnSend.setText(translator.getString("chat.send"));
        this.setTitle(translator.getString("chat"));
    }
}
