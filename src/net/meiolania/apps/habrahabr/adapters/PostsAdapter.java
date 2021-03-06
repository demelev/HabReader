/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PostsAdapter extends BaseAdapter {
    private ArrayList<PostsData> posts;
    private Context context;
    private boolean additionalLayout = false;
    private boolean postsFullInfo = false;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public PostsAdapter(Context context, ArrayList<PostsData> posts) {
	this.context = context;
	this.posts = posts;

	Preferences preferences = Preferences.getInstance(context);
	additionalLayout = preferences.getAdditionalPosts();
	postsFullInfo = preferences.getPostsFullInfo();

	DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
	ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(3000000)
		.maxImageWidthForMemoryCache(200).discCacheSize(50000000).httpReadTimeout(5000).defaultDisplayImageOptions(options).build();
	imageLoader.init(configuration);
    }

    @Override
    public int getCount() {
	return posts.size();
    }

    @Override
    public PostsData getItem(int position) {
	return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
	PostsData data = getItem(position);

	if (additionalLayout) {
	    ViewHolder viewHolder;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.posts_list_row, null);
		
		viewHolder = new ViewHolder();
		viewHolder.title = (TextView) view.findViewById(R.id.post_title);
		viewHolder.hubs = (TextView) view.findViewById(R.id.post_hubs);
		viewHolder.author = (TextView) view.findViewById(R.id.post_author);
		viewHolder.date = (TextView) view.findViewById(R.id.post_date);
		viewHolder.score = (TextView) view.findViewById(R.id.post_score);
		viewHolder.image = (ImageView) view.findViewById(R.id.post_image);
		viewHolder.text = (TextView) view.findViewById(R.id.post_text);
		
		view.setTag(viewHolder);
	    } else
		viewHolder = (ViewHolder) view.getTag();

	    viewHolder.title.setText(data.getTitle());

	    if (postsFullInfo) {
		if (!TextUtils.isEmpty(data.getImage())) {
		    viewHolder.image.setVisibility(View.VISIBLE);
		    imageLoader.displayImage(data.getImage(), viewHolder.image);
		} else
		    viewHolder.image.setVisibility(View.GONE);

		viewHolder.text.setText(Html.fromHtml(data.getText()));
	    } else {
		viewHolder.image.setVisibility(View.GONE);
		viewHolder.text.setVisibility(View.GONE);
	    }

	    viewHolder.hubs.setText(data.getHubs());
	    viewHolder.author.setText(data.getAuthor());
	    viewHolder.date.setText(data.getDate());

	    Integer rating = UIUtils.parseRating(data.getScore());
	    if (rating != null) {
		viewHolder.score.setVisibility(View.VISIBLE);

		if (rating > 0)
		    viewHolder.score.setTextColor(context.getResources().getColor(R.color.rating_positive));
		else if (rating < 0)
		    viewHolder.score.setTextColor(context.getResources().getColor(R.color.rating_negative));
		else
		    viewHolder.score.setTextColor(context.getResources().getColor(R.color.black));
	    }
	    viewHolder.score.setText(data.getScore());

	    return view;
	} else {
	    ViewHolder viewHolder;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.posts_list_row_simple, null);
		
		viewHolder = new ViewHolder();
		
		viewHolder.title = (TextView) view.findViewById(R.id.post_title);
		
		view.setTag(viewHolder);
	    } else
		viewHolder = (ViewHolder) view.getTag();

	    viewHolder.title.setText(data.getTitle());

	    return view;
	}
    }
    
    static class ViewHolder {
	TextView title;
	TextView hubs;
	TextView author;
	TextView date;
	TextView score;
	ImageView image;
	TextView text;
    }

}
