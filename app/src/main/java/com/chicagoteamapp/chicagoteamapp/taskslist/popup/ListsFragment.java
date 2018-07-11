package com.chicagoteamapp.chicagoteamapp.taskslist.popup;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chicagoteamapp.chicagoteamapp.MyApp;
import com.chicagoteamapp.chicagoteamapp.R;
import com.chicagoteamapp.chicagoteamapp.data.model.MyList;
import com.chicagoteamapp.chicagoteamapp.util.ViewUtil;
import com.furianrt.bottompopupwindow.BottomPopupWindow;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chicagoteamapp.chicagoteamapp.taskslist.popup.ListsListAdapter.OnListInteractionListener;

public class ListsFragment extends Fragment {

    @BindView(R.id.button_add_list)
    Button mButtonAddList;

    @BindView(R.id.list_lists)
    MaxHeightRecyclerView mRecyclerView;

    @BindView(R.id.edit_text_new_list)
    EditText mEditTextNewList;

    private ListsListAdapter mAdapter;
    private OnListInteractionListener mListener = new OnListInteractionListener() {

        @Override
        public void onListItemClick(MyList list) {

        }

        @Override
        public void onListItemLongClick(MyList list) {
            MyApp.getInstance()
                    .getDatabase()
                    .listDao()
                    .delete(list);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        ButterKnife.bind(this, view);

        mAdapter  = new ListsListAdapter(mListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyApp.getInstance()
                .getDatabase()
                .listDao()
                .getAllLists()
                .observe(this, lists -> mAdapter.submitList(lists));

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @OnClick(R.id.button_add_list)
    public void onButtonAddListClick() {
        MyList list = new MyList(mEditTextNewList.getText().toString());
        mEditTextNewList.getText().clear();
        MyApp.getInstance()
                .getDatabase()
                .listDao()
                .insert(list);
    }

    @OnClick(R.id.button_profile)
    public void onButtonProfileClick() {
        ProfileFragment fragment = new ProfileFragment();
        BottomPopupWindow popupWindow =
                Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_popup_window);
        popupWindow.setFragment(fragment);
        popupWindow.show();
    }

    @OnClick(R.id.image_button_close)
    public void onButtonCloseClick() {
        ViewUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
        BottomPopupWindow popupWindow =
                Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_popup_window);
        popupWindow.hide();
    }
}
