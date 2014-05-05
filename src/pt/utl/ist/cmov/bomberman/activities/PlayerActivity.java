package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.controllers.CommunicationController;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PlayerActivity extends FullScreenActivity {

	private static final String TAG = PlayerActivity.class.getSimpleName();

	private static Context context;

	private MainGamePanel gamePanel;

	private CommunicationController commController;

	private Channel mChannel;

	private WifiP2pManager mManager;

	private WifiP2pDevice mMainDevice;

	private IntentFilter mIntentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_main);

		WifiP2pDevice peer = getIntent().getExtras().getParcelable(
				PeerChoiceActivity.PEER_MESSAGE);

		this.mMainDevice = peer;

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		commController = new CommunicationController(mManager, mChannel, this);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

		commController.connect(peer);
	}

	public void printConnectionInfo(WifiP2pInfo info) {
		Log.e("RTFPESSOA", info.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(commController, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(commController);
	}

	public void upClick(View view) {
		Toast.makeText(this, "CLICK UP", Toast.LENGTH_SHORT).show();
	}

	public void downClick(View view) {
		Toast.makeText(this, "CLICK DOWN", Toast.LENGTH_SHORT).show();
	}

	public void rightClick(View view) {
		Toast.makeText(this, "CLICK RIGHT", Toast.LENGTH_SHORT).show();
	}

	public void leftClick(View view) {
		Toast.makeText(this, "CLICK LEFT", Toast.LENGTH_SHORT).show();
	}

	public void bombClick(View view) {
		Toast.makeText(this, "CLICK BOMB", Toast.LENGTH_SHORT).show();
	}
}
