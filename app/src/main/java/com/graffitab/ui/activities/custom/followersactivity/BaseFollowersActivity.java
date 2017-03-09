package com.graffitab.ui.activities.custom.followersactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;

/**
 * Created by david on 16/02/2017.
 */

public abstract class BaseFollowersActivity extends AppCompatActivity {

    //TODO: GenericFollowersFragment
    private GenericStreamablesFragment content;

    //TODO: GenericFollowersActivityFragment
    public abstract GenericStreamablesFragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        // Activity view contains the Fragment which contains an AdvancedRecyclerView
        // This can be overriden in order to provide separate layouts to this activity
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        setupTopBar();
        setupContent(getFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getLayoutResId() {
        return R.layout.activity_fragment_holder;
    }

    //TODO: the content of this activity (fragment)
    public GenericStreamablesFragment getContent() {
        return content;
    }

    // Setup

    // Sets Home button as back arrow (standard Android)
    public void setupTopBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Sets the content - attach fragment to the activity
    public void setupContent(GenericStreamablesFragment contentFragment) {
        content = contentFragment;
        content.hasOptionsMenu = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }
}
