package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.handlers.PlayerSocketHandler;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.util.Log;

public class PlayerActivity extends GameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.wifiP2pGroupOwner = getIntent().getExtras().getParcelable(
				GameDiscoveryActivity.DEVICE_MESSAGE);
		this.wifiDirectController.connect(wifiP2pGroupOwner);
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (!p2pInfo.isGroupOwner) {
			Log.i("BOMBERMAN", "Connected as peer");
			handler = new PlayerSocketHandler(this.clientManager,
					p2pInfo.groupOwnerAddress);
			handler.start();
		} else {
			Log.e("BOMBERMAN", "This device should not be the groupd owner!");
		}
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// INFO: this is not needed
	}

	@Override
	public void startNewServer(Integer width, Integer height, ArrayList<ModelDTO> models) {
		Intent intent = new Intent(this, ServerActivity.class);
		intent.putExtra(GameActivity.CONNECT_TO_ALL, true);
		intent.putExtra(GameActivity.PREVIOUS_SERVER, this.wifiP2pGroupOwner);
		intent.putExtra(GameActivity.WIDTH, width);
		intent.putExtra(GameActivity.HEIGHT, height);
		intent.putExtra(GameActivity.MODELS, models);
		finish();
		startActivity(intent);
	}
}
