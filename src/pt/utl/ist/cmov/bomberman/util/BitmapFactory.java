package pt.utl.ist.cmov.bomberman.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class BitmapFactory {

	public static Bitmap getBitmapFromAsset(Context context, String strName) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = android.graphics.BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			return null;
		}

		return bitmap;
	}

}
