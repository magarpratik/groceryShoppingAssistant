package com.example.groceryapp.viewModel;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

// A dialog box that appears on the bottom of the screen
public class AddNewListViewModel extends BottomSheetDialogFragment {

    // TAG to identify the specific dialog fragment
    public static final String TAG = "AddListBottomDialog";

    // declare UI elements
    private EditText newListText;
    private Button newListSaveButton;

    private DatabaseHandler db;

    // returns an object of the AddNewList class so that it can be used in the MainActivity
    public static AddNewListViewModel newInstance() {
        return new AddNewListViewModel();
    }

    /*Bundles are generally used for passing data between various Android activities.
    It depends on you what type of values you want to pass,
    but bundles can hold all types of values and pass them to the new activity.*/

    // savedInstanceState checks if the instance of this fragment exists in the memory
    // or in the previous
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // Initialise the view for the new list
        View view = inflater.inflate(R.layout.new_list, container, false);

        // Readjusts the size of the bottom sheet dialog fragment, when text is typed
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    // define all the code that is necessary for the functions in the dialog fragment
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // define the UI elements
        newListText = getView().findViewById(R.id.newListText);
        newListSaveButton = getView().findViewById(R.id.newListSaveButton);

        // define the database
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        // newListSaveButton functionality
        // Save a new list or update (rename) an existing list

        // isUpdate will check if a new list is being added or updated
        boolean isUpdate = false;

        // pass data to fragment
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String listName = bundle.getString("LIST_NAME");
            newListText.setText(listName);

            // check if listName is empty or not
            if(listName.length() > 0) {
                newListSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
            }
        }

        // Listener to check if the text changed in the EditText
        // Save button will be enabled only after text is entered in the EditText
        newListText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if EditText is empty, Save button is disabled to prevent inserting empty string into db
                if(s.toString().equals("")) {
                    newListSaveButton.setEnabled(false);
                    newListSaveButton.setTextColor(Color.GRAY);
                }
                else {
                    newListSaveButton.setEnabled(true);
                    newListSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        // Listener for the SAVE button
        boolean finalIsUpdate = isUpdate;
        newListSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = newListText.getText().toString();

                if(!listName.equals("")) {
                    if(finalIsUpdate) {
                        db.updateList(bundle.getInt("ID"), listName);
                    }
                    else {
                        ShoppingListModel shoppingList = new ShoppingListModel(listName);
                        db.addNewList(shoppingList);
                    }
                    // dismiss the bottom sheet dialog fragment
                    dismiss();
                }
            }
        });
    }

    // refreshes and updates the recyclerview
    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
