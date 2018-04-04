package group10.partyfinder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group10.partyfinder.PartyListFragment.OnListFragmentInteractionListener;


import java.text.ParseException;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Party} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyPartyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPartyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Party> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int list;

    public MyPartyItemRecyclerViewAdapter(List<Party> items, OnListFragmentInteractionListener listener, int list) {
        mValues = items;
        mListener = listener;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_partyitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());

        if (list == 1) {
            holder.mContentView.setText(String.valueOf(mValues.get(position).getDistance()) + " km");
        }
        if (list == 2) {
            try {
                if (Integer.parseInt(mValues.get(position).getPartyDayDiff()) > 0) {
                    holder.mContentView.setText("in " + String.valueOf(mValues.get(position).getPartyDayDiff()) + " days");
                } else {
                    holder.mContentView.setText("today");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
                Intent i = new Intent("android.intent.action.PartyView");
                i.putExtra("ID", holder.mItem.getId());
                //i.putExtra("ID", 2);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Party mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
}
