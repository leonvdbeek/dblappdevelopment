package group10.partyfinder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by LyoubomirKatzarov on 3/19/2018.
 */

public class DiscoverPage extends Fragment {

    private static final String TAG = "Discover";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_page,container,false);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insertNestedFragmentMap();
        insertNestedFragmentList();
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragmentMap() {
        Bundle data = new Bundle();//create bundle instance
        data.putInt("map", 2);
        Fragment childFragment = new MapsActivity();
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
