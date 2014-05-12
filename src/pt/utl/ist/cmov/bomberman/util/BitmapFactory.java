package pt.utl.ist.cmov.bomberman.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

public class BitmapFactory {

	private static BitmapFactory instance = null;

	private final Context context;
	private final HashMap<String, Bitmap> bitmaps;

	protected BitmapFactory(Context context) {
		this.context = context;
		this.bitmaps = new HashMap<String, Bitmap>();
	}

	public static BitmapFactory getInstance(Context context) {
		if (instance == null) {
			instance = new BitmapFactory(context);
		}
		return instance;
	}

	public Bitmap getBitmapFromAsset(String strName) {
		Bitmap bitmap = bitmaps.get(strName);

		if (bitmap == null) {
			bitmap = loadBitmapFromAsset(strName);
			bitmaps.put(strName, bitmap);
		}

		return bitmap;
	}

	private Bitmap loadBitmapFromAsset(String strName) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = android.graphics.BitmapFactory.decodeStream(istr);
			bitmap = Bitmap.createScaledBitmap(bitmap,
					MapMeasurements.POSITION_WIDTH,
					MapMeasurements.POSITION_HEIGHT, true);
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			Log.e("BOMBERMAN", "Could not load «" + strName + "«");
		}

		return bitmap;
	}

}
