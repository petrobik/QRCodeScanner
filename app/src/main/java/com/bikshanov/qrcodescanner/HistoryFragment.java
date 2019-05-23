package com.bikshanov.qrcodescanner;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Snackbar mSnackbar;
    private CodeAdapter mCodeAdapter;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mCodeAdapter = new CodeAdapter();
        mRecyclerView.setAdapter(mCodeAdapter);

        mCodeViewModel = ViewModelProviders.of(getActivity()).get(CodeViewModel.class);
        mCodeViewModel.getAllCodes().observe(this, new Observer<List<Code>>() {
            @Override
            public void onChanged(List<Code> codes) {
                // update RecyclerView
                mCodeAdapter.setCodes(codes);
//                mSnackbar = Snackbar.make(findViewById(R.id.main_layout), "onChanged", Snackbar.LENGTH_SHORT);
//                mSnackbar.setAnchorView(R.id.fab);
//                mSnackbar.show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mCodeViewModel.delete(mCodeAdapter.getCodeAt(viewHolder.getAdapterPosition()));
                mSnackbar = Snackbar.make(getActivity().findViewById(R.id.main_layout), getResources().getString(R.string.item_deleted), Snackbar.LENGTH_SHORT);
                mSnackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                mSnackbar.show();
            }
        }).attachToRecyclerView(mRecyclerView);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_history:
                if (mCodeAdapter.getItemCount() != 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.clear_history))
                            .setMessage(getString(R.string.clear_history_message))
                            .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mCodeViewModel.deleteAllCodes();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}