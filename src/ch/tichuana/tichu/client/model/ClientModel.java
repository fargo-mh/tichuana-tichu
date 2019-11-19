package ch.tichuana.tichu.client.model;

import ch.tichuana.tichu.commons.message.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientModel {

    private SimpleStringProperty newestMessage = new SimpleStringProperty();
    private SimpleIntegerProperty msgCode = new SimpleIntegerProperty();
    private Socket socket;
    private volatile boolean closed;
    private String playerName;
    private String nextPlayerName;
    private String playerToSchupfCard;
    private SimpleBooleanProperty connected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty hisTurn = new SimpleBooleanProperty(false);
    private Logger logger = Logger.getLogger("");

    private String teamMate;
    private String[] opponents;
    private Hand hand;

    /**
     * connects client to server with JoinMsg and listens for incoming messages
     * @author Philipp
     * @param ipAddress ip 127.0.0.1 from config.properties
     * @param port port number 8080 from config.properties
     * @param playerName receiving from GUI
     * @param password receiving from GUI
     */
    public void connect(String ipAddress, int port, String playerName, String password) {
        logger.info("Connect");
        this.playerName = playerName;
        this.closed = false;
        try {
            socket = new Socket(ipAddress, port);

            Runnable r = () -> {
                while(!closed) {

                    Message msg = Message.receive(socket);

                    if (msg instanceof ConnectedMsg) {
                        if (msg.getStatus()) {
                            this.connected.set(true);
                            newestMessage.set("successfully connected to Server");
                        } else
                            newestMessage.set("connection failed: wrong password");
                    }

                    if (msg instanceof GameStartedMsg) {
                        newestMessage.set("you successfully entered a game");
                        this.teamMate = msg.getTeamMate();
                        this.opponents = msg.getOpponents();
                    }

                    if (msg instanceof DealMsg) {
                        logger.info(msg.getCards().toString());
                        this.hand = new Hand(msg.getCards());
                        newestMessage.set("your first eight cards");
                    }

                    if (msg instanceof AnnouncedTichuMsg) {
                        newestMessage.set("");
                        newestMessage.set(msg.getPlayers().toString()+" announced: "+msg.getTichuType());
                    }

                    if (msg instanceof DemandTichuMsg) {
                        logger.info("please announce tichu or pass");
                    }

                    if (msg instanceof DemandSchupfenMsg) {
                        if (!this.playerName.equals(msg.getPlayerName())) {
                            this.playerToSchupfCard = msg.getPlayerName();
                            logger.info("please choose card for player: "+msg.getPlayerName());
                        } else
                            sendMessage(new ReceivedMsg(true));
                    }

                    if (msg instanceof UpdateMsg) {
                        if (!this.playerName.equals(msg.getNextPlayer())) {
                            this.hisTurn.set(false);
                            this.nextPlayerName = msg.getNextPlayer();
                            sendMessage(new ReceivedMsg(true));
                        } else {
                            this.hisTurn.set(true);
                            logger.info("it is your turn "+msg.getNextPlayer());
                        }

                    }
                }
            };
            Thread t = new Thread(r);
            t.start();

            Message msg = new JoinMsg(playerName, password);
            msg.send(socket);

        } catch(IOException e) {
            logger.warning(e.toString());
        }
    }

    /**
     * stops listening and closes socket
     * @author Philipp
     */
    public void disconnect() {
        logger.info("Disconnect");
        this.closed = true;

        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * called from controller to send messages to Player-Object (Server)
     * @author Philipp
     * @param message from a specific type
     */
    public void sendMessage(Message message) {
        message.send(this.socket);
    }

    //TODO - needed for broadcasts or not?
    public String receiveMessage() {
        logger.info("Receive Message");
        return newestMessage.get();
    }

    //Getter
    public String getPlayerToSchupfCard() {
        return playerToSchupfCard;
    }
    public boolean isConnected() {
        return connected.get();
    }
    public int getMsgCode() {
        return msgCode.get();
    }
    public SimpleIntegerProperty msgCodeProperty() {
        return msgCode;
    }
    public SimpleBooleanProperty getConnectedProperty() {
        return connected;
    }
    public SimpleBooleanProperty getHisTurnProperty() {
        return hisTurn;
    }
    public SimpleStringProperty getNewestMessageProperty() {
        return newestMessage;
    }
    public String getTeamMate() {
        return teamMate;
    }
    public String getOpponent(int i) {
        return opponents[i];
    }
    public String getPlayerName() {
        return playerName;
    }
    public Hand getHand() {
        return hand;
    }
}