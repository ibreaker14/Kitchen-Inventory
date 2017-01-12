package mingninja.net.kitcheninventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mingninja.net.kitcheninventory.R;
import mingninja.net.kitcheninventory.data.DataItem;
import mingninja.net.kitcheninventory.data.DataItemAdapter;
import mingninja.net.kitcheninventory.data.SampleDataProvider;
import mingninja.net.kitcheninventory.fragment.HomeFragment;
import mingninja.net.kitcheninventory.fragment.ManageInventoryFragment;
import mingninja.net.kitcheninventory.fragment.NotificationsFragment;
import mingninja.net.kitcheninventory.fragment.AddFoodsFragment;
import mingninja.net.kitcheninventory.fragment.ShoppingListFragment;
import mingninja.net.kitcheninventory.other.CircleTransform;

public class MainActivity extends AppCompatActivity {

//    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
//    List<String> itemNames = new ArrayList<>();

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgLogo;
    private TextView txtName;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_ADD_FOODS = "add foods";
    private static final String TAG_MANAGE_INVENTORY = "manage inventory";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SHOPPING_LIST = "shopping list";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    //private RelativeLayout relativeLayout;

    /* fab ontouch variables*/
    /* float dX;
    float dY;
    int lastAction;*/

    DisplayMetrics displaymetrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* start toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* end toolbar */

        /* screen size start*/
        displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int screenWidth = displaymetrics.widthPixels;
        final int screenHeight = displaymetrics.heightPixels;
        /* screen size end */

        //relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgLogo = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();
        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        /* start fab*/
/*        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setX(event.getRawX()+dX);
                        view.setY(event.getRawY() + dY);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        */
        /* end fab */

        /* start data item adapter*/
//        Collections.sort(dataItemList,new Comparator<DataItem>(){ //remember me
//            @Override
//            public int compare(DataItem o1,DataItem o2){
//                return o1.getItemName().compareTo(o2.getItemName());
//            }
//        });

//        DataItemAdapter adapter = new DataItemAdapter(this,dataItemList); //remember me
//        ListView listView = (ListView) findViewById(android.R.id.list);
//        listView.setAdapter(adapter);
        /*end data item adapter*/
    }//end oncreate

    /* start menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.

        switch(navItemIndex){
            case 0:  // show menu only when home fragment is selected
                getMenuInflater().inflate(R.menu.main, menu);
                return true;

            case 1:  // when fragment is notifications, load the menu created for notifications
                getMenuInflater().inflate(R.menu.notifications, menu);
                return true;

        }

        //getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.action_mark_all_read:
                Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_clear_notifications:
                Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_filter:
                Toast.makeText(getApplicationContext(), "Filter!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
                return true;

            case R.id.submenu_sortby_category:
                Toast.makeText(getApplicationContext(), "Sort by category", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.submenu_sortby_format:
                Toast.makeText(getApplicationContext(), "sort by format", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.submenu_sortby_storage:
                Toast.makeText(getApplicationContext(), "sort by storage", Toast.LENGTH_SHORT).show();
                return true;

        } //end switch

        return super.onOptionsItemSelected(item);
    }
    /* end menu*/






    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // sliding header name
        txtName.setText("Kitchen Inventory");

        // loading header background image
        Glide.with(this).load(R.drawable.header_background)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        //Glide.with(this).load(urlProfileImg)
        Glide.with(this).load(R.drawable.logo2)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgLogo);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    /* returns fragment depending on nav menu item the user selected. Done using the variacle navItemIndex*/
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // add foods fragment
                AddFoodsFragment addFoodsFragment = new AddFoodsFragment();
                return addFoodsFragment;
            case 2:
                // manage inventory fragment
                ManageInventoryFragment manageInventoryFragment = new ManageInventoryFragment();
                return manageInventoryFragment;
            case 3:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;
            case 4:
                // shoppinglist fragment
                ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
                return shoppingListFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        //sets title of current activity
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    /* initializes the nav drawer by creating necessary click listeners and other functions*/
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_add_foods:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ADD_FOODS;
                        break;
                    case R.id.nav_manage_inventory:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MANAGE_INVENTORY;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_shopping_list:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SHOPPING_LIST;
                        break;
                    case R.id.nav_settings:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

}
