package mn.skyware.howto;

import java.io.IOException;
import java.io.InputStream;

import mn.skyware.daavkaguitar.R;
import mn.skyware.view.PagerSlidingTabStrip;
import uk.co.senab.photoview.PhotoView;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;

public class HowTo extends FragmentActivity {
	private PagerSlidingTabStrip	strip;
	private ViewPager				pager;
	private String[]				names	= { "Монгол дуу", "Цохилт",
			"Аккорд", "Хөглөгч", "Бусад"	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.how_to);
		strip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
		pager = (ViewPager) findViewById(R.id.mPager);
		pager.setAdapter(new MyAdapter(names));
		strip.setViewPager(pager);
		strip.setIndicatorColorResource(R.color.pop_back);
	}

	private class MyAdapter extends PagerAdapter {
		String[]	mCont;

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mCont[position];
		}

		public MyAdapter(String[] name) {
			// TODO Auto-generated constructor stub
			this.mCont = name;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {

			holder hol = new holder();
			LayoutInflater inflater = getLayoutInflater();
			View v = inflater.inflate(R.layout.cv, null);
			hol.img = (PhotoView) v.findViewById(R.id.magazine_adap_item);

			hol.img.setScaleType(ScaleType.CENTER_CROP);
			AssetManager assetManager = getAssets();
			InputStream is = null;
			try {
				is = assetManager.open("howto" + (position + 1) + ".jpg");
				TileBitmapDrawable.attachTileBitmapDrawable(hol.img, is, null,
						null);

			} catch (IOException e) {
				e.printStackTrace();
			}
			((ViewPager) container).addView(v, 0);
			return v;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCont.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			// TODO Auto-generated method stub
			return view == ((View) arg1);
		}

		class holder {
			PhotoView	img;
		}
	}
}
