package pt.utl.ist.cmov.bomberman.network.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.IGameClient;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.network.CommunicationObject;
import pt.utl.ist.cmov.bomberman.network.channel.ICommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

import com.google.gson.Gson;

public class ServerCommunicationProxy implements ICommunicationProxy,
		IGameClient {

	private List<ICommunicationChannel> commChannels;
	private GameServer gameServer;

	private Gson gson = new Gson();

	public ServerCommunicationProxy(GameServer gameServer) {
		this.commChannels = Collections
				.synchronizedList(new ArrayList<ICommunicationChannel>());
		this.gameServer = gameServer;
	}

	public void addCommChannel(ICommunicationChannel commChannel) {
		this.commChannels.add(commChannel);
		this.gameServer.initClient();
	}

	@Override
	public void receive(Object object) {
		Gson gson = new Gson();

		CommunicationObject obj = (CommunicationObject) object;

		if (obj.getType().equals(CommunicationObject.MOVE)) {
			Direction direction = (Direction) gson.fromJson(
					obj.getExtraMessage(), Direction.class);

			this.gameServer.move(obj.getMessage(), direction);
		} else if (obj.getType().equals(CommunicationObject.PUT_BOMB)) {
			this.gameServer.putBomb(obj.getMessage());
		} else if (obj.getType().equals(CommunicationObject.PUT_BOMBERMAN)) {
			this.gameServer.putBomberman(obj.getMessage());
		} else if (obj.getType().equals(CommunicationObject.PAUSE)) {
			this.gameServer.pause(obj.getMessage());
		} else if (obj.getType().equals(CommunicationObject.QUIT)) {
			this.gameServer.quit(obj.getMessage());
		} else if (obj.getType().equals(CommunicationObject.DEBUG)) {
			Log.i("CommunicationManager", obj.getMessage());
		}
	}

	@Override
	public void updateScreen(ArrayList<ModelDTO> models) {
		String innerJson = gson.toJson(models);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, innerJson);

		broadcast(object);
	}

	@Override
	public void init(Integer lines, Integer cols, ArrayList<ModelDTO> models) {
		HashMap<String, Integer> extraMessage = new HashMap<String, Integer>();
		extraMessage.put("lines", lines);
		extraMessage.put("cols", cols);

		String messageJson = gson.toJson(models);
		String extraMessageJson = gson.toJson(extraMessage);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.INIT, messageJson, extraMessageJson);

		broadcast(object);
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		String innerJson = gson.toJson(players);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_PLAYERS, innerJson);

		broadcast(object);
	}

	@Override
	public void startServer(String username, String levelName, Integer width,
			Integer height, ArrayList<ModelDTO> models,
			ArrayList<WifiP2pDevice> players) {
		HashMap<String, String> extraMessage = new HashMap<String, String>();
		extraMessage.put("username", username);
		extraMessage.put("levelName", levelName);
		extraMessage.put("width", width.toString());
		extraMessage.put("height", height.toString());

		String messageJson = gson.toJson(models);
		String extraMessageJson = gson.toJson(extraMessage);
		String objJson = gson.toJson(players);

		CommunicationObject object = new CommunicationObject(
				CommunicationObject.START_SERVER, messageJson,
				extraMessageJson, objJson);

		broadcast(object);
	}

	@Override
	public void confirmQuit(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.CONFIRM_QUIT, username);

		broadcast(object);
	}

	private void broadcast(Object object) {
		synchronized (commChannels) {
			for (ICommunicationChannel commChannel : commChannels) {
				try {
					commChannel.send(object);
				} catch (Throwable t) {
					for (int i = 0; i < commChannels.size(); i++) {
						if (commChannel.equals(commChannels.get(i))) {
							commChannel.close();
							commChannels.remove(i);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void close() {
		synchronized (commChannels) {
			for (ICommunicationChannel commChannel : commChannels) {
				commChannel.close();
			}
		}
	}

	@Override
	public ArrayList<String> getChannelEndpoints() {
		ArrayList<String> channelAddresses = new ArrayList<String>();

		synchronized (commChannels) {
			for (Iterator<ICommunicationChannel> commChannel = commChannels
					.iterator(); commChannel.hasNext();) {
				String address = commChannel.next().getChannelEndpoint();
				if (address != null) {
					channelAddresses.add(address);
				}
			}
		}

		return channelAddresses;
	}

}
