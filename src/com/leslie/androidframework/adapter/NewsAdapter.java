package com.leslie.androidframework.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.leslie.androidframework.R;
import com.leslie.androidframework.entity.News;
import com.leslie.androidframework.util.BitmapCache;

public class NewsAdapter extends BaseAdapter {
	private Context context;
	private List<News> data;
	private ImageLoader imageLoader;
	private RequestQueue queue;

	public NewsAdapter(Context context, List<News> data) {
		this.context = context;
		this.data = data;
		queue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(queue, new BitmapCache());
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.news_list_item, null);
			holder = new ViewHolder();
			holder.image = (NetworkImageView) convertView
					.findViewById(R.id.news_item_img);
			holder.title = (TextView) convertView
					.findViewById(R.id.news_item_title);
			holder.summary = (TextView) convertView
					.findViewById(R.id.news_item_abstract);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		News news = data.get(position);
		news.setImgUrl("http://files.cnblogs.com/lesliefang/icon1.ico");
		if (news.getImgUrl() != null && !news.getImgUrl().equals("")) {
			holder.image.setDefaultImageResId(R.drawable.ic_launcher);
			holder.image.setErrorImageResId(R.drawable.ic_launcher);
			holder.image.setImageUrl(news.getImgUrl(), imageLoader);
		}

		holder.title.setText(news.getTitle());
		holder.summary.setText(news.getSummary());

		return convertView;
	}

	private class ViewHolder {
		NetworkImageView image;
		TextView title;
		TextView summary;
	}
}
