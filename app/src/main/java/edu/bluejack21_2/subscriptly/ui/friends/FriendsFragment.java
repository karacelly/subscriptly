package edu.bluejack21_2.subscriptly.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.FriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.databinding.FragmentFriendsBinding;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;


public class FriendsFragment extends Fragment {

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<FriendRequest> requests;
    private SearchView fieldSearchFriend;
    private RecyclerView friendsRecycler;
    private final int LIMIT = 8;
    boolean isFetchingData = false;

    private FriendRecyclerAdapter mAdapter;
    private FragmentFriendsBinding mBinding;


    public FriendsFragment() {}

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFriendsBinding.inflate(
                inflater, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends, container, false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        users.clear();
        UserRepository.lastFetchedSnapshot = null;
        fetchData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        UserRepository.lastFetchedSnapshot = null;
        fieldSearchFriend = view.findViewById(R.id.field_search_friend);
        friendsRecycler = view.findViewById(R.id.recycler_friends);
        initializeRecycler();
//        fetchData();
        fieldSearchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                final List<User> filteredModelList = filter(users, query);
                mAdapter.setUsers((ArrayList<User>) filteredModelList);
                mAdapter.notifyDataSetChanged();
//                mAdapter.replaceAll(filteredModelList);
                mBinding.recyclerFriends.scrollToPosition(0);
                return true;
            }
        });
    }

    private void fetchData() {
//        requests = new ArrayList<>();
        UserRepository.getRelatedRequest(FirebaseAuth.getInstance().getUid(), relatedRequests -> {
            mAdapter.setRequests(relatedRequests);
            mAdapter.notifyDataSetChanged();
        });

        UserRepository.getAllUser(LIMIT, fetchedUsers -> {
            if(!fetchedUsers.isEmpty()) {
                for (User user:
                     fetchedUsers) {
                    Log.d("FETCHED DATA | USER", user.getUserID());
                    if(!user.getUserID().equals(FirebaseAuth.getInstance().getUid())) {
                        users.add(user);
                    }
                }
                mAdapter.setUsers(users);
                mAdapter.notifyDataSetChanged();
            }
            isFetchingData = false;
        });
    }

    private void initializeRecycler() {
        mAdapter = new FriendRecyclerAdapter(users, requests, getActivity(), R.layout.friend_item);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        friendsRecycler.setHasFixedSize(true);
        friendsRecycler.setAdapter(mAdapter);
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) friendsRecycler.getLayoutManager();
////                int totalItem = linearLayoutManager.getItemCount();
//        int totalItem = users.size();
//        int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//        Log.d("Recycler STAT", "Last Visible: " + lastVisible + " | Total Item: " + totalItem + " | OFFSET: " + OFFSET);
//        fetchData();

        friendsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                Log.d("Recycler Listener", "onScrolled");
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int totalItem = linearLayoutManager.getItemCount();
                int totalItem = users.size();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                Log.d("RECYCLER STATS", "TOTAL ITEM: " + totalItem + " | LAST VISIBLE: " + lastVisible);

                if(lastVisible >= totalItem - 2) {
//                    Log.d("RECYCLER STATS", "ADA PERMINTAAN FETCHING");
                    if(!isFetchingData) {
                        isFetchingData = true;
                        fetchData();
                    }
                }
            }
        });
    }
}