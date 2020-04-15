package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsers extends Fragment {


    @Bind(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;
    int positionInList;


    public FragmentUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        final FragmentUsersMyAdapter adapter = new FragmentUsersMyAdapter(generateSimpleListAllUsers());
        RecyclerView recyclerView = (RecyclerView) usersRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            // Nie używane przeciąganie
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {


                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                positionInList = viewHolder.getAdapterPosition();

                deleteUserDialog(positionInList);


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(usersRecyclerView);


        return view;
    }

    public void deleteUserDialog(final int positionInList)
    {

        AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(getActivity());

        alertDialogDeleteUser.setTitle(R.string.deleteUser);
        alertDialogDeleteUser.setMessage(R.string.deleteUserMessage);
        alertDialogDeleteUser.setIcon(R.drawable.ic_logo);


        alertDialogDeleteUser.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        int userID;
                        String userLogin;
                        DataBaseMain bd = new DataBaseMain(getActivity());
                        Cursor kurs = bd.giveUserIDAndLogin();

                        if (kurs.getCount() != 0)
                        {
                            kurs.moveToPosition(positionInList);
                            userID = kurs.getInt(0);
                            userLogin = kurs.getString(1);

                            if(!userLogin.equals("Admin"))
                            {
                                bd.deleteUser(userID, userLogin);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(FragmentUsers.this).attach(FragmentUsers.this).commit();
                            }
                            else
                            {

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(FragmentUsers.this).attach(FragmentUsers.this).commit();
                                dialog.cancel();
                                deleteAdminDialog();
                            }


                        }
                        dialog.cancel();

                    }
                });


        alertDialogDeleteUser.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(FragmentUsers.this).attach(FragmentUsers.this).commit();
                        dialog.cancel();
                    }
                });


        alertDialogDeleteUser.show();
    }



    public void deleteAdminDialog()
    {

        AlertDialog.Builder alertDialogDeleteAdmin = new AlertDialog.Builder(getActivity());

        alertDialogDeleteAdmin.setTitle(R.string.deleteUser);
        alertDialogDeleteAdmin.setMessage(R.string.removeAdminMessage);
        alertDialogDeleteAdmin.setIcon(R.drawable.ic_logo);


        alertDialogDeleteAdmin.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });



        alertDialogDeleteAdmin.show();
    }


    private List<FragmentUsersMyViewModel> generateSimpleListAllUsers() {

        List<FragmentUsersMyViewModel> simpleViewModelList = new ArrayList<>();


        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllUsers();

        if (kurs.getCount() != 0) {

            kurs.moveToFirst();
            do {


                String nick = kurs.getString(10);
                String nameAndSurname = kurs.getString(1) + " " + kurs.getString(2);

                simpleViewModelList.add(new FragmentUsersMyViewModel(nick, nameAndSurname));


                // Dopóki kursor może poruszać się w dół (po wynikach)
            } while (kurs.moveToNext());
        }

        return simpleViewModelList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
