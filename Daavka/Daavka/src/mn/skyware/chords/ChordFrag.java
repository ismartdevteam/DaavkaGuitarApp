package mn.skyware.chords;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mn.skyware.daavkaguitar.R;
import mn.skyware.database.Chord;
import mn.skyware.database.DatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChordFrag extends Fragment {
	private View			v;
	private ListView		chord;
	private ListView		subChord;
	private ListView		chordImg;
	private List<String>	chordList;
	private List<Chord>		subChordList;
	private List<String>	selSubChords;
	private TextView		header;
	private DatabaseHelper	helper;
	private SubChordAdapter	subChordAdapter;
	private String			chordStr;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		helper = new DatabaseHelper(getActivity());
		selSubChords = new ArrayList<String>();
		chord.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		chordList = Arrays.asList(getActivity().getResources().getStringArray(
				R.array.chord));
		chord.setAdapter(new ChordAdapter(getActivity(), chordList));
		chord.setItemChecked(0, true);

		chordStr = chordList.get(0);
		setChordName(chordStr);
		try {
			subChordList = helper.getChordDao().queryForEq("name",
					chordList.get(0));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		subChord.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		subChordAdapter = new SubChordAdapter(getActivity(),
				Arrays.asList(subChordList.get(0).stat.split(",")));
		subChord.setAdapter(subChordAdapter);
		chord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				try {
					chordStr = chordList.get(arg2);
					setChordName(chordStr);
					subChordList = helper.getChordDao().queryForEq("name",
							chordStr);

					selSubChords = Arrays.asList(subChordList.get(0).stat
							.split(","));
					subChordAdapter = new SubChordAdapter(getActivity(),
							selSubChords);
					subChord.setAdapter(subChordAdapter);
					chordImg.setAdapter(null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		subChord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String[] list = subChordList.get(0).stat.split(",");
				String subChord = list[pos];
				setChordName(chordStr + " " + subChord);
				List<String> path = new ArrayList<String>();
				try {
					String[] as = getActivity().getAssets().list(
							"chord" + File.separator + chordStr);
					// Pattern pattern =
					// Pattern.compile(chordStr+" "+subChord+"(\\D.+)[$.jpg]");
					for (int i = 0; i < as.length; i++) {
						if (subChord.contains("+"))
							subChord = subChord.replace("+", "[+]");

						if (as[i].matches(chordStr + " " + subChord.trim()
								+ "([^7a596]+)[$.jpg]")) {
							Log.e("name of images", as[i]);
							path.add(as[i]);
						}
					}
					chordImg.setAdapter(new ImageAdapter(getActivity(), path));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void setChordName(String name) {
		header.setText(name + "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.chord_main, null);
		chord = (ListView) v.findViewById(R.id.chordList);
		subChord = (ListView) v.findViewById(R.id.chordSubList);
		chordImg = (ListView) v.findViewById(R.id.chordImage);
		header = (TextView) v.findViewById(R.id.chord_name);
		return v;
	}

	private class ChordAdapter extends ArrayAdapter<String> {
		List<String>	obj;
		Context			context;

		public ChordAdapter(Context context, List<String> objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
			this.obj = objects;
			this.context = context;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			final String item = obj.get(position);
			Holder hol;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.chord_item, null);
				hol = new Holder();
				hol.chord = (TextView) v.findViewById(R.id.chord_item);
				v.setTag(hol);
			} else
				hol = (Holder) v.getTag();
			hol.chord.setText(item + "");
			return v;
		}
	}

	class Holder {
		TextView	chord;
	}

	private class SubChordAdapter extends ArrayAdapter<String> {
		List<String>	ob;

		public SubChordAdapter(Context context, List<String> objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
			this.ob = objects;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			String item = ob.get(position);
			SubHolder hol;
			if (v == null) {
				v = getActivity().getLayoutInflater().inflate(
						R.layout.chord_item, null);
				hol = new SubHolder();
				hol.chord = (TextView) v.findViewById(R.id.chord_item);
				v.setTag(hol);
			} else
				hol = (SubHolder) v.getTag();
			hol.chord.setText(item + "");
			return v;
		}
	}

	class SubHolder {
		TextView	chord;
	}

	private class ImageAdapter extends ArrayAdapter<String> {

		public ImageAdapter(Context context, List<String> objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			final String item = getItem(position);
			ImgHolder hol;
			if (v == null) {
				v = getActivity().getLayoutInflater().inflate(
						R.layout.chord_image, null);
				hol = new ImgHolder();
				hol.image = (ImageView) v.findViewById(R.id.chord_img_item);
				v.setTag(hol);
			} else
				hol = (ImgHolder) v.getTag();

			final float scale = getResources().getDisplayMetrics().widthPixels;
			int pixels = (int) (scale / 2.2 + 0.5f);
			hol.image.setLayoutParams(new LinearLayout.LayoutParams(pixels,
					pixels));
			hol.image.setImageBitmap(getBitmapFromAsset(chordStr + "/" + item));
			hol.image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					v.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), R.anim.touch_up));
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							ChordDet det = new ChordDet();
							Bundle b = new Bundle();
							b.putString("path", "chord/" + chordStr + "/"
									+ item);
							det.setArguments(b);
							det.show(getActivity().getSupportFragmentManager()
									.beginTransaction(), "chord");
						}
					}, 250);

				}
			});
			return v;
		}
	}

	class ImgHolder {
		ImageView	image;
	}

	private Bitmap getBitmapFromAsset(String strName) {
		AssetManager assetManager = getActivity().getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open("chord/" + strName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

}
