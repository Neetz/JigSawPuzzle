package com.example.puzzle;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Bitmap> smallImages;
	int imageHeight, imageWidth;

	public ImageAdapter(Context c, ArrayList<Bitmap> smallImages) {

		mContext = c;
		this.smallImages = smallImages;
		imageHeight = smallImages.get(0).getHeight();
		imageWidth = smallImages.get(0).getWidth();
	}

	@Override
	public int getCount() {
		return smallImages.size();
	}

	@Override
	public Object getItem(int position) {
		return smallImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView image;
		if (convertView == null) {
			image = new ImageView(mContext);

			/*
			 * NOTE: I have set imageWidth - 10 and imageHeight as arguments to
			 * LayoutParams class. But you can take anything as per your
			 * requirement
			 */
			image.setLayoutParams(new GridView.LayoutParams(imageWidth - 4,
					imageHeight));
			image.setPadding(0, 0, 0, 0);
		} else {
			image = (ImageView) convertView;
		}
		image.setImageBitmap(smallImages.get(position));
		return image;
	}

}
