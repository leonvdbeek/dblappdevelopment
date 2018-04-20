package group10.partyfinder.Fragments.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import group10.partyfinder.Event;
import group10.partyfinder.Fragments.Components.MapsFragment;
import group10.partyfinder.Fragments.Components.PartyListFragment;
import group10.partyfinder.R;

/**
 * Created by LyoubomirKatzarov on 3/19/2018.
 */

public class DiscoverPage extends Fragment {

    private static final String TAG = "Discover";

    private boolean subscribe;

    @Override
    public void onStart() {
        super.onStart();
        if (!subscribe) {
            EventBus.getDefault().register(this);
            subscribe = true;
        }
    }

    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.event == 1) {
            EventBus.getDefault().postSticky(new Event(2));
            insertNestedFragmentList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_page,container,false);

       // insertNestedFragmentMap();

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        insertNestedFragmentList();
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragmentMap() {
        Bundle data = new Bundle();//create bundle instance
        data.putInt("map", 2);
        Fragment childFragment = new MapsFragment();
        childFragment.setArguments(data);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_fragment_container, childFragment).commit();
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragmentList() {
        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 2);
        Fragment childFragment = new PartyListFragment();
        childFragment.setArguments(data);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.list_fragment_container, childFragment).commit();
    }

}
