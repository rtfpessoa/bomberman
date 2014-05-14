package pt.utl.ist.cmov.bomberman.activities;

import java.io.IOException;
import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.network.channel.FakeCommunicationChannel;
import pt.utl.ist.cmov.bomberman.network.handler.ServerSocketHandler;
import pt.utl.ist.cmov.bomberman.network.proxy.ServerCommunicationProxy;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class ServerActivity extends GameActivity {

	private GameServer gameServer;
	private ServerCommunicationProxy serverManager;

	private Boolean hasStartedServer;

	private ArrayList<WifiP2pDevice> currentPlayers;

	private Handler timerHandler;

	private Runnable timerRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.hasStartedServer = false;

		this.wifiDirectController.startGroup();

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		currentPlayers = getIntent().getExtras().getParcelableArrayList(
				GameActivity.CURRENT_DEVICES);

		Integer width = getIntent().getExtras().getInt(GameActivity.WIDTH);
		Integer height = getIntent().getExtras().getInt(GameActivity.HEIGHT);
		ArrayList<ModelDTO> models = getIntent().getExtras()
				.getParcelableArrayList(GameActivity.MODELS);
		ArrayList<BombermanPlayer> players = getIntent().getExtras()
				.getParcelableArrayList(GameActivity.PLAYERS);

		Level level;
		if (models != null && models.size() > 0) {
			level = LevelManager.loadLevel(this, getAssets(), levelName,
					height, width, models);
		} else {
			level = LevelManager.loadLevel(this, getAssets(), levelName);
		}

		this.gameServer = new GameServer(this, level, players);

		this.serverManager = new ServerCommunicationProxy(this.gameServer);

		gameServer.setGameClient(serverManager);

		clientManager
				.setCommChannel(new FakeCommunicationChannel(serverManager));
		serverManager
				.addCommChannel(new FakeCommunicationChannel(clientManager));

		this.timerHandler = new Handler();
		this.timerRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					gameServer.decrementTime();
				} catch (NullPointerException ignored) {
				}

				timerHandler.postDelayed(timerRunnable, 1000);
			}
		};
		this.timerRunnable.run();

		connectToPlayers();
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (!hasStartedServer && p2pInfo.isGroupOwner) {
			Log.i("BOMBERMAN", "Connected as GroupOwner");
			try {
				handler = new ServerSocketHandler(this.serverManager);
				handler.start();
				hasStartedServer = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!p2pInfo.isGroupOwner) {
			Log.e("BOMBERMAN", "This device must be the groupd owner!");
		} else {
			Log.i("BOMBERMAN", "This device already has a server!");
		}
	}

	private void connectToPlayers() {
		if (currentPlayers == null) {
			return;
		}

		for (WifiP2pDevice device : currentPlayers) {
			this.wifiDirectController.connect(device);
		}

		currentPlayers = null;
	}

	public ArrayList<WifiP2pDevice> getDevices() {
		return this.wifiDirectController.getGroupDevices();
	}

	public void updateDevices() {
		this.wifiDirectController.requestGroupInfo();
	}

	protected void stopAll() {
		super.stopAll();
		this.gameServer.stopAll();
		this.serverManager.close();
	}
}
