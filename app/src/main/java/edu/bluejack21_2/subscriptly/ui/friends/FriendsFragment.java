package edu.bluejack21_2.subscriptly.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.FriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.databinding.FragmentFriendsBinding;
import edu.bluejack21_2.subscriptly.models.User;


public class FriendsFragment extends Fragment {

    private static final Comparator<User> ALPHABETICAL_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            return a.getUsername().compareTo(b.getUsername());
        }
    };
    private SearchView fieldSearchFriend;
    private ArrayList<User> users;
    private RecyclerView friendsRecycler;
    private FriendRecyclerAdapter mAdapter;
    private FragmentFriendsBinding mBinding;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        Log.d("FLOW", "newInstance");
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FLOW", "onCreateView");

        mBinding = FragmentFriendsBinding.inflate(
                inflater, container, false);
        Log.d("BINDINGFriendsFragment", mBinding + "");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends, container, false);

        return rootView;
//        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("FLOW", "onViewCreated");
        fieldSearchFriend = view.findViewById(R.id.field_search_friend);
        friendsRecycler = view.findViewById(R.id.recycler_friends);

        users = new ArrayList<>();
        SubscriptlyDB.getDB().collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                ArrayList<String> memberIDs = (ArrayList<String>)document.get("members");
//                                for (String id:
//                                     memberIDs) {
//                                    UserRepository.getUser(id, fragment);
//                                }
//                                ArrayList<User> members = (ArrayList<User>)document.get("members");
//                                Log.d("Members", document.get("members").getClass().toString());
                            users.add(new User(
                                    document.getId(),
                                    document.getString("name"),
                                    document.getString("username"),
                                    document.getString("email"),
                                    ""));
                            Log.d("UserData", document.getString("name"));
                            setRecyclerView(users, friendsRecycler);
                        }
                    } else {

                    }
                    Log.d("FLOW", "beforefieldSearchFriend");
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

    private void setRecyclerView(ArrayList<User> data, RecyclerView recyclerView) {
        Log.d("FLOW", "setRecyclerView");
        mAdapter = new FriendRecyclerAdapter(data, ALPHABETICAL_COMPARATOR, getActivity(), R.layout.friend_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        mAdapter.add(data);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.d("FLOW", "onCreate");
//        super.onCreate(savedInstanceState);
//    }
}