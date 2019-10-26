package ch.tichuana.tichu.commons.message;

import ch.tichuana.tichu.commons.models.Card;
import ch.tichuana.tichu.commons.models.TichuType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Message {

	private MessageType msgType;

	/**
	 * Sends the message (msg) of this instace through the socket given as a parameter
	 * @author Christian
	 * @param socket
	 */
	public void send(Socket socket) {
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(socket.getOutputStream());
			out.write(this.toString()+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives a JSON-String from a specified
	 * @author Christian
	 * @param socket
	 */
	public static Message receive(Socket socket) {
		BufferedReader in;
		String response = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			response = in.readLine();
		} catch (IOException e){
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		JSONObject message = null;

		if (response != null) {
			try {
				message = (JSONObject) parser.parse(response.toString());
			} catch (Exception e){
				e.printStackTrace();
			}
			return parseMessage(message);
		}
		return null;
	}

	/**
	 * Parses a JSON-Message and returns a Message-object of the corresponding Message class
	 * @author Christian
	 * @param json
	 * @return Message
	 */
	public static Message parseMessage(JSONObject json){
		String playerName, password, status;
		TichuType tichuType;
		ArrayList<String> players = new ArrayList();
		JSONArray array;
		ArrayList cards;


		MessageType messageType = MessageType.valueOf((String) json.get("msg"));
		Message newMessage = null;

		switch (messageType){

			case AnnouncedTichuMsg:
				tichuType = TichuType.valueOf((String) json.get("tichuType"));
				array = (JSONArray) json.get("players");
				Iterator<String> iterator = array.iterator();
				while (iterator.hasNext()){ players.add(iterator.next()); }
				newMessage = new AnnouncedTichuMsg(players, tichuType);
				break;

			case JoinMsg:
				playerName = (String) json.get("playerName");
				password = (String) json.get("password");
				newMessage = new JoinMsg(playerName,password);
				break;

			case CreatePlayerMsg:
				playerName = (String) json.get("playerName");
				password = (String) json.get("password");
				newMessage = new CreatePlayerMsg(playerName,password);
				break;

			case ConnectedMsg:
				status = (String) json.get("status");
				newMessage = new ConnectedMsg(Boolean.parseBoolean(status));
				break;

			case GameStartedMsg:
				playerName = (String) json.get("teamMate");
				array = (JSONArray) json.get("opponents");
				String[] opponents = new String[2];
				opponents[0] = (String) array.get(0);
				opponents[1] = (String) array.get(1);
				newMessage = new GameStartedMsg(playerName, opponents);
				break;

			case DealMsg:
				cards= new ArrayList();
				array = (JSONArray) json.get("cards");
				Iterator iterator2 = array.iterator();
				while (iterator2.hasNext()){
					cards.add(Card.cardFactory((JSONObject) iterator2.next()));
				}
				newMessage = new DealMsg(cards);
				break;

			case ReceivedMsg:
				status = (String) json.get("status");
				newMessage = new ReceivedMsg(Boolean.parseBoolean(status));
				break;

			case DemandTichuMsg:
				tichuType = TichuType.valueOf((String) json.get("tichuType"));
				newMessage = new DemandTichuMsg(tichuType);
				break;

			case TichuMsg:
				playerName = (String) json.get("playerName");
				tichuType = TichuType.valueOf((String) json.get("tichuType"));
				newMessage = new TichuMsg(playerName,tichuType);
				break;

			case DemandSchupfenMsg:
				playerName = (String) json.get("playerName");
				newMessage = new DemandSchupfenMsg(playerName);
				break;

			case SchupfenMsg:
				playerName = (String) json.get("playerName");
				Card card = Card.cardFactory((JSONObject) json.get("card"));
				newMessage = new SchupfenMsg(playerName,card);
				break;

			case PlayMsg:
				cards= new ArrayList();
				array = (JSONArray) json.get("cards");
				Iterator iterator3 = array.iterator();
				while (iterator3.hasNext()){
					cards.add(Card.cardFactory((JSONObject) iterator3.next()));
				}
				newMessage = new PlayMsg(cards);
				break;

			case UpdateMsg:
				cards= new ArrayList();
				array = (JSONArray) json.get("lastMove");
				Iterator iterator4 = array.iterator();
				while (iterator4.hasNext()){
					cards.add(Card.cardFactory((JSONObject) iterator4.next()));
				}
				int opponentScore = (Integer) json.get("opponentScore");
				int ownScore = (Integer) json.get("ownScore");
				playerName = (String) json.get("playerName");
				newMessage = new UpdateMsg(playerName,cards,opponentScore,ownScore);
				break;
		}
		return newMessage;
	}

	public MessageType getMsgType(){
		return this.msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	// Methods only here to be overwritten
	public String getPlayerName(){ return null; }
	public String getPassword(){ return null; }
	public boolean getStatus(){ return false; }
	public TichuType getTichuType() { return null; }
	public ArrayList getPlayers(){ return null; }
	public String[] getOpponents(){ return null; }
	public String getTeamMate(){ return null; }
	public Card getCard() { return null; }
	public ArrayList getCards(){return null; }
	public String getNextPlayer() { return null; }
	public ArrayList<Card> getLastMove() {
		return null;
	}
	public int getOpponentScore() {
		return 0;
	}
	public int getOwnScore() {
		return 0;
	}
}