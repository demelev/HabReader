/*
Copyright 2012 Andrey Zaytsev

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
import net.meiolania.apps.habrahabr.data.HubsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HubsAdapter extends BaseAdapter{
    protected ArrayList<HubsData> hubsDatas;
    protected Context context;
    protected boolean additionalLayout = false;

    public HubsAdapter(Context context, ArrayList<HubsData> hubsDatas){
        this.context = context;
        this.hubsDatas = hubsDatas;
        
        Preferences preferences = Preferences.getInstance(context);
        this.additionalLayout = preferences.getAdditionalHubs();
    }

    public int getCount(){
        return hubsDatas.size();
    }

    public HubsData getItem(int position){
        return hubsDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final HubsData hubsData = getItem(position);
        
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.hubs_list_row, null);
        }
        
        TextView title = (TextView)view.findViewById(R.id.hub_title);
        title.setText(hubsData.getTitle());
        
        TextView stat = (TextView)view.findViewById(R.id.hub_stat);
        TextView category = (TextView)view.findViewById(R.id.hub_category);
        TextView index = (TextView)view.findViewById(R.id.hub_index);
        LinearLayout hubInfo = (LinearLayout)view.findViewById(R.id.hub_info);
        
        if(additionalLayout){
            stat.setText(hubsData.getStat());
            category.setText(hubsData.getCategory());
            index.setText(hubsData.getIndex());
        }else{
            stat.setVisibility(View.GONE);
            hubInfo.setVisibility(View.GONE);
        }
        
        return view;
    }

}