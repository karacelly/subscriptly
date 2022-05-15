package edu.bluejack21_2.subscriptly;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.adapter.ChooseFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.ChosenUserRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class ChooseFriendActivity extends AppCompatActivity {

    private static final Comparator<User> ALPHABETICAL_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            return a.getName().compareTo(b.getName());
        }
    };
    public static ChooseFriendRecyclerAdapter chooseFriendAdapter;
    public static ChosenUserRecyclerAdapter chosenUserAdapter;
    private static ArrayList<User> users;
    private TextView doneChooseFriend;
    private SearchView fieldSearchFriend;
    private RecyclerView friendsRecycler, chosenUserRecycler;
    private Boolean isActivityReopened = false;

    private static List<User> filter(List<User> users, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<User> filteredModelList = new ArrayList<>();
        for (User model : users) {
            final String text = model.getUsername().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public static void updateChosenRecycler() {
        chosenUserAdapter.notifyDataSetChanged();
    }

    public static void updateChooseFriendRecycler() {
        chooseFriendAdapter.notifyDataSetChanged();
    }

    private void initComponents() {
        friendsRecycler = findViewById(R.id.recycler_friends);
        chosenUserRecycler = findViewById(R.id.recycler_users_chosen);

        fieldSearchFriend = findViewById(R.id.field_search_friend);

        doneChooseFriend = findViewById(R.id.action_done_choose_friend);
        doneChooseFriend.setOnClickListener(view -> {
            onBackPressed();
        });

        chosenUserAdapter = new ChosenUserRecyclerAdapter(this);
        setRecyclerView(chosenUserAdapter, LinearLayoutManager.HORIZONTAL, chosenUserRecycler);

        chooseFriendAdapter = new ChooseFriendRecyclerAdapter(ALPHABETICAL_COMPARATOR, this);

        if (users == null) users = new ArrayList<>();
        else {
            chooseFriendAdapter.add(users);
            setRecyclerView(chooseFriendAdapter, LinearLayoutManager.VERTICAL, friendsRecycler);
        }

        UserRepository.userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (UserRepository.checkFriend(UserRepository.LOGGED_IN_USER, document.getId()) && !UserHelper.userAlreadyExist(users, document.getId())) {
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
    }

    private void setRecyclerView(RecyclerView.Adapter adapter, int layout, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, layout, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_friend);
        initComponents();
        if (!isActivityReopened) {
            isActivityReopened = true;
        }

    }

}