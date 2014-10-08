package mn.skyware.chords;

import java.io.IOException;
import java.io.InputStream;

import mn.skyware.daavkaguitar.R;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChordDet extends DialogFragment implements OnClickListener {
	private View		v;
	private Button		mCancel;
	private ImageView	chord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_No_Border);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		String path = getArguments().getString("path");
		chord.setImageBitmap(getBitmapFromAsset(path));
	}

	private Bitmap getBitmapFromAsset(String strName) {
		Log.i("str path", strName + "");
		AssetManager assetManager = getActivity().getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open(strName);

		} catch (IOException e) {
			Toast.makeText(getActivity(), "Таарах аккорд олдсонгүй",
					Toast.LENGTH_SHORT).show();
			getDialog().dismiss();
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.chord_det, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chord = (ImageView) v.findViewById(R.id.chord_img);
		mCancel = (Button) v.findViewById(R.id.pop_dismiss_chord);
		mCancel.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.pop_dismiss_chord:
			getDialog().dismiss();
			break;

		}
	}
}
