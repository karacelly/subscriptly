package edu.bluejack21_2.subscriptly;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.NotificationAdapter;
import edu.bluejack21_2.subscriptly.adapter.ViewPagerAdapter;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.BroadcastManager;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;

public class HomeActivity extends AppCompatActivity {
    NotificationAdapter notificationAdapter;
    private BottomNavigationView mBottomNavigation;
    private ViewPager2 viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private CardView profileIcon;
    private ImageView profilePicture;
    private ImageButton notifIcon;
    private RelativeLayout notifPopUp;
    private Boolean notifOpen = false;

    private void initComponents() {
        mBottomNavigation = findViewById(R.id.buttom_navigation);
        viewPager = findViewById(R.id.view_pager);
        profileIcon = findViewById(R.id.profile_icon);
        profilePicture = findViewById(R.id.profile_picture);
        notifIcon = findViewById(R.id.notification_icon);
        notifPopUp = findViewById(R.id.notification_pop_up);

        mViewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(mViewPagerAdapter);
    }

    private void fetchNotificationsData() {
        ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();
        DocumentReference user = UserRepository.userRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.get()
            .addOnSuccessListener(userSnapshot -> {
                if (userSnapshot.contains("notifications")) {
                    ArrayList<DocumentReference> details = (ArrayList<DocumentReference>) userSnapshot.get("notifications");
                    for (DocumentReference detail :
                            details) {
                        detail.get()
                            .addOnSuccessListener(detailSnapshot -> {
                                DocumentReference subscriptionRef = detail.getParent().getParent().getParent().getParent();
                                subscriptionRef.get().addOnSuccessListener(subscriptionSnapshot -> {
                                    SubscriptionRepository.documentToSubscription(subscriptionSnapshot, subscription -> {
                                        SubscriptionRepository.documentToTransactionDetail(detailSnapshot, transactionDetail -> {
                                            transactionDetail.setSubscription(subscription);
                                            transactionDetails.add(transactionDetail);
                                            notificationAdapter.setNotifications(transactionDetails);
                                            notificationAdapter.notifyDataSetChanged();
                                        });
                                    });
                                }).addOnFailureListener(e -> {
                                });
                            }).addOnFailureListener(e -> {
                        });
                    }
                }
            })
            .addOnFailureListener(e -> {
            });
    }

    private void setNotifications() {
        notificationAdapter = new NotificationAdapter(this);
        fetchNotificationsData();
        RecyclerView rv = findViewById(R.id.notification_recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(notificationAdapter);

        notifPopUp.setVisibility(View.GONE);

        notifIcon.setOnClickListener(v -> {
            notifOpen = !notifOpen;
            if (notifOpen) {
                notifPopUp.setVisibility(View.VISIBLE);
                fetchNotificationsData();
            } else {
                notifPopUp.setVisibility(View.GONE);
            }

            ZonedDateTime nowZoned = ZonedDateTime.now();
            Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
            Duration duration = Duration.between(midnight, Instant.now());
            long seconds = duration.getSeconds();
            Log.d("Time", seconds + "");
        });
    }

    private void createPushNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name =  "SubscriptlyChannel" ;
            String description = "Channel for Subscriptly";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("subscriptly", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createPushNotificationChannel();

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        initComponents();
        createMenu();
        setNotifications();

        mBottomNavigation.setOnItemSelectedListener(item -> {
//            Log.d("ViewPager BottomNavigation", item.toString());
//            Log.d("ViewPager BottomNavigation ItemID", item.getItemId()+"");
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_subscriptions:
                    Log.d("ViewPager Setting Item", "Subscription: 1");
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_friends:
                    Log.d("ViewPager Setting Item", "Friend: 2");
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_subscriptions_add:
                    Intent i = new Intent(this, AddSubscriptionActivity.class);
                    startActivity(i);
                    break;
            }
            return true;
        });

//        loadFragment(new HomeFragment(), R.id.container_frame_layout);
        viewPager.setCurrentItem(0);
    }

    private boolean loadFragment(Fragment fragment, int destination) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(destination, fragment);
            ft.commit();
            return true;
        }
        return false;
    }

    public void createMenu() {
        profileIcon = findViewById(R.id.profile_icon);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, profileIcon);

                popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
//                popupMenu.getMenu().getItem(R.id.menu_logout).setOnMenuItemClickListener(menuItem -> {

                popupMenu.getMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
                    Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(i);
                    return true;
                });

                popupMenu.getMenu().getItem(1).setOnMenuItemClickListener(menuItem -> {
                    Intent i = new Intent(HomeActivity.this, MyTransactionsActivity.class);
                    startActivity(i);
                    return true;
                });

                popupMenu.getMenu().getItem(2).setOnMenuItemClickListener(menuItem -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();

                    if(UserRepository.GOOGLE_LOGGED != null) {
                        UserRepository.GOOGLE_LOGGED.signOut();
                    }
                    UserRepository.logOutFirebaseUser();

                    UserRepository.GOOGLE_LOGGED = null;
                    UserRepository.LOGGED_IN_USER = null;

                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    return true;
                });

                popupMenu.show();
            }
        });

        profilePicture = findViewById(R.id.profile_picture);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getLoggedInUser(user -> {
            ImageHelper.LoadImage(getApplicationContext(), user.getImage(), profilePicture);
        });
    }
}