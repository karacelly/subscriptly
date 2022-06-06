package edu.bluejack21_2.subscriptly.ui.friends;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import edu.bluejack21_2.subscriptly.ui.friends_tab.AddFriendsFragment;
import edu.bluejack21_2.subscriptly.ui.friends_tab.FriendsExistingFragment;
import edu.bluejack21_2.subscriptly.ui.friends_tab.FriendsPendingFragment;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailHistoryFragment;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailMediaFragment;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailMemberFragment;
import edu.bluejack21_2.subscriptly.utils.FontHelper;


public class FriendsFragment extends Fragment {

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<FriendRequest> requests;

    private RecyclerView friendsRecycler;
    private final int LIMIT = 8;
    boolean isFetchingData = false;
    private TextView menuFriends, menuPending, menuAddFriend;

    private FriendRecyclerAdapter adapter;
    private FragmentFriendsBinding mBinding;

    private Typeface outfitMedium, outfitSemiBold, outfitBold;


    private void resetTypeFace(Typeface tf) {
        menuFriends.setTypeface(tf);
        menuPending.setTypeface(tf);
        menuAddFriend.setTypeface(tf);
    }

    private void resetBackground(){
        menuFriends.setBackgroundResource(0);
        menuPending.setBackgroundResource(0);
        menuAddFriend.setBackgroundResource(0);
    }

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
//        fetchData();
    }

    private void initFonts() {
        outfitMedium = FontHelper.getOutfitMedium(getContext());
        outfitSemiBold = FontHelper.getOutfitSemiBold(getContext());
        outfitBold = FontHelper.getOutfitBold(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initFonts();
        UserRepository.lastFetchedSnapshot = null;

        friendsRecycler = view.findViewById(R.id.recycler_friends);

        menuFriends = view.findViewById(R.id.text_menu_friends);
        menuPending = view.findViewById(R.id.text_menu_pending);
        menuAddFriend = view.findViewById(R.id.text_menu_add_friend);

        menuFriends.setTypeface(outfitBold);

        menuFriends.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));

        menuFriends.setOnClickListener(v -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuFriends.setTypeface(outfitBold);

            menuFriends.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new FriendsExistingFragment());
        });

        menuFriends.performClick();

        menuPending.setOnClickListener(v -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuPending.setTypeface(outfitBold);

            menuPending.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new FriendsPendingFragment());
        });

        menuAddFriend.setOnClickListener(v -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuAddFriend.setTypeface(outfitBold);

            menuAddFriend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new AddFriendsFragment());
        });

//        initializeRecycler();
//
//        fieldSearchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String query) {
//                final List<User> filteredModelList = filter(users, query);
//                adapter.setUsers((ArrayList<User>) filteredModelList);
//                adapter.notifyDataSetChanged();
////                adapter.replaceAll(filteredModelList);
//                mBinding.recyclerFriends.scrollToPosition(0);
//                return true;
//            }
//        });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.friend_fragment_placeholder, fragment);
        ft.commit();
    }

//    private void fetchData() {
//        Log.d("FriendsFragment", "FetchData");
////        requests = new ArrayList<>();
//
//
//        UserRepository.getAllUser(LIMIT, fetchedUsers -> {
//            Log.d("FriendsFragment", "FetchData");
//            if(!fetchedUsers.isEmpty()) {
//                for (User user:
//                     fetchedUsers) {
//                    Log.d("FETCHED DATA | USER", user.getUserID());
//                    if(!user.getUserID().equals(FirebaseAuth.getInstance().getUid())) {
//                        users.add(user);
//                    }
//                }
//                adapter.setUsers(users);
//                adapter.notifyDataSetChanged();
//            }
//            isFetchingData = false;
//        });
//    }

//    private void initializeRecycler() {
//        adapter = new FriendRecyclerAdapter(users, requests, getActivity(), R.layout.friend_item);
//        friendsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        friendsRecycler.setHasFixedSize(true);
//        friendsRecycler.setAdapter(adapter);
//
//        fetchData();
//
//        friendsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
////                super.onScrolled(recyclerView, dx, dy);
//                Log.d("Recycler Listener", "onScrolled");
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
////                int totalItem = linearLayoutManager.getItemCount();
//                int totalItem = users.size();
//                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
////                Log.d("RECYCLER STATS", "TOTAL ITEM: " + totalItem + " | LAST VISIBLE: " + lastVisible);
//
//                if(lastVisible >= totalItem - 2) {
////                    Log.d("RECYCLER STATS", "ADA PERMINTAAN FETCHING");
//                    if(!isFetchingData) {
//                        isFetchingData = true;
//                        fetchData();
//                    }
//                }
//            }
//        });
//    }
}