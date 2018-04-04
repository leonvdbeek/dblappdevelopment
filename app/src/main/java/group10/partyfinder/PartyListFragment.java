package group10.partyfinder;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PartyListFragment extends android.support.v4.app.Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListenerT;
    private OnListFragmentInteractionListener mListenerF;

    //get the instance of our database
    //private DBSnapshot DB = DBSnapshot.getInstance();
    private ArrayList<Party> partiesT;
    private ArrayList<Party> partiesF;
    int getArgument;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartyListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PartyListFragment newInstance(int columnCount) {
        PartyListFragment fragment = new PartyListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Party p1 = new Party(0, "name", "info", new Date(), "theme", "creator", "address", "0.500000", "0.200000");

        //Party p2 = new Party(0, "name2", "info2", new Date(), "theme2", "creator2", "address2", "0.5000002", "0.2000002");

        //parties = new ArrayList<>();
        //parties.add(p1);
        //parties.add(p2);


        getArgument = getArguments().getInt("list");//Get pass data with its key value

        if (getArgument == 1) {
            partiesT = new ArrayList<>(DBSnapshot.getInstance().getTodayParties());
            Collections.sort(partiesT, (a, b) -> a.getDistance() < b.getDistance() ? -1 : a.getDistance() == b.getDistance() ? 0 : 1);
        }
        if (getArgument == 2) {
            partiesF = new ArrayList<>(DBSnapshot.getInstance().getFutureParties());
            Collections.sort(partiesF, (a, b) -> a.getPartyTimeMs() < b.getPartyTimeMs() ? -1 : a.getPartyTimeMs() == b.getPartyTimeMs() ? 0 : 1);
        }

        //Todo find a way to reset the list content after the DB is loaded


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partyitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (getArgument == 1) {
            recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(partiesT, mListenerT));
            }
            if (getArgument == 2) {
            recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(partiesF, mListenerF));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            if (getArgument == 1) {
            mListenerT = (OnListFragmentInteractionListener) context;
            }
            if (getArgument == 2) {
            mListenerF = (OnListFragmentInteractionListener) context;
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getArgument == 1) {
            mListenerT = null;
        }
        if (getArgument == 2) {
            mListenerF = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Party item);
    }
}
