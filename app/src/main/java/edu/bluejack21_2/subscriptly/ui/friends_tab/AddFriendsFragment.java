package edu.bluejack21_2.subscriptly.ui.friends_tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.AddFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.PendingFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class AddFriendsFragment extends Fragment implements QueryChangeListener<User> {
    private static ArrayList<User> users = new ArrayList<>();
    private final int LIMIT = 8;
    boolean isFetchingData = false;
    private AddFriendRecyclerAdapter adapter;
    private SearchView fieldSearchFriend;
    private RecyclerView recyclerView;

    public AddFriendsFragment() {
    }

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new AddFriendRecyclerAdapter(getActivity(), this);
        recyclerView = view.findViewById(R.id.recycler_existing_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        fieldSearchFriend = view.findViewById(R.id.field_search_friend);
//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int totalItem = transactions.size();
//                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//
//                if (lastVisible >= totalItem - 2) {
//                    if (!isFetchingData) {
//                        isFetchingData = true;
//                        fetchData();
//                    }
//                }
//            }
//        });

        fieldSearchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                final List<User> filteredModelList = filter(users, query);
                adapter.setUsers((ArrayList<User>) filteredModelList);
//                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
                return true;
            }
        });
    }

    private void fetchData() {
        UserRepository.getAllStranger(FirebaseAuth.getInstance().getUid(), users -> {
            this.users.addAll(users);
            adapter.setUsers(users);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        users.clear();
        fetchData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_tab_add_friend, container, false);
    }

    @Override
    public void onChange(User data) {
        users.remove(data);
    }
}