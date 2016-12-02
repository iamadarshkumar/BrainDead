package com.adapps.braindead;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    private List<Memes> MemeList;
    private Context context;
    String ImageURL;
    int width,height;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.memeText);
            //image = (TextView) view.findViewById(R.id.Pimage);
            image = (ImageView) view.findViewById(R.id.memeImg);

        }

    }
    public ListAdapter(Context context, List<Memes> MemeList,int width,int height) {
        this.context = context;
        this.MemeList = MemeList;
        this.width=width;
        this.height=height;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.v("haha2",width+" "+height);

        Memes meme = MemeList.get(position);
        holder.name.setText(meme.getName());

        //holder.image.setText(pro.getImage());
        ImageURL = meme.getImage();
        Picasso.with(context)
                .load(ImageURL).resize(width,height-350).placeholder(R.drawable.loading).into(holder.image);

    }


    @Override
    public int getItemCount() {
        return MemeList.size();
    }
}
