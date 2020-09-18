package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.UnselectFavoriteEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoNeighbourActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_activity_info) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.name_on_avatar_neighbour) TextView nameOnAvatar;
    @BindView(R.id.title_name_neighbour) TextView titleNameNeighbour;
    @BindView(R.id.location_neighbour) TextView locationNeighbour;
    @BindView(R.id.phone_neighbour) TextView phoneNeighbour;
    @BindView(R.id.website_neighbour) TextView websiteNeighbour;
    @BindView(R.id.avatar_neighbour) ImageView avatarNeighbour;
    @BindView(R.id.text_about_me_neighbour) TextView textAboutMeNeighbour;

    private static String TAG_NEIGHBOUR_INTENT_EXTRA = "NEIGHBOUR_EXTRA";
    private static int TAG_REQUEST_CODE_RESULTS = 1;
    private Neighbour mNeighbour;

    private NeighbourApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_neighbour);

        ButterKnife.bind(this);

        initializeToolbar();

        // Get list of Neighbour
        mApiService = DI.getNeighbourApiService();

        // Get info Neighbour to display from intent
        Intent infoNeighbour = getIntent();

        if(infoNeighbour.hasExtra(TAG_NEIGHBOUR_INTENT_EXTRA)){
            mNeighbour = (Neighbour) infoNeighbour.getSerializableExtra(TAG_NEIGHBOUR_INTENT_EXTRA);
            initializeInfoToDisplay();
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void initializeInfoToDisplay(){
        //TextViews
        nameOnAvatar.setText(mNeighbour.getName());
        titleNameNeighbour.setText(mNeighbour.getName());

        if(mNeighbour.getAddress() != null){ locationNeighbour.setText(mNeighbour.getAddress());}
        if(mNeighbour.getPhoneNumber() != null){ phoneNeighbour.setText(mNeighbour.getPhoneNumber());}
        if(mNeighbour.getAboutMe() != null) { textAboutMeNeighbour.setText(mNeighbour.getAboutMe());}
        if(mNeighbour.getWebSite() != null) { websiteNeighbour.setText(mNeighbour.getWebSite());}

        // Avatar
        Glide.with(InfoNeighbourActivity.this)
                .load(mNeighbour.getAvatarUrl())
                .skipMemoryCache(false)
                .apply(RequestOptions.centerCropTransform())
                .skipMemoryCache(false)
                .into(avatarNeighbour);

        // FloatingButton status
        if(mNeighbour.getFavorite()){ fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_yellow_24dp, null));}
        else{ fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_yellow_24dp, null));}
    }

    @Subscribe
    public void updateFavoriteStatusInList(UnselectFavoriteEvent event){
        // Find which item must been modified in list
        int index = mApiService.getNeighbours().indexOf(event.favorite);
        // Update "Favorite" status
        mApiService.getNeighbours().get(index).setFavorite(mNeighbour.getFavorite());
    }

    @OnClick(R.id.fab)
    public void updateFabDisplay(){
        // Update "Favorite" status
        mNeighbour.setFavorite(!mNeighbour.getFavorite());
        // Update display according to new value
        if(mNeighbour.getFavorite()){ fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_yellow_24dp, null));}
        else{ fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_yellow_24dp, null));}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_info_neighbour, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){ // Back arrow
            // Save new Favorite status for current User before ending the activity
            saveFavoriteStatus();
            finish();
        }
        if(item.getItemId() == R.id.edit_item_menu){ // Edit contact
            Intent launchActivityInfo = new Intent(this, AddNeighbourActivity.class);
            launchActivityInfo.putExtra(TAG_NEIGHBOUR_INTENT_EXTRA, mNeighbour);
            startActivityForResult(launchActivityInfo, TAG_REQUEST_CODE_RESULTS);
        }
        
        return super.onOptionsItemSelected(item);
    }



    public void initializeToolbar(){
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            toolbar.setPopupTheme(R.style.BackArrowTheme);
        } catch(NullPointerException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        // Save new Favorite status for current User before ending the activity
        saveFavoriteStatus();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAG_REQUEST_CODE_RESULTS){
            if(resultCode  == Activity.RESULT_OK){
                // Update mNeighbour fields
                Neighbour newNeighbour = (Neighbour) data.getSerializableExtra("UPDATE");

                mNeighbour.setName(newNeighbour.getName());
                mNeighbour.setName(newNeighbour.getName());
                mNeighbour.setAddress(newNeighbour.getAddress());
                mNeighbour.setPhoneNumber(newNeighbour.getPhoneNumber());
                mNeighbour.setWebSite(newNeighbour.getWebSite());
                mNeighbour.setAboutMe(newNeighbour.getAboutMe());

                // Update display
                initializeInfoToDisplay();

                // Update data in Neighbour list
                mApiService.updateDataNeighbour(mNeighbour);
            }
        }
    }

    public void saveFavoriteStatus(){
        EventBus.getDefault().post(new UnselectFavoriteEvent(mNeighbour));
    }
}
