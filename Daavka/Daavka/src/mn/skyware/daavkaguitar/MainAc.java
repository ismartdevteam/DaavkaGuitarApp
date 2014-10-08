package mn.skyware.daavkaguitar;

import mn.skyware.howto.HowTo;
import mn.skyware.view.MyImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainAc extends FragmentActivity implements 
		OnClickListener, OnItemClickListener {
	public static DrawerLayout	mDrawerLayout;
	public static ListView		mDrawerList;
//	public static ImageView			search;
	private ImageView			menu;
	String[]					menuItems	= { "Гитарын сургалт",
			"Ашиглах заавар" };
	private Animation			click;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.main_layout);

		click = AnimationUtils.loadAnimation(this, R.anim.touch_down);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.END);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.content_frame, new MenuFrag()).commit();
		// View head = getLayoutInflater().inflate(R.layout.menu_list_head,
		// null);
		// mDrawerList.addHeaderView(head);
		mDrawerList.setAdapter(new MenuAdapter(this, 0, 0, menuItems));

		menu = (ImageView) findViewById(R.id.main_menu);
		menu.setOnClickListener(this);
		mDrawerList.setOnItemClickListener(this);
	}

	private class MenuAdapter extends ArrayAdapter<String> {
		Context	mContext;

		public MenuAdapter(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			String item = getItem(position);
			Holder holder = null;
			if (v == null) {
				v = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.menu_list_item, null);
				holder = new Holder();
				holder.text = (TextView) v.findViewById(R.id.menu_item);
				v.setTag(holder);
			} else {
				holder = (Holder) v.getTag();
			}
			holder.text.setText(item);

			return v;
		}

		class Holder {
			TextView	text;
		}
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
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
				case R.id.main_menu:
					mDrawerLayout.openDrawer(mDrawerList);
					break;

				default:
					break;
				}

			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub
		switch (pos) {
		case 0:
			Bundle b = new Bundle();
			b.putString("image", "surgalt.jpg");
			Intent image = new Intent(MainAc.this, MyImageView.class);
			image.putExtras(b);
			startActivity(image);
			break;
		case 1:
			Intent howto = new Intent(MainAc.this, HowTo.class);
			startActivity(howto);
			break;
		}

		mDrawerLayout.closeDrawer(mDrawerList);
	}
}
