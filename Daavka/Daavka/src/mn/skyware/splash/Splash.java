package mn.skyware.splash;

import mn.skyware.daavkaguitar.MainAc;
import mn.skyware.daavkaguitar.R;
import mn.skyware.daavkaguitar.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;

public class Splash extends Activity implements OnClickListener {
	Uri				targetUri;

	VideoView		surfaceView;
	private Button	skip;
	private Handler	video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		skip = (Button) findViewById(R.id.skip);
		skip.setOnClickListener(this);
		surfaceView = (VideoView) findViewById(R.id.splashVid);

		int videoResource = getResources().getIdentifier("splash", "raw",
				getPackageName());
		targetUri = Uri.parse("android.resource://" + getPackageName() + "/"
				+ videoResource);
		surfaceView.setVideoURI(targetUri);
		surfaceView.start();

		video = new Handler();
		video.postDelayed(run, 12000);
		surfaceView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				video.removeCallbacks(run);
				setContentView(R.layout.splash_new);
				video.postDelayed(run, 2000);
				skip.setVisibility(View.GONE);
		
				return true;
			}
		});
	}

	private Runnable	run	= new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									startActivity(new Intent(Splash.this,
											MainAc.class));
									overridePendingTransition(
											anim.intent_enter, anim.intent_exit);
									finish();
								}
							};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == skip) {
//			video.removeCallbacks(run);
			startActivity(new Intent(Splash.this, MainAc.class));
			overridePendingTransition(anim.intent_enter, anim.intent_exit);
			finish();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		video.removeCallbacks(run);
	}

	
}
