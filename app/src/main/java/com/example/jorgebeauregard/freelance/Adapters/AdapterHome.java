package com.example.jorgebeauregard.freelance.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorgebeauregard.freelance.Activity.MyProjectActivity;
import com.example.jorgebeauregard.freelance.Classes.Project;
import com.example.jorgebeauregard.freelance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge Beauregard on 4/26/2017.
 */

public class AdapterHome extends BaseAdapter {
    private Context context;
    private List<Project> listProjects;
    private LayoutInflater mInflater;
    private Activity parentActivity;

    public AdapterHome(Context context, List listProjects, Activity parentActivity){
        this.context = context;
        this.listProjects = listProjects;
        this.mInflater = LayoutInflater.from(context);
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return listProjects.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String url = "http://10.50.92.115:8000/";
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_project, parent, false);
        }
        TextView categories = (TextView)convertView.findViewById(R.id.categories);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView description = (TextView)convertView.findViewById(R.id.description);
        ImageView image = (ImageView)convertView.findViewById(R.id.image);



        //SetText for Categories
        String categories_string = "";
        List<String> categories_array = new ArrayList<>();
        categories_array = listProjects.get(position).getCategories();
        if(categories_array.size()!=0) {
            for (int i = 0; i < categories_array.size(); i++) {
                categories_string += categories_array.get(i) + ", ";
            }

            categories.setText(categories_string);
        }

        //SetText for Name
        name.setText(listProjects.get(position).getName());

        //SetText for Description
        description.setText(listProjects.get(position).getDescription());

        //Picasso for Image
        if((listProjects.get(position).getImage().size())>0) {
            Log.e("image", listProjects.get(position).getImage().get(0));
            Picasso.with(context).load(url + listProjects.get(position).getImage().get(0)).centerCrop().fit().into(image);
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
