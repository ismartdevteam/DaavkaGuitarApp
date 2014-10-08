package mn.skyware.view;

import java.io.IOException;
import java.io.InputStream;

import mn.skyware.daavkaguitar.R;
import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ImageView.ScaleType;

import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;

public class MyImageView extends Activity {
	private PhotoView	imgDisplay;
	private Bundle b;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cv);
		b=getIntent().getExtras();
		imgDisplay = (PhotoView) findViewById(R.id.magazine_adap_item);
		imgDisplay.setScaleType(ScaleType.CENTER_CROP);
		AssetManager assetManager = getAssets();
		InputStream is = null;
		try {
			is = assetManager.open(b.getString("image"));
			TileBitmapDrawable.attachTileBitmapDrawable(imgDisplay, is, null,
					null);
//			WindowManager w = getWindowManager();
//			Display d = w.getDefaultDisplay();
//			DisplayMetrics metrics = new DisplayMetrics();
//			d.getMetrics(metrics);
//			// since SDK_INT = 1;
//			float widthPixels = metrics.widthPixels;
//			imgDisplay.setScale(widthPixels);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
