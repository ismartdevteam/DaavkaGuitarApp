package mn.skyware.strum;

import mn.skyware.daavkaguitar.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;

public class StrumFrag extends Fragment implements OnClickListener {
	private View v;
	private Button one;
	private Button two;
	private Button three;
	private Button four;
	private Button five;
	private Button six;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.strum, null);
		one = (Button) v.findViewById(R.id.tsoh_one);
		two = (Button) v.findViewById(R.id.tsoh_two);
		three = (Button) v.findViewById(R.id.tsoh_three);
		four = (Button) v.findViewById(R.id.tsoh_four);
		five = (Button) v.findViewById(R.id.tsoh_five);
		six = (Button) v.findViewById(R.id.tsoh_six);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		v.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = 0;
		switch (v.getId()) {
		case R.id.tsoh_one:
			pos = 0;
			break;
		case R.id.tsoh_two:
			pos = 1;
			break;
		case R.id.tsoh_three:
			pos = 2;
			break;
		case R.id.tsoh_four:
			pos = 3;
			break;
		case R.id.tsoh_five:
			pos = 4;
			break;
		case R.id.tsoh_six:

			break;

		}
		StrumDet det = new StrumDet();
		Bundle bundle = new Bundle();
		bundle.putInt("pos", pos);
		det.setArguments(bundle);
		det.show(getActivity().getSupportFragmentManager().beginTransaction(),
				"strum");
	}

}
