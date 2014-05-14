package pt.utl.ist.cmov.bomberman.network.proxy;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.network.CommunicationObject;
import pt.utl.ist.cmov.bomberman.network.channel.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClientCommunicationProxy implements ICommunicationProxy,
		IGameServer {

	private ICommunicationChannel commChannel;
	private GameClient gameClient;

	private Gson gson = new Gson();
	Type modelDTOType = new TypeToken<Collection<ModelDTO>>() {
	}.getType();
	Type bombermanPlayerType = new TypeToken<HashMap<String, BombermanPlayer>>() {
	}.getType();
	Type integerMapType = new TypeToken<HashMap<String, Integer>>() {
	}.getType();
	Type stringMapType = new TypeToken<HashMap<String, String>>() {
	}.getType();
	Type devicesType = new TypeToken<ArrayList<WifiP2pDevice>>() {
	}.getType();

	public ClientCommunicationProxy(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public void setCommChannel(ICommunicationChannel commChannel) {
		close();
		this.commChannel = commChannel;
		gameClient.putBomberman();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void receive(Object object) {

		CommunicationObject obj = (CommunicationObject) object;

		if (obj.getType().equals(CommunicationObject.UPDATE_SCREEN)) {
			ArrayList<ModelDTO> models = (ArrayList<ModelDTO>) gson.fromJson(
					obj.getMessage(), modelDTOType);
			this.gameClient.updateScreen(models);
		} else if (obj.getType().equals(CommunicationObject.UPDATE_PLAYERS)) {
			HashMap<String, BombermanPlayer> players = (HashMap<String, BombermanPlayer>) gson
					.fromJson(obj.getMessage(), bombermanPlayerType);
			this.gameClient.updatePlayers(players);
		} else if (obj.getType().equals(CommunicationObject.INIT)) {
			ArrayList<ModelDTO> models = (ArrayList<ModelDTO>) gson.fromJson(
					obj.getMessage(), modelDTOType);

			HashMap<String, Integer> mesurements = (HashMap<String, Integer>) gson
					.fromJson(obj.getExtraMessage(), integerMapType);

			this.gameClient.init(mesurements.get("lines"),
					mesurements.get("cols"), models);
		} else if (obj.getType().equals(CommunicationObject.START_SERVER)) {
			ArrayList<ModelDTO> models = (ArrayList<ModelDTO>) gson.fromJson(
					obj.getMessage(), modelDTOType);

			HashMap<String, String> values = (HashMap<String, String>) gson
					.fromJson(obj.getExtraMessage(), stringMapType);

			ArrayList<WifiP2pDevice> players = (ArrayList<WifiP2pDevice>) gson
					.fromJson(obj.getObject(), devicesType);

			this.gameClient.startServer(values.get("username"),
					values.get("levelName"),
					Integer.parseInt(values.get("width")),
					Integer.parseInt(values.get("height")), models, players);
		} else if (obj.getType().equals(CommunicationObject.CONFIRM_QUIT)) {
			this.gameClient.confirmQuit(obj.getMessage());
		} else if (obj.getType().equals(CommunicationObject.DEBUG)) {
			Log.i("CommunicationManager", obj.getMessage());
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

		send(object);
	}

	@Override
	public void putBomb(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMB, username);

		send(object);
	}

	@Override
	public void move(String username, Direction direction) {
		String extraMessageJson = gson.toJson(direction);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.MOVE, username, extraMessageJson);

		send(object);
	}

	@Override
	public void pause(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PAUSE, username);

		send(object);
	}

	@Override
	public void quit(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.QUIT, username);

		send(object);
	}

	@Override
	public void close() {
		if (this.commChannel != null) {
			this.commChannel.close();
			this.commChannel = null;
		}
	}

	@Override
	public ArrayList<String> getChannelEndpoints() {
		ArrayList<String> channelAddresses = new ArrayList<String>();
		channelAddresses.add(commChannel.getChannelEndpoint());
		return channelAddresses;
	}

	private void send(Object object) {
		if (commChannel != null) {
			try {
				commChannel.send(object);
			} catch (Throwable t) {
				close();
			}
		}
	}
}
