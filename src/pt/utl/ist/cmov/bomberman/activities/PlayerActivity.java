package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.network.handler.PlayerSocketHandler;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
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
	public void startNewServer(String levelName, Integer width, Integer height,
			ArrayList<ModelDTO> models, ArrayList<WifiP2pDevice> devices) {
		Intent intent = new Intent(this, ServerActivity.class);
		intent.putParcelableArrayListExtra(GameActivity.CURRENT_PLAYERS,
				devices);
		intent.putExtra(LevelChoiceActivity.LEVEL_MESSAGE, levelName);
		intent.putExtra(GameActivity.WIDTH, width);
		intent.putExtra(GameActivity.HEIGHT, height);
		intent.putParcelableArrayListExtra(GameActivity.MODELS, models);
		finish();
		startActivity(intent);
	}
}
