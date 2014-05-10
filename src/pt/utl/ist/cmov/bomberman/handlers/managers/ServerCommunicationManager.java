package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.IGameClient;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;
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
	}

	@Override
	public void updateScreen(List<String> drawings) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.UPDATE_SCREEN, drawings);

		for (Iterator<ICommunicationChannel> commChannel = commChannels
				.iterator(); commChannel.hasNext();) {
			commChannel.next().send(object);
		}
	}

	@Override
	public void init(List<List<Element>> elements) {
		CommunicationObject object = new CommunicationObject(
				CommunicationObject.INIT, elements);

		for (Iterator<ICommunicationChannel> commChannel = commChannels
				.iterator(); commChannel.hasNext();) {
			commChannel.next().send(object);
		}
	}

}
