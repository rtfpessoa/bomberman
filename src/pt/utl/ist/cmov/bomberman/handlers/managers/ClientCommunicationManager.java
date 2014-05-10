package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
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
		gameClient.putBomberman();
	}

	@Override
	public void receive(String object) {
		Gson gson = new Gson();

		String[] parts = object.split("±");

		CommunicationObject obj = gson.fromJson(parts[1],
				CommunicationObject.class);

		if (obj.getType().equals(CommunicationObject.DEBUG)) {
			Log.i("CommunicationManager", (String) obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.UPDATE_SCREEN)) {
			Type collectionType = new TypeToken<Collection<Drawing>>(){}.getType();

			ArrayList<Drawing> drawings = (ArrayList<Drawing>) gson.fromJson(obj.getMessage(),
					collectionType);
			this.gameClient.updateScreen(drawings);
		}
		if (obj.getType() == CommunicationObject.UPDATE_PLAYERS) {
//			HashMap<String, BombermanPlayer> players = (HashMap<String, BombermanPlayer>) obj
//					.getMessage();
//			this.gameClient.updatePlayers(players);
		}
		if (obj.getType() == CommunicationObject.INIT) {
//			HashMap<String, Object> message = (HashMap<String, Object>) obj
//					.getMessage();
//
//			this.gameClient.init((Integer) message.get("lines"),
//					(Integer) message.get("cols"),
//					(ArrayList<Drawing>) message.get("drawings"));
		}
	}

	@Override
	public void addCommChannel(ICommunicationChannel commChannel) {
		setCommChannel(commChannel);
	}

	@Override
	public void putBomberman(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMBERMAN, username);

		Gson gson = new Gson();
		String json = gson.toJson(object);
;;;
		commChannel.send(CommunicationObject.PUT_BOMBERMAN + "±" + json);
	}

	@Override
	public void putBomb(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMB, username);

		Gson gson = new Gson();
		String json = gson.toJson(object);

		commChannel.send(CommunicationObject.PUT_BOMB + "±" + json);
	}

	@Override
	public void move(String username, Direction direction) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("direction", direction);

		Gson gson = new Gson();
		String innerJson = gson.toJson(params);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.MOVE, innerJson);

		String json = gson.toJson(object);

		commChannel.send(CommunicationObject.MOVE + "±" + json);
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
