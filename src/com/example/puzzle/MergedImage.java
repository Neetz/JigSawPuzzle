package com.example.puzzle;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

public class MergedImage extends Activity {

	MediaPlayer song;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle bundle){
		
		super.onCreate(bundle);
		setContentView(R.layout.activity_main);
		song = MediaPlayer.create(MergedImage.this, R.raw.sound3);
		final Bitmap bitmap = getIntent().getParcelableExtra("merged_image");
		final ImageView image = (ImageView) findViewById(R.id.merged_image);
		image.setImageBitmap(bitmap);
		final	AlertDialog alert=new AlertDialog.Builder(MergedImage.this).create();
					alert.setTitle("CONGRATULATIONS :");
					alert.setMessage(" PUZZLE IS COMPLETE");
					
		alert.setButton("BACK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent openMain = new Intent("com.example.puzzle.MainActivity");
				finish();
				startActivity(openMain);
			}

		});
					
					song.start();
					alert.show();
					}
		
		
}

