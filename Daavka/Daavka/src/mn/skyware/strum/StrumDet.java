package mn.skyware.strum;

import mn.skyware.daavkaguitar.R;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class StrumDet extends DialogFragment implements OnClickListener {
	View				v;
	private ViewPager	mPager;
	private Button		mCancel;
	private ImageView	backward;
	private ImageView	forward;
	private ImageView	play;
	private int			i_42[]			= { R.raw.i_421, R.raw.i_422 };
	private int			s_42[]			= { R.raw.s_421, R.raw.s_422 };
	private int			i_43[]			= { R.raw.i_431, R.raw.i_432,
			R.raw.i_433, R.raw.i_434, R.raw.i_435, R.raw.i_436 };
	private int			s_43[]			= { R.raw.s_431, R.raw.s_432,
			R.raw.s_433, R.raw.s_434, R.raw.s_435, R.raw.s_436, };
	private int			i_44[]			= { R.raw.i_441, R.raw.i_442,
			R.raw.i_443, R.raw.i_444, R.raw.i_445, R.raw.i_446, R.raw.i_447,
			R.raw.i_448, R.raw.i_449	};
	private int			s_44[]			= { R.raw.s_441, R.raw.s_442,
			R.raw.s_443, R.raw.s_444, R.raw.s_445, R.raw.s_446, R.raw.s_447,
			R.raw.s_448, R.raw.s_449	};
	private int			i_86[]			= { R.raw.i_861 };
	private int			s_86[]			= { R.raw.s_861 };
	private int			i_88[]			= { R.raw.i_881 };
	private int			s_88[]			= { R.raw.s_881 };
	private int			currentPagePos	= 0;
	private MediaPlayer	mp;
	private int			strumSong[];
	private int			fromSong		= 0;

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
		int i = getArguments().getInt("pos");
		mp = MediaPlayer.create(getActivity(), s_42[0]);
		switch (i) {
		case 0:
			mPager.setAdapter(new MyAdapter(i_42));
			strumSong = s_42;
			break;

		case 1:
			mPager.setAdapter(new MyAdapter(i_43));
			strumSong = s_43;
			break;
		case 2:
			mPager.setAdapter(new MyAdapter(i_44));
			strumSong = s_44;
			break;
		case 3:
			mPager.setAdapter(new MyAdapter(i_86));
			strumSong = s_86;
			break;
		case 4:
			mPager.setAdapter(new MyAdapter(i_88));
			strumSong = s_88;
			break;

		case 5:
			int strum = getArguments().getInt("strum");
			int strumPos = getArguments().getInt("strumPos");
			switch (strum) {
			case 42:
				strum = i_42[strumPos];
				fromSong = s_42[strumPos];
				break;
			case 43:
				strum = i_43[strumPos];
				fromSong = s_43[strumPos];
				break;
			case 44:
				strum = i_44[strumPos];
				fromSong = s_44[strumPos];
				break;
			case 86:
				strum = i_86[0];
				fromSong = s_86[0];
				break;
			case 88:
				strum = i_88[0];
				fromSong = s_88[0];
				break;
			}
			int[] fromAdapter = { strum };
			mPager.setAdapter(new MyAdapter(fromAdapter));
			break;

		}
		backward.setVisibility(View.GONE);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int count = mPager.getAdapter().getCount();
				currentPagePos = arg0;
				if (mp.isPlaying()) {
					play.setImageResource(R.drawable.play);
					mp.stop();
				}
				if (count > 1) {
					count = count - 1;
				}
				if (arg0 == count) {
					forward.setVisibility(View.GONE);
					backward.setVisibility(View.VISIBLE);
				} else {
					backward.setVisibility(View.VISIBLE);
					forward.setVisibility(View.VISIBLE);
				}

				if (arg0 == 0) {
					backward.setVisibility(View.GONE);
					forward.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (mPager.getAdapter().getCount() < 2) {
			forward.setVisibility(View.GONE);
			backward.setVisibility(View.GONE);
		}
	}

	private class MyAdapter extends PagerAdapter {
		int[]	mCont;

		public MyAdapter(int[] content) {
			// TODO Auto-generated constructor stub
			this.mCont = content;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {

			ImageView imageView = new ImageView(getActivity());
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			imageView.setBackgroundResource(mCont[position]);
			imageView.setLayoutParams(new LayoutParams(metrics.widthPixels, 0));
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCont.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			// TODO Auto-generated method stub
			return view == ((ImageView) arg1);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.strum_pop, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		backward = (ImageView) v.findViewById(R.id.backward);
		forward = (ImageView) v.findViewById(R.id.forward);
		play = (ImageView) v.findViewById(R.id.play);
		backward.setOnClickListener(this);
		forward.setOnClickListener(this);
		play.setOnClickListener(this);
		mCancel = (Button) v.findViewById(R.id.pop_dismiss);
		mCancel.setOnClickListener(this);
		mPager = (ViewPager) v.findViewById(R.id.mPager);
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		v.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.touch_down));
		switch (v.getId()) {
		case R.id.play:
			if (mp == null || !mp.isPlaying()) {
				if (fromSong == 0)
					mp = MediaPlayer.create(getActivity(),
							strumSong[currentPagePos]);
				else
					mp = MediaPlayer.create(getActivity(), fromSong);
				mp.start();
				mp.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						play.setImageResource(R.drawable.play);
					}
				});
				play.setImageResource(R.drawable.stop);
			} else {
				play.setImageResource(R.drawable.play);
				mp.stop();
			}
			break;
		case R.id.pop_dismiss:
			getDialog().dismiss();
			break;
		case R.id.backward:
			mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
			break;
		case R.id.forward:
			mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		if (mp != null || mp.isPlaying())
			mp.stop();
		super.onDismiss(dialog);

	}

}
