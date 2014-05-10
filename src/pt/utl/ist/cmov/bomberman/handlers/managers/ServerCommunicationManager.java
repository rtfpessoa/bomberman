package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.IGameClient;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.util.Log;

public class ServerCommunicationManager implements ICommunicationManager,
		IGameClient {

	private List<ICommunicationChannel> commChannels;
	private GameServer gameServer;

	public ServerCommunicationManager(GameServer gameServer) {
		this.commChannels = new ArrayList<ICommunicationChannel>();
		this.gameServer = gameServer;
	}

	public void addCommChannel(ICommunicationChannel commChannel) {
		this.commChannels.add(commChannel);
		this.gameServer.initClient();
	}

	@Override
	public void receive(CommunicationObject object) {
		if (object.getType() == CommunicationObject.DEBUG) {
			Log.d("CommunicationManager", (String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.PUT_BOMBERMAN) {
			this.gameServer.putBomberman((String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.PUT_BOMB) {
			this.gameServer.putBomb((String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.PAUSE) {
			this.gameServer.pause((String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.QUIT) {
			this.gameServer.quit((String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.MOVE) {
			HashMap<String, Object> params = (HashMap<String, Object>) object
					.getMessage();

			this.gameServer.move((String) params.get("username"),
					(Direction) params.get("direction"));
		}
	}

	@Override
	public void updateScreen(List<Element> drawings) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, drawings);

		broadcast(object);
	}

	@Override
	public void init(List<List<Element>> elements) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.INIT, elements);

		broadcast(object);
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_PLAYERS, players);

		broadcast(object);
	}

	private void broadcast(CommunicationObject object) {
		for (Iterator<ICommunicationChannel> commChannel = commChannels
				.iterator(); commChannel.hasNext();) {
			commChannel.next().send(object);
		}
	}

}
