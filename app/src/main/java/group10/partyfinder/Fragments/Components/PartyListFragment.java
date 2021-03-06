package group10.partyfinder.Fragments.Components;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collections;

import group10.partyfinder.DataStructure.DBSnapshot;
import group10.partyfinder.DataStructure.MyPartyItemRecyclerViewAdapter;
import group10.partyfinder.DataStructure.Party;
import group10.partyfinder.R;

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
    private OnListFragmentInteractionListener mListener;

    //get the instance of our database
    //private DBSnapshot DB = DBSnapshot.getInstance();
    private ArrayList<Party> partiesT;
    private ArrayList<Party> partiesF;
    private ArrayList<Party> partiesS;
    private ArrayList<Party> partiesM;
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

        getArgument = getArguments().getInt("list");//Get pass data with its key value

        if (getArgument == 1) {
            partiesT = new ArrayList<>(DBSnapshot.getInstance().getTodayParties());
            Collections.sort(partiesT, (a, b) -> a.getDistance() < b.getDistance()
                    ? -1 : a.getDistance() == b.getDistance() ? 0 : 1);
        }
        if (getArgument == 2) {
            partiesF = new ArrayList<>(DBSnapshot.getInstance().getFutureParties());
            Collections.sort(partiesF, (a, b) -> a.getPartyTimeMs() < b.getPartyTimeMs()
                    ? -1 : a.getPartyTimeMs() == b.getPartyTimeMs() ? 0 : 1);
        }
        if (getArgument == 3) {
            partiesS = new ArrayList<>(DBSnapshot.getInstance().getSavedParties());
            Collections.sort(partiesS, (a, b) -> a.getPartyTimeMs() < b.getPartyTimeMs()
                    ? -1 : a.getPartyTimeMs() == b.getPartyTimeMs() ? 0 : 1);
        }
        if (getArgument == 4) {
            partiesM = new ArrayList<>(DBSnapshot.getInstance().getMyParties());
            Collections.sort(partiesM, (a, b) -> a.getPartyTimeMs() < b.getPartyTimeMs()
                    ? -1 : a.getPartyTimeMs() == b.getPartyTimeMs() ? 0 : 1);
        }

        //Todo find a way to reset the list content after the DB is loaded


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_partyitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(
                        context, mColumnCount));
            }
            if (getArgument == 1) {
            recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(
                    partiesT, mListener, 1));
            }
            if (getArgument == 2) {
            recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(
                    partiesF, mListener, 2));
            }
            if (getArgument == 3) {
                recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(
                        partiesS, mListener, 3));
            }
            if (getArgument == 4) {
                recyclerView.setAdapter(new MyPartyItemRecyclerViewAdapter(
                        partiesM, mListener, 4));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
