package com.example.puzzle;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ArrayList<Bitmap> org, check;
	int chunkNumbers = 9;
	int rows, cols;
	int no_col = 3;
	Intent j;
	int i = 0;
	int count = 0;
	int flag = 0;
	private int firstPosition = -1;
	MediaPlayer song, song1;
	ImageView image;
	Bitmap bmp;
	final static int cameraData = 0;
	ImageButton ib;
	int diff_pos = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid);
		song = MediaPlayer.create(MainActivity.this, R.raw.sound2);
		song1 = MediaPlayer.create(MainActivity.this, R.raw.sound4);
		song.start();
		ib = (ImageButton) findViewById(R.id.imageButton1);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				j = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(j, cameraData);

			}
		});

		Button help = (Button) findViewById(R.id.help);
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

		InputStream is = getResources().openRawResource(R.drawable.ic_tab_cam);
		bmp = BitmapFactory.decodeStream(is);
		Button Exit = (Button) findViewById(R.id.Exit);
		Exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				finish();
				song.release();
				
			}
		});
		Button mergeButton = (Button) findViewById(R.id.merge_button);
		mergeButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				// Get the width and height of the smaller chunks
				int count1 = 1;
				if (flag == 0) {
					final AlertDialog alert = new AlertDialog.Builder(
							MainActivity.this).create();
					alert.setTitle(" TRY AGAIN");
					alert.setMessage(" TAKE THE PHOTO");
					alert.setButton("BACK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(final DialogInterface arg0,
										final int arg1) {
									// TODO Auto-generated method stub

									alert.closeOptionsMenu();

								}
							});

					alert.show();

				} else {
					for (int i = 0; i < chunkNumbers; i++) {
						Bitmap b1 = null;
						Bitmap b2 = null;
						b1 = check.get(i);
						b2 = org.get(i);
						if (!imagesAreEqual(b1, b2)) {
							count1 = 0;
							break;

						}
					}
					if (count1 == 1)

					{

						Bitmap bitmap = bmp;

						Intent intent = new Intent(MainActivity.this,
								MergedImage.class);

						intent.putExtra("merged_image", bitmap);
						finish();
						song.release();
						startActivity(intent);

					} else {

						final AlertDialog alert = new AlertDialog.Builder(
								MainActivity.this).create();
						alert.setTitle(" TRY AGAIN");
						alert.setMessage(" THE PUZZLE IS NOT COMPLETE");
						alert.setButton("BACK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(
											final DialogInterface arg0,
											final int arg1) {
										// TODO Auto-generated method stub

										alert.closeOptionsMenu();

									}
								});

						alert.show();
					}
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			org = split(bmp, chunkNumbers);
			check = split(bmp, chunkNumbers);

			Collections.shuffle(org);
			final GridView grid = (GridView) findViewById(R.id.gridview);

			grid.setAdapter(new ImageAdapter(this, org));

			grid.setNumColumns(no_col);

			final ImageAdapter ca = new ImageAdapter(this, org);

			grid.setAdapter(ca);
			
			

			grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					song1.start();

					if (firstPosition < 0) {

						firstPosition = arg2;
					}

					else {

						Bitmap swapImage = org.get(arg2);

						org.set(arg2, org.get(firstPosition));
						org.set(firstPosition, swapImage);

						firstPosition = -1;
						ca.notifyDataSetChanged();
						grid.invalidateViews();
						grid.setAdapter(ca);


					}
				}

			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private ArrayList<Bitmap> split(Bitmap bitmap, int chunkNumbers) {

		// For the number of rows and columns of the grid to be displayed

		rows = cols = 3;
		

		flag = 1;

		// For height and width of the small image chunks
		int chunkHeight, chunkWidth;

		// To store all the small image chunks in bitmap format in this list
		ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);


		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
				bitmap.getWidth() * 4, bitmap.getHeight() * 4, true);

		rows = cols = (int) Math.sqrt(chunkNumbers);
		chunkHeight = (bitmap.getHeight() * 4) / rows;
		chunkWidth = (bitmap.getWidth() * 4) / cols;

		// xCoord and yCoord are the pixel positions of the image chunks
		int yCoord = 0;
		for (int x = 0; x < rows; x++) {
			int xCoord = 0;
			for (int y = 0; y < cols; y++) {
				chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord,
						yCoord, chunkWidth, chunkHeight));
				xCoord += chunkWidth;
			}
			yCoord += chunkHeight;
		}
		return chunkedImages;
	}

	boolean imagesAreEqual(Bitmap i1, Bitmap i2) {
		if (i1.getHeight() != i2.getHeight())
			return false;
		if (i1.getWidth() != i2.getWidth())
			return false;

		for (int y = 0; y < i1.getHeight(); ++y)
			for (int x = 0; x < i1.getWidth(); ++x)
				if (i1.getPixel(x, y) != i2.getPixel(x, y))
					return false;

		return true;
	}

	public void showDialog() {
		final CharSequence[] items = { "Easy", "Medium", "Hard" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Difficulty Level");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.setSingleChoiceItems(items, diff_pos,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if ("Easy".equals(items[which])) {
							rows = cols = 2;
							chunkNumbers = 4;
							no_col = 2;
							diff_pos = 0;
						} else if ("Medium".equals(items[which])) {
							rows = cols = 3;
							chunkNumbers = 9;
							no_col = 3;
							diff_pos = 1;

						} else if ("Hard".equals(items[which])) {
							rows = cols = 4;
							chunkNumbers = 16;
							no_col = 4;
							diff_pos = 2;
						}

					}
				});
		builder.show();

	}

}
