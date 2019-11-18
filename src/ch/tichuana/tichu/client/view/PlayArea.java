package ch.tichuana.tichu.client.view;

import ch.tichuana.tichu.client.model.ClientModel;
import ch.tichuana.tichu.client.services.ServiceLocator;
import ch.tichuana.tichu.client.services.Translator;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class PlayArea extends GridPane {

	private Label[] playerLbl;
	private Label[] headings;
	private ClientModel clientModel;

	/**
	 * creates a table-like, resizeable grid for information about the game-flow
	 * @author Philipp
	 */
	PlayArea(ClientModel clientModel) {

		this.clientModel = clientModel;
		Translator translator = ServiceLocator.getServiceLocator().getTranslator();
		this.headings = new Label[7];
		headings[0] = new Label(translator.getString("name"));
		headings[1] = new Label(translator.getString("team"));
		headings[2] = new Label(translator.getString("hand"));
		headings[3] = new Label(translator.getString("tichu"));
		headings[4] = new Label(translator.getString("played"));
		headings[5] = new Label(translator.getString("matchPoints"));
		headings[6] = new Label(translator.getString("total"));

		for (int i = 0; i < headings.length; i++)	{
			this.add(headings[i], i, 0);
			GridPane.setHgrow(headings[i], Priority.ALWAYS);
		}

		this.add(new Separator(), 0, 1, 7, 1);

		this.playerLbl = new Label[4];
		for (int i = 0; i < playerLbl.length; i++) {
			playerLbl[i] = new Label("Waiting for player...");
		}

		this.add(playerLbl[0], 0, 2, 1, 1);
		GridPane.setVgrow(playerLbl[0], Priority.ALWAYS);
		this.add(new Separator(), 0, 3, 5, 1);

		this.add(playerLbl[1], 0, 4, 1, 1);
		GridPane.setVgrow(playerLbl[1], Priority.ALWAYS);
		this.add(new Separator(), 0, 5, 7, 1);

		this.add(playerLbl[2], 0, 6, 1, 1);
		GridPane.setVgrow(playerLbl[2], Priority.ALWAYS);
		this.add(new Separator(), 0, 7, 5, 1);

		this.add(playerLbl[3], 0, 8, 1, 1);
		GridPane.setVgrow(playerLbl[3], Priority.ALWAYS);

		this.add(new Separator(), 0, 9, 9, 1);

		this.maxWidth(6000);
		this.maxHeight(6000);
		this.setHgap(2);
		this.setVgap(2);
	}

	public void updatePlayerName() {
		this.playerLbl[0].setText(clientModel.getPlayerName());
	}

	public void updateNameColumn() {
		this.playerLbl[1].setText(clientModel.getTeamMate());
		this.playerLbl[2].setText(clientModel.getOpponent(0));
		this.playerLbl[3].setText(clientModel.getOpponent(1));
	}

	public void updateTeamColumn() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			this.add(new Label(""), 1, i);
		}
	}

	public void updateHandColumn() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			this.add(new Label(""), 2, i);
		}
	}

	public void updateTichuColumn() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			this.add(new Label(""), 3, i);
		}
	}

	public void updatePlayedColumn() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			//TODO - Change after GUI-Testing to be able to add the real cards from the msg
			this.add(new CardArea(CardArea.CardAreaType.Thumbnails, 8), 4, i);
		}
	}

	public void updateMatchPoints() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			this.add(new Label(""), 5, i);
		}
	}

	public void updateTotalPoints() {
		for (int i = 2; i < this.getColumnCount(); i+=2) {
			this.add(new Label(""), 6, i);
		}
	}
}