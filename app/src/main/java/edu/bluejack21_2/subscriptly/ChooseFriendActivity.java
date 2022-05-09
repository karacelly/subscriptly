package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.adapter.ChooseFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.databinding.FragmentFriendsBinding;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class ChooseFriendActivity extends AppCompatActivity {

    private static final Comparator<User> ALPHABETICAL_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            return a.getUsername().compareTo(b.getUsername());
        }
    };
    private ArrayList<User> users;
    private ArrayList<FriendRequest> requests;

    private SearchView fieldSearchFriend;
    private RecyclerView friendsRecycler;

    private ChooseFriendRecyclerAdapter mAdapter;
    private FragmentFriendsBinding mBinding;

    private static List<User> filter(List<User> users, String query) {
        Log.d("FLOW", "filter");
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

    private void initComponents() {
        fieldSearchFriend = findViewById(R.id.field_search_friend);
        friendsRecycler = findViewById(R.id.recycler_friends);

        users = new ArrayList<>();
        UserRepository.userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(!UserRepository.LOGGED_IN_USER.getUserID().equals(document.getId())) {
                                users.add(UserRepository.documentToUser(document));
                                setRecyclerView(users, requests, friendsRecycler);
                            }
                        }
                    } else {}
                    fieldSearchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }
                        @Override
                        public boolean onQueryTextChange(String query) {
                            final List<User> filteredModelList = filter(users, query);
                            mAdapter.replaceAll(filteredModelList);
                            mBinding.recyclerFriends.scrollToPosition(0);
                            return true;
                        }
                    });
                });
    }

    private void setRecyclerView(ArrayList<User> users, ArrayList<FriendRequest> requests, RecyclerView recyclerView) {
        mAdapter = new ChooseFriendRecyclerAdapter(users, requests, ALPHABETICAL_COMPARATOR, this, R.layout.friend_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        mAdapter.add(users);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);

        initComponents();
    }

}