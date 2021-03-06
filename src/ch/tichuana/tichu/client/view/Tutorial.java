package ch.tichuana.tichu.client.view;

import ch.tichuana.tichu.client.services.Configuration;
import ch.tichuana.tichu.client.services.ServiceLocator;
import ch.tichuana.tichu.client.services.Translator;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Tutorial extends Stage {

    private static Configuration configuration;
    private static Translator translator;
    private static Tutorial tutorial;
    private Button nextBtnRules, nextBtnCards, nextBtnMoves;
    private Scene scene;

    private Tutorial(){

        this.setMinWidth(800);
        this.setMinHeight(400);

        ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();

        // needs to stay
        translator = serviceLocator.getTranslator();
        configuration = serviceLocator.getConfiguration();

        // next buttons
        nextBtnRules = new Button(translator.getString("tutorial.next"));
        nextBtnCards = new Button(translator.getString("tutorial.next"));
        nextBtnMoves = new Button(translator.getString("tutorial.next"));

        ArrayList<Tab> tabs = new ArrayList<>();

        Tab rules = makeRulesTab();
        Tab moves = makeValidMoveTab();
        Tab cards = makeCardsTab();
        Tab credits = makeCreditsTab();

        rules.getStyleClass().add("tabs");
        moves.getStyleClass().add("tabs");
        cards.getStyleClass().add("tabs");
        credits.getStyleClass().add("tabs");

        tabs.add(rules);
        tabs.add(moves);
        tabs.add(cards);
        tabs.add(credits);

        for (Tab tab : tabs){
            tab.setOnSelectionChanged(e-> {
                if (tab.isSelected()) tab.getStyleClass().add("active");
                else tab.getStyleClass().remove("active");
            });
        }

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("red");
        tabPane.getTabs().addAll(rules,moves, cards,credits);


        // selection model -> for next buttons
        SelectionModel<Tab> select = tabPane.getSelectionModel();
        nextBtnRules.setOnAction(e -> select.select(moves));
        nextBtnMoves.setOnAction(e -> select.select(cards));
        nextBtnCards.setOnAction(e -> select.select(credits));

        // scene
        this.scene = new Scene(tabPane);
        this.setScene(scene);
        this.setHeight(600);
        this.setWidth(1000);

        this.getIcons().add(new Image(configuration.getProperty("tichu-icon")));
        scene.getStylesheets().add(
                getClass().getClassLoader().getResource(configuration.getProperty("tutorialStyle")).toExternalForm());
    }

    /**
     * factory method to return singleton Tutorial. We only ever need one instance
     * @author Dominik
     * @return the only existing instance of this class
     */
    public static Tutorial getTutorial(){
        if (tutorial == null ){
            tutorial = new Tutorial();
        } else if (ServiceLocator.getServiceLocator().getTranslator() != translator) {
            // if translator has changed, we also make new tutorial
            tutorial = new Tutorial();
        }
        return tutorial;
    }


    /**
     * creates the valid moves tab
     * @author Dominik
     * @return validMoves tab
     */
    public Tab makeValidMoveTab(){
        // valid moves
        Tab validMoves = new Tab(translator.getString("tutorial.moves"));
        ScrollPane scrollPane = new ScrollPane();
        GridPane grid = new GridPane();

        // combinations
        String[] combinations = {"highcard", "pair", "step", "bomb","fullhouse", "straight", "straightflush"};
        int rowCount = 0;
        for (String combination : combinations){
            // create image view
            String path = configuration.getProperty("tutorial")+ combination + ".png";
            Image img = new Image(path);
            ImageView imgv= new ImageView(img);
            imgv.setPreserveRatio(true);
            imgv.setFitHeight(100);
            grid.add(imgv,0,rowCount);

            // create description
            Label lbl = new Label(translator.getString("tutorial.moves."+combination));
            lbl.setWrapText(true);
            lbl.setPrefWidth(300);
            grid.add(lbl,1, rowCount);
            rowCount++;
        }
        grid.add(nextBtnMoves,0,rowCount+1);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setId("validmove");
        grid.setMaxWidth(700);
        scrollPane.getStyleClass().add("red");
        scrollPane.setContent(grid);
        validMoves.setContent(scrollPane);
        validMoves.setClosable(false);

        return validMoves;
    }

    /**
     * creates the rules Tab
     * @author Dominik
     * @return rulesTab
     */
    public Tab makeRulesTab(){
        Tab rulesTab = new Tab(translator.getString("tutorial.rules"));

        ScrollPane scrollPane = new ScrollPane();

        VBox root = new VBox();
        String[] rules = {"players", "matches", "tricks", "smalltichu", "grandtichu", "push", "start", "combo", "points"};
        int ruleCounter = 0;
        for (String rule : rules){
            ruleCounter ++;
            Label lbl = new Label(ruleCounter + ". " + translator.getString("tutorial.rules."+rule));
            lbl.setWrapText(true);
            root.getChildren().add(lbl);
        }
        root.getChildren().add(nextBtnRules);
        root.setSpacing(10);

        root.setMaxWidth(700);
        scrollPane.setContent(root);
        rulesTab.setContent(scrollPane);
        rulesTab.setClosable(false);
        return rulesTab;
    }

    /**
     * creates the cards tab
     * @author Dominik
     * @return cardsTab
     */
    public Tab makeCardsTab(){
        Tab cardsTab = new Tab(translator.getString("tutorial.cards"));
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox();

        String[] rules = {"deck", "suit", "score"};
        int ruleCounter = 0;
        for (String rule : rules){
            ruleCounter ++;
            Label lbl = new Label(ruleCounter + ". " + translator.getString("tutorial.cards."+rule));
            lbl.setWrapText(true);
            root.getChildren().add(lbl);
        }

        GridPane grid = new GridPane();
        String[] specialCards = {"mahjong", "hound", "phoenix", "dragon"};
        int rowCounter = 0;
        for (String rule : specialCards){
            String path = configuration.getProperty("tutorial")+ rule + ".png";
            Image img = new Image(path);
            ImageView imgv = new ImageView(img);
            imgv.setPreserveRatio(true);
            imgv.setFitHeight(100);
            Label lbl = new Label(translator.getString("tutorial.specialcard."+ rule));
            lbl.setWrapText(true);
            grid.add(imgv,0,rowCounter);
            grid.add(lbl,1,rowCounter);
            rowCounter++;
        }
        grid.setVgap(10);
        grid.setHgap(10);
        root.getChildren().add(grid);
        root.getChildren().add(nextBtnCards);

        root.setMaxWidth(700);
        root.setSpacing(10);
        scrollPane.setContent(root);
        cardsTab.setContent(scrollPane);
        cardsTab.setClosable(false);
        return cardsTab;
    }

    public Tab makeCreditsTab(){
        Tab tab = new Tab(translator.getString("tutorial.credits"));
        tab.setClosable(false);

        ScrollPane scrollPane = new ScrollPane();

        VBox root = new VBox();
        Label text = new Label(translator.getString("tutorial.credits.text"));
        Label col = new Label(translator.getString("tutorial.credits.collaborators"));

        text.setWrapText(true);
        root.getChildren().addAll(text,col);
        root.setMaxWidth(700);
        scrollPane.setContent(root);
        tab.setContent(scrollPane);

        return tab;
    }
}
