package pt.utl.ist.cmov.bomberman.activities;

import java.io.IOException;
import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.handlers.ServerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.channels.FakeCommunicationChannel;
import pt.utl.ist.cmov.bomberman.handlers.managers.ServerCommunicationManager;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class ServerActivity extends GameActivity {

	private static Context context;

	private GameServer gameServer;
	private ServerCommunicationManager serverManager;

	private Boolean hasStartedServer;

	private Boolean connectToAll;
	private WifiP2pDevice previousMaster;

	private Handler timerHandler;

	private Runnable timerRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		this.hasStartedServer = false;

		this.wifiDirectController.startGroup();

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		connectToAll = getIntent().getExtras().getBoolean(
				GameActivity.CONNECT_TO_ALL);
		previousMaster = getIntent().getExtras().getParcelable(
				GameActivity.PREVIOUS_SERVER);

		Integer width = getIntent().getExtras().getInt(GameActivity.WIDTH);
		Integer height = getIntent().getExtras().getInt(GameActivity.HEIGHT);
		ArrayList<ModelDTO> models = getIntent().getExtras().getParcelable(
				GameActivity.MODELS);

		Level level;
		if (models != null && models.size() > 0) {
			level = LevelManager.loadLevel(context, context.getAssets(),
					levelName, height, width, models);
		} else {
			level = LevelManager.loadLevel(context, context.getAssets(),
					levelName);
		}

		this.gameServer = new GameServer(this, level);

		this.serverManager = new ServerCommunicationManager(this.gameServer);

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

		/* Connect to previous players if any, only after all setup */
		this.wifiDirectController.discoverPeers();
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

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		if (connectToAll && !peers.getDeviceList().isEmpty()) {
			for (WifiP2pDevice device : peers.getDeviceList()) {
				if (previousMaster == null
						|| !previousMaster.deviceAddress
								.equals(device.deviceAddress)) {
					this.wifiDirectController.connect(device);
				}
			}

			connectToAll = false;
		}
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
