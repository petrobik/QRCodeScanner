package com.bikshanov.qrcodescanner;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private CodeViewModel mCodeViewModel;
    private RecyclerView mRecyclerView;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        final CodeAdapter adapter = new CodeAdapter();
        mRecyclerView.setAdapter(adapter);

        mCodeViewModel = ViewModelProviders.of(getActivity()).get(CodeViewModel.class);
        mCodeViewModel.getAllCodes().observe(this, new Observer<List<Code>>() {
            @Override
            public void onChanged(List<Code> codes) {
                // update RecyclerView
                adapter.setCodes(codes);
//                mSnackbar = Snackbar.make(findViewById(R.id.main_layout), "onChanged", Snackbar.LENGTH_SHORT);
//                mSnackbar.setAnchorView(R.id.fab);
//                mSnackbar.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
