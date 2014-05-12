package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	public void receive(Object object) {
		Gson gson = new Gson();

		CommunicationObject obj = (CommunicationObject) object;

		if (obj.getType().equals(CommunicationObject.DEBUG)) {
			Log.i("CommunicationManager", obj.getMessage());
		}
		if (obj.getType().equals(CommunicationObject.UPDATE_SCREEN)) {
			Type collectionType = new TypeToken<Collection<Drawing>>() {
			}.getType();

			ArrayList<Drawing> drawings = (ArrayList<Drawing>) gson.fromJson(
					obj.getMessage(), collectionType);
			this.gameClient.updateScreen(drawings);
		}
		if (obj.getType().equals(CommunicationObject.UPDATE_PLAYERS)) {
			Type collectionType = new TypeToken<HashMap<String, BombermanPlayer>>() {
			}.getType();

			HashMap<String, BombermanPlayer> players = (HashMap<String, BombermanPlayer>) gson
					.fromJson(obj.getMessage(), collectionType);
			this.gameClient.updatePlayers(players);
		}
		if (obj.getType().equals(CommunicationObject.INIT)) {
			Type hashType = new TypeToken<HashMap<String, Integer>>() {
			}.getType();

			Type collectionType = new TypeToken<ArrayList<Drawing>>() {
			}.getType();

			ArrayList<Drawing> drawings = (ArrayList<Drawing>) gson.fromJson(
					obj.getMessage(), collectionType);

			HashMap<String, Integer> mesurements = (HashMap<String, Integer>) gson
					.fromJson(obj.getExtraMessage(), hashType);

			this.gameClient.init(mesurements.get("lines"),
					mesurements.get("cols"), drawings);
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

		commChannel.send(object);
	}

	@Override
	public void putBomb(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMB, username);

		commChannel.send(object);
	}

	@Override
	public void move(String username, Direction direction) {
		Gson gson = new Gson();
		String extraMessageJson = gson.toJson(direction);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.MOVE, username, extraMessageJson);

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

	@Override
	public void close() {
		this.commChannel.close();
	}

	@Override
	public ArrayList<String> getChannelEndpoints() {
		ArrayList<String> channelAddresses = new ArrayList<String>();
		channelAddresses.add(commChannel.getChannelEndpoint());
		return channelAddresses;
	}
}
