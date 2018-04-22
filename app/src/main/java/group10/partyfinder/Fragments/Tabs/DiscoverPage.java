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
            //EventBus.getDefault().postSticky(new Event(3));
            insertNestedFragmentList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_page,container,false);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

       // insertNestedFragmentList();
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        EventBus.getDefault().postSticky(new Event(3));
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragmentList() {
        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 2);
        Fragment childFragment = new PartyListFragment();
        childFragment.setArguments(data);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        try {transaction.replace(R.id.list_fragment_container, childFragment).commit();}
        catch (IllegalStateException e) {}
    }

}
