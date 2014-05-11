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
		Gson gson = new Gson();

		CommunicationObject obj = gson.fromJson(object,
				CommunicationObject.class);

		if (obj.getType().equals(CommunicationObject.DEBUG)) {
			Log.i("CommunicationManager", obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.PUT_BOMBERMAN)) {
			this.gameServer.putBomberman(obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.PUT_BOMB)) {
			this.gameServer.putBomb(obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.PAUSE)) {
			this.gameServer.pause(obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.QUIT)) {
			this.gameServer.quit(obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.MOVE)) {
			Direction direction = (Direction) gson.fromJson(
					obj.getExtraMessage(), Direction.class);

			this.gameServer.move(obj.getMessage(), direction);
		}
	}

	@Override
	public void updateScreen(ArrayList<Drawing> drawings) {
		Gson gson = new Gson();
		String innerJson = gson.toJson(drawings);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, innerJson);

		String json = gson.toJson(object);
		broadcast(json);
	}

	@Override
	public void init(Integer lines, Integer cols, ArrayList<Drawing> drawings) {
		HashMap<String, Integer> extraMessage = new HashMap<String, Integer>();
		extraMessage.put("lines", lines);
		extraMessage.put("cols", cols);

		Gson gson = new Gson();
		String messageJson = gson.toJson(drawings);
		String extraMessageJson = gson.toJson(extraMessage);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.INIT, messageJson, extraMessageJson);

		String json = gson.toJson(object);
		broadcast(json);
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		Gson gson = new Gson();
		String innerJson = gson.toJson(players);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_PLAYERS, innerJson);

		String json = gson.toJson(object);

		broadcast(json);
	}

	private void broadcast(String object) {
		for (Iterator<ICommunicationChannel> commChannel = commChannels
				.iterator(); commChannel.hasNext();) {
			commChannel.next().send(object);
		}
	}

}
