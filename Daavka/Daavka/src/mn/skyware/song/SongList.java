package mn.skyware.song;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mn.skyware.daavkaguitar.R;
import mn.skyware.database.Artist;
import mn.skyware.database.DatabaseHelper;
import mn.skyware.database.Song;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SongList extends Fragment implements OnClickListener {
	private View				v;
	private TextView			sortChar;
	private ExpandableListView	songLV;
	private DatabaseHelper		helper;
	private ExAdapter		adapter;
	private List<Artist>	artists;
	private List<String>	artistName;
	private AlertDialog.Builder	choiceName;
	private int					selected	= 0;
	private ImageView			favSort;
	public static boolean isBack=false;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isBack){
			try {
				artists=helper.getArtDao().queryForAll();
				adapter.notifyDataSetChanged();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		helper = new DatabaseHelper(getActivity());
		choiceName = new AlertDialog.Builder(getActivity());
		try {

			artists = helper.getArtDao().queryBuilder()
					.orderByRaw("Name COLLATE NOCASE").query();
			adapter = new ExAdapter(getActivity(), artists, false);
			artistName = new ArrayList<String>();
			artistName.add(0, "Бүгд");
			sortChar.setText(artistName.get(0));
			songLV.setAdapter(adapter);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				songLV.expandGroup(i);
				artistName.add(artists.get(i).name + "");
			}
			songLV.setGroupIndicator(null);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void CreateDialog() {

		choiceName.setTitle(getString(R.string.charac));
		String[] array = new String[artistName.size()];
		artistName.toArray(array);
		choiceName.setSingleChoiceItems(array, selected,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						selected = which;
					}

				});
		choiceName.setPositiveButton("Болих",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		choiceName.setNegativeButton("Сонгох",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try {
							sortArtist(selected);
							sortChar.setText(artistName.get(selected) + "");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		choiceName.create();
		choiceName.show();
	}

	private void sortArtist(int pos) throws SQLException {
		Log.e("pos", pos + "");
		if (pos == 0) {
			artists = helper.getArtDao().queryBuilder()
					.orderByRaw("Name COLLATE NOCASE").query();
		} else
			artists = helper.getArtDao()
					.queryForEq("name", artistName.get(pos));
		adapter = new ExAdapter(getActivity(), artists, false);
		songLV.setAdapter(adapter);
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			songLV.expandGroup(i);
		}
		songLV.setGroupIndicator(null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.song_list, null);
		songLV = (ExpandableListView) v.findViewById(R.id.songList);
		sortChar = (TextView) v.findViewById(R.id.sort_char);
		favSort = (ImageView) v.findViewById(R.id.favsort);
		favSort.setOnClickListener(this);
		sortChar.setOnClickListener(this);
		// v.setOnClickListener(this);
		return v;
	}

	class ExAdapter extends BaseExpandableListAdapter {
		Context			mContext;
		List<Artist>	art;
		boolean			isfav	= false;

		public ExAdapter(Context con, List<Artist> objects, boolean isFav) {
			this.mContext = con;
			this.art = objects;
			this.isfav = isFav;
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			List<Song> song = null;
			Artist id = (Artist) getGroup(arg0);
			try {
				if (!isfav)
					song = helper.getSongDao().queryForEq("artist_id", id.id);
				else {
					song = helper.getSongDao().queryBuilder().where()
							.eq("artist_id", id.id).and().eq("isfav", true)
							.query();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return song.get(arg1);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(final int arg0, int arg1, boolean arg2,
				View songView, ViewGroup arg4) {
			// TODO Auto-generated method stub
			final Song song = (Song) getChild(arg0, arg1);
			SubHolder subHolder = null;
			if (songView == null) {
				subHolder = new SubHolder();
				songView = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.song_list_song, null);
				subHolder.name = (TextView) songView
						.findViewById(R.id.song_name);
				subHolder.fav = (ImageView) songView
						.findViewById(R.id.song_fav);
				subHolder.lvl = (ImageView) songView
						.findViewById(R.id.song_lvl);
				songView.setTag(subHolder);
			} else {
				subHolder = (SubHolder) songView.getTag();
			}
			subHolder.name.setText(song.name);

			if (song.isfav)
				subHolder.fav.setImageResource(R.drawable.fav);
			else
				subHolder.fav.setImageResource(R.drawable.favwhite);

			switch (song.level) {
			case 1:
				subHolder.lvl.setImageResource(R.drawable.level1);
				break;
			case 2:
				subHolder.lvl.setImageResource(R.drawable.level2);
				break;
			case 3:
				subHolder.lvl.setImageResource(R.drawable.level3);
				break;
			default:
				break;
			}
			songView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent det = new Intent(getActivity(), SongDet.class);
					Bundle extra = new Bundle();
					Artist artist = (Artist) getGroup(arg0);
					extra.putInt("id", song.id);
					extra.putString("art", artist.name + "");
					det.putExtras(extra);
					startActivity(det);
				}
			});
			return songView;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			List<Song> child = null;
			Artist id = (Artist) getGroup(arg0);
			try {
				if (!isfav)
					child = helper.getSongDao().queryForEq("artist_id", id.id);
				else
					child = helper.getSongDao().queryBuilder().where()
							.eq("artist_id", id.id).and().eq("isfav", true)
							.query();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return child.size();
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return art.get(arg0);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return art.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(int position, boolean arg1, View v,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			Artist artist = (Artist) getGroup(position);
			ViewHolder holder = null;

			if (v == null) {
				v = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.song_list_item, null);
				holder = new ViewHolder();
				holder.image = (ImageView) v.findViewById(R.id.art_image);
				holder.name = (TextView) v.findViewById(R.id.art_name);

				v.setTag(holder);
			} else
				holder = (ViewHolder) v.getTag();
			holder.image.setImageBitmap(getBitmapFromAsset(artist.id + ".png"));
			holder.name.setText(artist.name);
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
			return v;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		class ViewHolder {
			TextView	name;
			ImageView	image;

		}

		class SubHolder {
			TextView	name;
			ImageView	lvl;
			ImageView	fav;
		}

	}

	// private class ArtAdapter extends ArrayAdapter<Artist> {
	// Context mContext;
	//
	// public ArtAdapter(Context context, List<Artist> objects) {
	// super(context, 0, 0, objects);
	// this.mContext = context;
	// // TODO Auto-generated constructor stub
	// }
	//
	// @Override
	// public View getView(int position, View v, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// Artist artist = getItem(position);
	// ViewHolder holder = null;
	//
	// if (v == null) {
	// v = ((Activity) mContext).getLayoutInflater().inflate(
	// R.layout.song_list_item, null);
	// holder = new ViewHolder();
	// holder.image = (ImageView) v.findViewById(R.id.art_image);
	// holder.name = (TextView) v.findViewById(R.id.art_name);
	// holder.songs = (LinearLayout) v.findViewById(R.id.art_songs);
	// try {
	// holder.lvSong = helper.getSongDao().queryForEq("artist_id",
	// artist.id);
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// v.setTag(holder);
	// } else
	// holder = (ViewHolder) v.getTag();
	// holder.image.setImageBitmap(getBitmapFromAsset(artist.id + ".png"));
	// holder.name.setText(artist.name);
	// // if (!holder.lvSong.isEmpty()) {
	// Song song = null;
	// holder.songs.removeAllViews();
	// View songView = ((Activity) mContext).getLayoutInflater().inflate(
	// R.layout.song_list_song, null);
	// Log.i("size id", holder.lvSong.size() + "");
	// for (int i = 0; i < holder.lvSong.size(); i++) {
	//
	// SubHolder subHolder = new SubHolder();
	// subHolder.name = (TextView) songView
	// .findViewById(R.id.song_name);
	// subHolder.fav = (ImageView) songView
	// .findViewById(R.id.song_fav);
	// subHolder.lvl = (ImageView) songView
	// .findViewById(R.id.song_lvl);
	// song = holder.lvSong.get(i);
	//
	// subHolder.name.setText(song.name);
	// holder.songs.addView(songView);
	//
	// // }
	//
	// }
	//
	// return v;
	// }
	//
	// }

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		v.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.touch_down));
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (v == sortChar) {
					CreateDialog();
				}
				if (v == favSort) {
					try {
						List<Song> favSong = helper.getSongDao().queryBuilder()
								.groupBy("artist_id").where().eq("isfav", true)
								.query();
						if (favSong.size() > 0) {
							artists.clear();
							for (int i = 0; i < favSong.size(); i++) {
								Artist art = helper.getArtDao().queryForId(
										favSong.get(i).artist_id);

								artists.add(art);

								Log.i("artist",
										art.name + !artists.contains(art));

							}
							Log.e("size of adapter	", artists.size() + "");
							adapter = new ExAdapter(getActivity(), artists,
									true);
							songLV.setAdapter(adapter);
							for (int i = 0; i < adapter.getGroupCount(); i++) {
								songLV.expandGroup(i);
							}
							songLV.setGroupIndicator(null);
						} else
							Toast.makeText(getActivity(),
									"Дуртай дууны жагсаалт хоосон байна",
									Toast.LENGTH_SHORT).show();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}, 150);

	}

	private Bitmap getBitmapFromAsset(String strName) {
		AssetManager assetManager = getActivity().getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open("artist/" + strName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}
}
