package mn.skyware.song;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import mn.skyware.chords.ChordDet;
import mn.skyware.daavkaguitar.R;
import mn.skyware.database.DatabaseHelper;
import mn.skyware.database.Song;
import mn.skyware.strum.StrumDet;
import android.R.color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class SongDet extends FragmentActivity implements OnClickListener,
		YouTubePlayer.OnInitializedListener {
	private LinearLayout					detLin;
	private LinearLayout					strumLin;
	private DatabaseHelper					helper;
	private Bundle							bundle;
	private Song							songObj;
	private TextView						name;
	private TextView						Artname;
	protected PowerManager.WakeLock			mWakeLock;
	// scroll
	private Button							minus;
	private Button							plus;
	private RelativeLayout					play;
	private ImageView						playImg;
	private int								verticalScrollMax;
	private Timer							scrollTimer				= null;
	private TimerTask						clickSchedule;
	private TimerTask						scrollerSchedule;
	private TimerTask						faceAnimationSchedule;
	private int								scrollPos				= 0;
	private Timer							clickTimer				= null;
	private Timer							faceTimer				= null;
	private ScrollView						scroll;
	private Animation						click;
	private int								minScroll				= 1;
	private int								maxScroll				= 4;
	private int								currentScroll			= 1;
	private boolean							isScrolling				= false;
	private ImageView						fav;
	private YouTubePlayerSupportFragment	playerFragment;
	private FrameLayout						frame;
	private YouTubePlayer					player;
	private Button							viewYoutube;
	private static final String				YOUTUBE_FRAGMENT_TAG	= "youtube";
	private int								tagText					= 0;
	private ZoomControls					zoom;
	private float							maxTextSize;
	private float							minTextSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		wakeLock();
		setContentView(R.layout.song_detail);
		strumLin = (LinearLayout) findViewById(R.id.det_tsohilt);
		strumLin.setOnClickListener(this);
		frame = (FrameLayout) findViewById(R.id.youtube_fragment);
		click = AnimationUtils.loadAnimation(this, R.anim.touch_down);
		bundle = getIntent().getExtras();
		zoom = (ZoomControls) findViewById(R.id.zoomControl);
		helper = new DatabaseHelper(this);
		try {
			songObj = helper.getSongDao().queryForId(bundle.getInt("id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// scroll
		scroll = (ScrollView) findViewById(R.id.song_scroll);
		minus = (Button) findViewById(R.id.scrollMin);
		plus = (Button) findViewById(R.id.scrollPlus);
		play = (RelativeLayout) findViewById(R.id.scrollPlay);
		playImg = (ImageView) findViewById(R.id.scrollPlayImage);
		minus.setOnClickListener(this);
		plus.setOnClickListener(this);
		play.setOnClickListener(this);
		fav = (ImageView) findViewById(R.id.det_fav);
		fav.setOnClickListener(this);
		detLin = (LinearLayout) findViewById(R.id.det_lin);
		fav = (ImageView) findViewById(R.id.det_fav);
		name = (TextView) findViewById(R.id.det_song_name);
		Artname = (TextView) findViewById(R.id.det_art_name);
		viewYoutube = (Button) findViewById(R.id.viewYoutube);
		viewYoutube.setOnClickListener(this);
		Artname.setText(bundle.getString("art"));
		name.setText(songObj.name + "");
		Log.i("isFav", songObj.isfav + "");
		
		if (songObj.isfav)
			fav.setImageResource(R.drawable.fav);
		initSong();
		Zoom();
	}

	private void Zoom() {

		zoom.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < detLin.getChildCount(); i++) {
					TextView txt = (TextView) detLin.getChildAt(i);
					int tag = (Integer) txt.getTag();

					if (tag == 1) {
						if (txt.getTextSize() < maxTextSize)
							txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									txt.getTextSize() + 1);
					}
				}
			}
		});
		zoom.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < detLin.getChildCount(); i++) {
					TextView txt = (TextView) detLin.getChildAt(i);
					int tag = (Integer) txt.getTag();

					if (tag == 1) {
						if (txt.getTextSize() > minTextSize)
							txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									txt.getTextSize() - 1);
					}
				}
			}
		});
	}

	private void youtube() {
		frame.setVisibility(View.VISIBLE);
		viewYoutube.setText(getString(R.string.hideYoutube));
		if (player == null) {
			playerFragment = new YouTubePlayerSupportFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.youtube_fragment, playerFragment,
							YOUTUBE_FRAGMENT_TAG).commit();
			playerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
		}
	}

	public void getScrollMaxAmount() {
		int actualWidth = (detLin.getMeasuredHeight() - (256 * 3));
		verticalScrollMax = actualWidth;
	}

	private void wakeLock() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void startAutoScrolling() {
		playImg.setImageResource(R.drawable.blackstop);
		if (scrollTimer == null) {
			scrollTimer = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {
					moveScrollView();
				}
			};

			if (scrollerSchedule != null) {
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer.schedule(scrollerSchedule, 60, 60);
		}
	}

	public void moveScrollView() {
		scrollPos = (int) (scroll.getScrollY() + currentScroll);
		if (scrollPos >= verticalScrollMax) {
			scrollPos = 0;
		}
		scroll.scrollBy(0, currentScroll);
		Log.i("scrollPos", scrollPos + "");
		Log.e("moveScrollView", "moveScrollView");
	}

	public void stopAutoScrolling() {
		playImg.setImageResource(R.drawable.blackplay);
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer = null;
		}
	}

	private void initSong() {
		String song = songObj.lyric + "";
		String[] arg = song.split("\\n");
		DisplayMetrics metrics = getResources().getDisplayMetrics();

		for (int i = 0; i < arg.length; i++) {
			float dp = 13f;
			String index = arg[i];
			if (index.contains("-H")) {
				index = index.replace("-H", "").trim();
				String[] chords = index.split("-");
				index = index.replace("-", "");
				SpannableString ss = new SpannableString(index);
				for (int s = 0; s < chords.length; s++) {
					int indexoF = 0;
					final String chord = new String(chords[s]).trim();
					if (chord.length() > 0) {
						indexoF = index.indexOf(chord);
						Log.i("indexof", indexoF + "");
						if (index.indexOf(chord, 2) < 0) {

							ss.setSpan(new MyClickableSpan(chord), indexoF,
									indexoF + chord.length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						} else {
							ss.setSpan(new MyClickableSpan(chord),
									index.indexOf(chord, 2),
									index.indexOf(chord, 2) + chord.length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}

				}
				TextView chordTv = new TextView(this);

				chordTv.setMovementMethod(LinkMovementMethod.getInstance());

				float fpixels = metrics.density * dp;
				int pixels = (int) (fpixels + 0.5f);
				chordTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels);
				chordTv.setGravity(Gravity.CENTER);
				// chordTv.setSingleLine();
				chordTv.setTextColor(getResources().getColor(R.color.chord_col));
				chordTv.setText(ss);
				chordTv.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				chordTv.setShadowLayer(0.5f, 0.5f, 0, color.black);
				// chordLin.addView(chordTv);
				tagText = 0;
				chordTv.setTag(tagText);
				detLin.addView(chordTv);

			} else {

				TextView row = new TextView(this);
				row.setTextColor(getResources().getColor(android.R.color.white));
				float fpixels = metrics.density * dp;
				int pixels = (int) (fpixels + 0.5f);
				if (index.contains("-p")) {
					index = index.replace("-p", "");
					dp = 20f;
					tagText = 0;
				} else {
					tagText = 1;
					maxTextSize = pixels * 2;
					minTextSize = pixels;
				}
				row.setText(index);

				row.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels);
				row.setTag(tagText);
				detLin.addView(row);
			}

		}

	}

	class MyClickableSpan extends ClickableSpan {// extend ClickableSpan

		String	clicked;

		public MyClickableSpan(String string) {
			// TODO Auto-generated constructor stub
			super();
			clicked = string.trim();
		}

		public void onClick(View tv) {
			ChordDet det = new ChordDet();
			Bundle b = new Bundle();
			if (clicked.contains("/"))
				clicked = clicked.replace("/", "");
			if (clicked.contains("m")) {
				clicked = clicked.replaceFirst("m", " min");
			} else {
				if (clicked.contains("7")) {
					clicked = clicked.replace("7", " maj7");
				} else {
					if (clicked.contains("sus")) {
						clicked = clicked.replace("sus", " sus");
					} else {
						clicked = clicked + " maj";
					}
				}
			}

			b.putString("path", "chord/" + clicked.substring(0, 2).trim() + "/"
					+ clicked + ".jpg");
			det.setArguments(b);
			det.show(getSupportFragmentManager().beginTransaction(), "chord");
			// Toast.makeText(SongDet.this, clicked, Toast.LENGTH_SHORT).show();
		}

		public void updateDrawState(TextPaint ds) {// override updateDrawState
			ds.setUnderlineText(false); // set to false to remove underline
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		v.startAnimation(click);
		switch (v.getId()) {
		case R.id.viewYoutube:
			if (frame.isShown()) {
				if(player!=null)
				player.pause();
				frame.setVisibility(View.GONE);
				viewYoutube.setText(getString(R.string.viewYoutube));
			} else {
				youtube();
			}
			break;
		case R.id.det_tsohilt:
			if (songObj.strum != null) {
				String strum[] = songObj.strum.split(" ");

				StrumDet det = new StrumDet();
				Bundle bundle = new Bundle();
				bundle.putInt("pos", 5);
				bundle.putInt("strum", Integer.parseInt(strum[0]));
				bundle.putInt("strumPos", Integer.parseInt(strum[1]) - 1);
				det.setArguments(bundle);
				det.show(getSupportFragmentManager().beginTransaction(),
						"strum");
			} else
				Toast.makeText(this, "Тохирох цохилт олдсонгүй",
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.scrollPlay:
			if (!isScrolling) {
				getScrollMaxAmount();
				startAutoScrolling();
				isScrolling = true;
			} else {
				stopAutoScrolling();
				isScrolling = false;
			}
			break;
		case R.id.scrollPlus:
			if (currentScroll < maxScroll)
				currentScroll++;
			break;
		case R.id.scrollMin:
			if (currentScroll > minScroll)
				currentScroll--;
			break;
		case R.id.det_fav:
			if (songObj.isfav) {
				songObj.isfav = false;
				fav.setImageResource(R.drawable.favwhite);
			} else {
				songObj.isfav = true;
				fav.setImageResource(R.drawable.fav);
			}
			try {
				helper.getSongDao().update(songObj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		}
	}

	public void onDestroy() {
		clearTimerTaks(clickSchedule);
		clearTimerTaks(scrollerSchedule);
		clearTimerTaks(faceAnimationSchedule);
		clearTimers(scrollTimer);
		clearTimers(clickTimer);
		clearTimers(faceTimer);

		clickSchedule = null;
		scrollerSchedule = null;
		faceAnimationSchedule = null;
		scrollTimer = null;
		clickTimer = null;
		faceTimer = null;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		SongList.isBack = true;
		// try {
		// SongList.artists = helper.getArtDao().queryForAll();
		// SongList.adapter.notifyDataSetChanged();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (NullPointerException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

		super.onBackPressed();
	}

	private void clearTimers(Timer timer) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void clearTimerTaks(TimerTask timerTask) {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		// TODO Auto-generated method stub
		if (!wasRestored) {
			this.player = player;
			player.cueVideo(songObj.youtube_url.replace(
					"https://www.youtube.com/watch?v=", ""));
		}
	}
}
