package pt.utl.ist.cmov.bomberman.activities.adapters;

import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameAdapter extends ArrayAdapter<WifiP2pDevice> {
	private final Context context;
	private final List<WifiP2pDevice> values;

	public GameAdapter(Context context, List<WifiP2pDevice> values) {
		super(context, R.layout.listview_line, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.listview_line, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.line_text);

		textView.setText(values.get(position).deviceName);

		return rowView;
	}
}
