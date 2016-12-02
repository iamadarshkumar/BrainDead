package com.adapps.braindead;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;
import android.app.DownloadManager.Request;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    JSONObject reader;
    JSONArray memes;
    JSONObject memeObject;
    String name,imageurl;
    int screenWidth;
    int screenHeight;
    SharedPreferences wmbPreference1;
    SharedPreferences.Editor editor;

    RecyclerView rView;
    ListAdapter L;

    public List<Memes> meme_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        meme_list = new ArrayList<>();


        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        Log.v("haha",screenWidth+" "+screenHeight);

        initViews();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        try {
            L.notifyDataSetChanged();
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rView.setLayoutManager(mLayoutManager);
            rView.setItemAnimator(new DefaultItemAnimator());
            rView.setAdapter(L);
        }
        catch(Exception e){}


    }

    public void jsonParse(){
        Ion.with(this)
                .load("http://www.braindead.96.lt/jsonobjects.js")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            System.out.println("Hey see the string in ion : "+result);
                            loadJson(result);
                            L=new ListAdapter(getApplicationContext(),meme_list,screenWidth,screenHeight);
                        }
                        catch(Exception e2){

                            Toast.makeText(MainActivity.this,"Cannot connect to Internet!",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }
    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        jsonParse();
        ListAdapter adapter = new ListAdapter(getApplicationContext(),meme_list,screenWidth,screenHeight);
        recyclerView.setAdapter(adapter);

    }
    void loadJson(String result){
        System.out.println("Hey see the string: "+result);
        try {
            //String result1 = "{ \"objects\":[{\"display\": \"My meme 1\",\"url\": \"http://www.braindead.96.lt/memes/meme1.jpg\"},{\"display\": \"My meme 2\",\"url\": \"http://www.braindead.96.lt/memes/meme1.jpg\"},{\"display\": \"My meme 3\",\"url\": \"http://www.braindead.96.lt/memes/meme1.jpg\"}]}";

            reader = new JSONObject(result);
            memes  = reader.getJSONArray("objects");

            for(int i=0;i<memes.length();i++){
                memeObject=memes.getJSONObject(i);

                name=memeObject.getString("display");
                imageurl=memeObject.getString("url");
                Log.v("json",name+" "+imageurl);

                meme_list.add(new Memes(name,imageurl));

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        try {
            data();
        }
        catch(Exception e){}



    }

    public void data(){

        L.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rView.setLayoutManager(mLayoutManager);
        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setAdapter(L);
        L.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.adapps.SnapMeme");

                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(this,"Please install SnapMeme first", Toast.LENGTH_LONG).show();

            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out the BrainDead app: url soon");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_contact) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();

            // Setting Dialog Title
            alertDialog.setTitle("BrainDead\nv1.0");

            alertDialog.setMessage("Contact us on : <Mail id>\nSee the documentation for any queries-\n"+ Html.fromHtml("<a href=\"http://www.google.com\">Check this link out</a>"));

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.funny);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void download(View view)
    {
        String servicestring = Context.DOWNLOAD_SERVICE;
        System.out.println("ghusa dwnld me");
        DownloadManager downloadmanager;
        downloadmanager = (DownloadManager) getSystemService(servicestring);
        Uri uri = Uri
                .parse(L.ImageURL);
        DownloadManager.Request request = new Request(uri);
        Long reference = downloadmanager.enqueue(request);
    }

    public void share(View view)
    {
        Uri uri = Uri.parse("android.resource://com.adapps.braindead/drawable/funny.jpg");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"yo");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        startActivity(intent);

    }

}
