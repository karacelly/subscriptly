package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.PaySubscriptionActivity;
import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.ui.subscriptions.SubscriptionsFragment;

public class SubscriptionDetailFragment extends Fragment {
    private Subscription subscription;
    private ImageButton button, uploadReceiptButton;
    private ImageView subsPhoto;
    private TextView subsName, menuMembers, menuHistory, menuMedia;

    private Typeface outfitMedium, outfitSemiBold, outfitBold;

    public SubscriptionDetailFragment() { }

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
        initFonts(view);
        initComponents(view);
        setDataOnView(view);
        createMenu(view);
        Log.d("SUBSCRIPTION MEMBERS", subscription.getMembers().size()+"");
        setFragment(new SubscriptionDetailMemberFragment(subscription.getMembers()));
    }

    private void initFonts(View v) {
        outfitMedium = ResourcesCompat.getFont(v.getContext(), R.font.outfit_medium);
        outfitSemiBold = ResourcesCompat.getFont(v.getContext(), R.font.outfit_semi_bold);
        outfitBold = ResourcesCompat.getFont(v.getContext(), R.font.outfit_bold);
    }

    public void initComponents(View v) {
        subsPhoto = v.findViewById(R.id.subs_photo);
        subsName = v.findViewById(R.id.subs_name);

        uploadReceiptButton = v.findViewById(R.id.action_upload_receipt);
        uploadReceiptButton.setOnClickListener(view -> {
            Intent uploadIntent = new Intent(getContext(), PaySubscriptionActivity.class);
            startActivity(uploadIntent);
        });


        menuMembers = v.findViewById(R.id.text_menu_members);
        menuHistory = v.findViewById(R.id.text_menu_history);
        menuMedia = v.findViewById(R.id.text_menu_media);
    }

    private void resetTypeFace(Typeface tf) {
        menuMembers.setTypeface(tf);
        menuHistory.setTypeface(tf);
        menuMedia.setTypeface(tf);
    }

    private void resetBackground(){
        menuMembers.setBackgroundResource(0);
        menuHistory.setBackgroundResource(0);
        menuMedia.setBackgroundResource(0);
    }

    public void setDataOnView(View v){
        Glide.with(getContext()).load(subscription.getImage()).into(subsPhoto);

        menuMembers.setTypeface(outfitBold);
        subsName.setText(subscription.getName());

        menuMembers.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));

        menuMembers.setOnClickListener(view -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuMembers.setTypeface(outfitBold);

            menuMembers.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new SubscriptionDetailMemberFragment(subscription.getMembers()));
        });

        menuHistory.setOnClickListener(view -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuHistory.setTypeface(outfitBold);

            menuHistory.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new SubscriptionDetailHistoryFragment(subscription));
        });

        menuMedia.setOnClickListener(view -> {
            resetTypeFace(outfitMedium);
            resetBackground();
            menuMedia.setTypeface(outfitBold);

            menuMedia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_white));
            setFragment(new SubscriptionDetailMediaFragment(subscription));
        });
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

    public void setFragment(Fragment f){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.subs_detail_fragment_placeholder, f);
        ft.commit();
    }

}