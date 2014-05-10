package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.util.Log;

public class ClientCommunicationManager implements ICommunicationManager,
		IGameServer {

	private ICommunicationChannel commChannel;
	private GameClient gameClient;

	public ClientCommunicationManager(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public void setCommChannel(ICommunicationChannel commChannel) {
		this.commChannel = commChannel;
	}

	@Override
	public void receive(CommunicationObject object) {
		if (object.getType() == CommunicationObject.DEBUG) {
			Log.d("CommunicationManager", (String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.UPDATE_SCREEN) {
			List<Element> drawings = (List<Element>) object.getMessage();
			this.gameClient.updateScreen(drawings);
		}
		if (object.getType() == CommunicationObject.UPDATE_PLAYERS) {
			HashMap<String, BombermanPlayer> players = (HashMap<String, BombermanPlayer>) object
					.getMessage();
			this.gameClient.updatePlayers(players);
		}
		if (object.getType() == CommunicationObject.INIT) {
			List<List<Element>> elements = (List<List<Element>>) object
					.getMessage();
			this.gameClient.init(elements);
		}
	}

	@Override
	public void putBomberman(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMBERMAN, username);
		commChannel.send(object);
	}

	@Override
	public void addCommChannel(ICommunicationChannel commChannel) {
		setCommChannel(commChannel);
	}

	@Override
	public void putBomb(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMB, username);
		commChannel.send(object);
	}

	@Override
	public void move(String username, Direction direction) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("direction", direction);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.MOVE, params);
		commChannel.send(object);
	}

	@Override
	public void pause(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void quit(String username) {
		// TODO Auto-generated method stub

	}

}
