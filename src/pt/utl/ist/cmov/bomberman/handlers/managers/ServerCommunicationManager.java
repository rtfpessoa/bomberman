package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.IGameClient;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.util.Log;
import com.google.gson.Gson;

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
	public void receive(String object) {
//		if (object.getType() == CommunicationObject.DEBUG) {
//			Log.i("CommunicationManager", (String) object.getMessage());
//		}
//		if (object.getType() == CommunicationObject.PUT_BOMBERMAN) {
//			this.gameServer.putBomberman((String) object.getMessage());
//		}
//		if (object.getType() == CommunicationObject.PUT_BOMB) {
//			this.gameServer.putBomb((String) object.getMessage());
//		}
//		if (object.getType() == CommunicationObject.PAUSE) {
//			this.gameServer.pause((String) object.getMessage());
//		}
//		if (object.getType() == CommunicationObject.QUIT) {
//			this.gameServer.quit((String) object.getMessage());
//		}
//		if (object.getType() == CommunicationObject.MOVE) {
//			HashMap<String, Object> params = (HashMap<String, Object>) object
//					.getMessage();
//
//			this.gameServer.move((String) params.get("username"),
//					(Direction) params.get("direction"));
//		}
	}

	@Override
	public void updateScreen(ArrayList<Drawing> drawings) {
		Gson gson = new Gson();
		String innerJson = gson.toJson(drawings);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, innerJson);

		String json = gson.toJson(object);

		broadcast(CommunicationObject.UPDATE_SCREEN + "±" + json);
	}

	@Override
	public void init(Integer lines, Integer cols, ArrayList<Drawing> drawings) {
		HashMap<String, Object> message = new HashMap<String, Object>();
		message.put("lines", lines);
		message.put("cols", cols);
		message.put("drawings", drawings);

		Gson gson = new Gson();
		String innerJson = gson.toJson(message);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.INIT, innerJson);

		String json = gson.toJson(object);

		broadcast(CommunicationObject.INIT + "±" + json);
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		Gson gson = new Gson();
		String innerJson = gson.toJson(players);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_PLAYERS, innerJson);

		String json = gson.toJson(object);

		broadcast(CommunicationObject.UPDATE_PLAYERS + "±" + json);
	}

	private void broadcast(String object) {
		for (Iterator<ICommunicationChannel> commChannel = commChannels
				.iterator(); commChannel.hasNext();) {
			commChannel.next().send(object);
		}
	}

}
