package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.adapter.ChooseFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.ChosenUserRecyclerAdapter;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class InviteFriendActivity extends AppCompatActivity implements QueryChangeListener<ArrayList<User>> {

    private static final Comparator<User> ALPHABETICAL_COMPARATOR = Comparator.comparing(User::getName);
    private HorizontalScrollView containerChosenUser;
    public static ChooseFriendRecyclerAdapter chooseFriendAdapter;
    public static ChosenUserRecyclerAdapter chosenUserAdapter;
    private ArrayList<User> users;
    private TextView inviteFriend;
    private SearchView fieldSearchFriend;
    private RecyclerView friendsRecycler, chosenUserRecycler;
    private Boolean isActivityReopened = false;
    private Boolean chosenUserIsShown = false;

    private static List<User> filter(List<User> users, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<User> filteredModelList = new ArrayList<>();
        for (User model : users) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void initAnimation(){
        containerChosenUser.setZ(-5);
        containerChosenUser.setVisibility(View.GONE);
        containerChosenUser.setAlpha(0.0f);
        containerChosenUser.setTranslationY(-containerChosenUser.getHeight());
    }

    private void showChosenUserList(){
        chosenUserIsShown = true;
        containerChosenUser.setVisibility(View.VISIBLE);
        containerChosenUser.setAlpha(0.0f);
        containerChosenUser
                .animate()
                .translationY(0)
                .alpha(1.0f).setListener(null);
    }

    private void hideChosenUserList(){
        containerChosenUser.animate()
                .translationY(-containerChosenUser.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        containerChosenUser.setVisibility(View.GONE);
                        chosenUserIsShown = false;
                    }
                });
    }

    private void initComponents() {
        containerChosenUser = findViewById(R.id.container_chosen_user);
        initAnimation();

        friendsRecycler = findViewById(R.id.recycler_friends);
        chosenUserRecycler = findViewById(R.id.recycler_users_chosen);
        fieldSearchFriend = findViewById(R.id.field_search_friend);

        inviteFriend = findViewById(R.id.action_invite_friend);
        inviteFriend.setText("Cancel");
        inviteFriend.setOnClickListener(view -> {
            onBackPressed();
        });

        chosenUserAdapter = new ChosenUserRecyclerAdapter(this, this);
        setRecyclerView(chosenUserAdapter, LinearLayoutManager.HORIZONTAL, chosenUserRecycler);

        chooseFriendAdapter = new ChooseFriendRecyclerAdapter(ALPHABETICAL_COMPARATOR, this, chosenUserAdapter, this);
        chosenUserAdapter.setChooseFriendRecyclerAdapter(chooseFriendAdapter);

        if (users == null) users = new ArrayList<>();
        else {
            chooseFriendAdapter.add(users);
            setRecyclerView(chooseFriendAdapter, LinearLayoutManager.VERTICAL, friendsRecycler);
        }

        UserRepository.getLoggedInUser(res -> {
            UserRepository.userRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (UserRepository.checkFriend(res, document.getId()) && !UserHelper.userAlreadyExist(users, document.getId()) && !UserHelper.userAlreadyExist(SubscriptionRepository.ACTIVE_SUBSCRIPTION.getMembers(), document.getId())) {
                                    users.add(UserRepository.documentToUser(document));
                                    chooseFriendAdapter.add(users);
                                    setRecyclerView(chooseFriendAdapter, LinearLayoutManager.VERTICAL, friendsRecycler);
                                }
                            }
                        } else {
                        }
                        fieldSearchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                final List<User> filteredModelList = filter(users, query);
                                chooseFriendAdapter.replaceAll(filteredModelList);
                                friendsRecycler.scrollToPosition(0);
                                return true;
                            }
                        });
                    });
        });


    }
    private void setInviteListener(){
//        Toast.makeText(this, "Setting Invite Listener", Toast.LENGTH_SHORT).show();
        inviteFriend.setOnClickListener(view -> {
//        Toast.makeText(this, "Invite Listener is Clicked", Toast.LENGTH_SHORT).show();
                    Log.d("INVITED USER | SIZE", SubscriptionRepository.chosenFriends.size()+"");
            if(!SubscriptionRepository.chosenFriends.isEmpty()) {
                for (User user:
                     SubscriptionRepository.chosenFriends) {
                    Log.d("INVITED USER", user.getUserID());
                    SubscriptionRepository.sendInvitation(FirebaseAuth.getInstance().getCurrentUser().getUid(), SubscriptionRepository.ACTIVE_SUBSCRIPTION.getSubscriptionId(), user.getUserID(), inviteListener -> {
                        if(inviteListener) {
//                            Toast.makeText(this, "Success Invite New Member", Toast.LENGTH_SHORT).show();
                            if(SubscriptionRepository.chosenFriends.get(SubscriptionRepository.chosenFriends.size()-1).equals(user)) {
                                SubscriptionRepository.chosenFriends.clear();
                                onBackPressed();
                            }
                        } else {
//                            Toast.makeText(this, "Invitation Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    private void setRecyclerView(RecyclerView.Adapter adapter, int layout, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, layout, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_friend);
        initComponents();
        if (!isActivityReopened) {
            isActivityReopened = true;
        }

    }

    @Override
    public void onChange(ArrayList<User> data) {
        if(data.isEmpty()) {
            if(chosenUserIsShown) hideChosenUserList();
            inviteFriend.setText("Cancel");
            inviteFriend.setOnClickListener(view -> {
                onBackPressed();
            });
        } else {
            if(!chosenUserIsShown) showChosenUserList();
            inviteFriend.setText("Invite");
            setInviteListener();
        }
    }
}