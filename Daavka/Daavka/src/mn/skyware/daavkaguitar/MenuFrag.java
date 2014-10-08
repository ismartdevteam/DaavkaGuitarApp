package mn.skyware.daavkaguitar;

import org.billthefarmer.tuner.MainActivity;

import mn.skyware.chords.ChordFrag;
import mn.skyware.song.SongList;
import mn.skyware.strum.StrumFrag;
import mn.skyware.view.MyImageView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeIntents;

public class MenuFrag extends Fragment implements OnClickListener {
	View						view;
	private LinearLayout		mglSong;
	private LinearLayout		tsohilt;
	private LinearLayout		chords;
	private LinearLayout		tuner;
	private LinearLayout		book;
	private LinearLayout		site;
	private LinearLayout		lesson;
	private LinearLayout		about;
	private FragmentTransaction	transaction;
	private Animation			click;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.menu, null);
		// menu
		mglSong = (LinearLayout) view.findViewById(R.id.mgl_song_lin);
		tsohilt = (LinearLayout) view.findViewById(R.id.tsohilt_lin);
		chords = (LinearLayout) view.findViewById(R.id.chords_lin);
		tuner = (LinearLayout) view.findViewById(R.id.tuner_lin);
		book = (LinearLayout) view.findViewById(R.id.book_lin);
		site = (LinearLayout) view.findViewById(R.id.site_lin);
		lesson = (LinearLayout) view.findViewById(R.id.lesson_lin);
		about = (LinearLayout) view.findViewById(R.id.about_lin);
		// mglSong.setOnTouchListener(this);
		// tsohilt.setOnTouchListener(this);
		// chords.setOnTouchListener(this);
		// tuner.setOnTouchListener(this);
		// book.setOnTouchListener(this);
		// site.setOnTouchListener(this);
		// lesson.setOnTouchListener(this);
		// about.setOnTouchListener(this);

		mglSong.setOnClickListener(this);
		tsohilt.setOnClickListener(this);
		chords.setOnClickListener(this);
		tuner.setOnClickListener(this);
		book.setOnClickListener(this);
		site.setOnClickListener(this);
		lesson.setOnClickListener(this);
		about.setOnClickListener(this);
		// head

		click = AnimationUtils.loadAnimation(getActivity(), R.anim.touch_down);
		return view;
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	//
	// // v.startAnimation(click);
	//
	// return false;
	// }

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		// view.setEnabled(false);

		v.startAnimation(click);
		click.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.book_lin:
					Bundle b = new Bundle();
					b.putString("image", "cv.jpg");
					Intent image = new Intent(getActivity(), MyImageView.class);
					image.putExtras(b);
					startActivity(image);
					break;
				case R.id.mgl_song_lin:
					transaction = getActivity().getSupportFragmentManager()
							.beginTransaction();
					transaction.add(R.id.content_frame, new SongList());
					transaction.addToBackStack("Song");
					transaction.commit();

					break;
				case R.id.tsohilt_lin:
					transaction = getActivity().getSupportFragmentManager()
							.beginTransaction();
					transaction.add(R.id.content_frame, new StrumFrag());
					transaction.addToBackStack("Strum");
					transaction.commit();
					break;
				case R.id.site_lin:

					Intent callIntent = new Intent(Intent.ACTION_VIEW);
					callIntent.setData(Uri.parse(getString(R.string.guitarmn)));
					startActivity(callIntent);
					break;
				case R.id.lesson_lin:

					startActivity(YouTubeIntents.createUserIntent(
							getActivity(), getString(R.string.playlistID)));
					break;
				case R.id.chords_lin:
					transaction = getActivity().getSupportFragmentManager()
							.beginTransaction();
					transaction.add(R.id.content_frame, new ChordFrag());
					transaction.addToBackStack("Chord");
					transaction.commit();
					break;
				case R.id.tuner_lin:
					startActivity(new Intent(getActivity(), MainActivity.class));
					break;
				case R.id.about_lin:
					Bundle about = new Bundle();
					about.putString("image", "about.jpg");
					Intent aboutIntent = new Intent(getActivity(),
							MyImageView.class);
					aboutIntent.putExtras(about);
					startActivity(aboutIntent);
					break;
				default:
					break;
				}

			}
		});
	}

}
