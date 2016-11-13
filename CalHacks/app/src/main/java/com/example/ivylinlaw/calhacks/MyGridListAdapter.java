package com.example.ivylinlaw.calhacks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ivylinlaw.calhacks.helper.ImageLoader;

import java.util.ArrayList;

import static android.R.attr.key;

public class MyGridListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> imageThumbList;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public MyGridListAdapter(Context context, ArrayList<String> imageThumbList) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageThumbList = imageThumbList;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return imageThumbList.size();
	}

	@Override
	public Object getItem(int position) {
		return imageThumbList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.media_list_inflater, null);
		Holder holder = new Holder();
		holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
		imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);

		final int pos = position;
		holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("MyGridListAdapter ", "holder.ivPhoto clicked @"+pos);
				Intent mIntent = new Intent(context, MediaFileAnalyzeActivity.class);
				mIntent.putExtra("URI", imageThumbList.get(pos));
				Log.d("DEBUG1", imageThumbList.get(pos));
				context.startActivity(mIntent);
			}
		});

		return view;
	}

	private class Holder {
		private ImageView ivPhoto;
	}

}
