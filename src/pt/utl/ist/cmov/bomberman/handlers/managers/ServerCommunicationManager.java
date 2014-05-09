package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.List;

import android.util.Log;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.IGameClient;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;

public class ServerCommunicationManager implements ICommunicationManager,
		IGameClient {

	private ICommunicationChannel commChannel;
	private GameServer gameServer;

	public ServerCommunicationManager(ICommunicationChannel commChannel,
			GameServer gameServer) {
		this.commChannel = commChannel;
		this.gameServer = gameServer;
	}

	@Override
	public void deliver(CommunicationObject object) {
		if (object.getType() == CommunicationObject.DEBUG) {
			Log.d("CommunicationManager", (String) object.getMessage());
		}
		if (object.getType() == CommunicationObject.PUT_BOMBERMAN) {
			this.gameServer.putBomberman((String) object.getMessage());
		}
	}

	@Override
	public void updateScreen(List<String> drawings) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, drawings);
		commChannel.send(object);
	}

}
