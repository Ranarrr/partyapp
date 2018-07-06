package com.partyspottr.appdir.ui.mainfragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.mainfragments.chatchildfragments.mine_chats_fragment;
import com.partyspottr.appdir.ui.mainfragments.chatchildfragments.venner_fragment;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class chatfragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppCompatButton mine_chats = view.findViewById(R.id.mine_chats);
        final AppCompatButton venner = view.findViewById(R.id.venner);

        if(getActivity() == null)
            return;

        ImageButton search_chat = getActivity().findViewById(R.id.search_events);
        search_chat.setImageDrawable(getResources().getDrawable(R.drawable.search));

        ((TextView) getActivity().findViewById(R.id.title_toolbar)).setText("Messages"); // TODO : Translation

        venner.setTypeface(typeface);
        mine_chats.setTypeface(typeface);

        Fragment chatFragment = getActivity().getSupportFragmentManager().findFragmentByTag(chatfragment.class.getName());

        if(chatFragment != null) {
            final ViewPager viewPager = getActivity().findViewById(R.id.viewpager_chat);
            final ImageView arrow_chatfragment = view.findViewById(R.id.arrow_chatfragment);

            if(viewPager != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch(viewPager.getCurrentItem()) {
                            case 0:
                                ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", 0.0f).start();

                                Utilities.onSearchMineChats(getActivity());
                                break;

                            case 1:
                                ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", mine_chats.getWidth()).start();

                                Utilities.onSearchVenner(getActivity());
                                break;
                        }
                    }
                }, 250);

                PagerAdapter pagerAdapter = new chatfragment.ScreenSliderPagerAdapter(chatFragment.getChildFragmentManager());
                viewPager.setAdapter(pagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        ImageButton search_events = getActivity().findViewById(R.id.search_events);

                            switch(position) {
                                case 0:
                                    if(!ProfilActivity.childfragmentsinstackChat.contains(mine_chats_fragment.class.getName()))
                                        ProfilActivity.childfragmentsinstackChat.add(mine_chats_fragment.class.getName());
                                    else {
                                        ProfilActivity.childfragmentsinstackChat.remove(mine_chats_fragment.class.getName());
                                        ProfilActivity.childfragmentsinstackChat.add(mine_chats_fragment.class.getName());
                                    }

                                    break;
                                case 1:
                                    if(!ProfilActivity.childfragmentsinstackChat.contains(venner_fragment.class.getName()))
                                        ProfilActivity.childfragmentsinstackChat.add(venner_fragment.class.getName());
                                    else {
                                        ProfilActivity.childfragmentsinstackChat.remove(venner_fragment.class.getName());
                                        ProfilActivity.childfragmentsinstackChat.add(venner_fragment.class.getName());
                                    }

                                    break;
                            }

                        search_events.setVisibility(View.VISIBLE);



                        switch(position) {
                            case 0:
                                ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", 0.0f).start();

                                Utilities.onSearchMineChats(getActivity());
                                break;

                            case 1:
                                ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", mine_chats.getWidth()).start();

                                Utilities.onSearchVenner(getActivity());
                                break;
                        }
                    }
                });
            }
        }
    }

    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {
        ScreenSliderPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new mine_chats_fragment();
                case 1:
                    return new venner_fragment();
            }
            return new mine_chats_fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
