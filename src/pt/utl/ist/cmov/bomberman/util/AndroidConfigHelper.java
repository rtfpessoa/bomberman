package pt.utl.ist.cmov.bomberman.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class AndroidConfigHelper {
	public static void enableWifi(Activity activity) {
		WifiManager wifi = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
	}

	public static void enableWifiDirect(WifiP2pManager manager, Channel channel) {
		try {
			Method enableWifiDirect = manager.getClass().getMethod("enableP2p",
					Channel.class);
			enableWifiDirect.invoke(manager, channel);
		} catch (Exception e) {

		}
	}
}
