package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.List;

import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
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
			List<String> drawings = (List<String>) object.getMessage();
			// this.gameClient.updateScreen(drawings);
		}
	}

	@Override
	public void putBomberman(String username) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.PUT_BOMBERMAN, username);
		commChannel.send(object);
	}

}
