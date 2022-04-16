package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.ui.subscriptions.SubscriptionsFragment;

public class SubscriptionDetailFragment extends Fragment {
    private Subscription subscription;
    private ImageButton button;

    public SubscriptionDetailFragment() {
    }

    public SubscriptionDetailFragment(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_detail, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createMenu(view);
    }

    public void createMenu(View v){
        button = v.findViewById(R.id.subs_detail_menu);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(getActivity(), button);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.subs_detail_menu, popupMenu.getMenu());
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }
}