package com.example.groceryapp.viewModel;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// A dialog box that appears on the bottom of the screen
public class AddNewItemViewModel extends BottomSheetDialogFragment {

    // TAG to identify the specific dialog fragment
    public static final String TAG = "AddItemBottomDialog";

    private EditText editTextItemName;
    private EditText editTextItemQty;
    private Spinner unitSpinner;
    private Button newItemSaveButton;

    private DatabaseHandler db;

    // returns an object of the AddNewItem class so that it can be used in the MainActivity
    public static AddNewItemViewModel newInstance() { return new AddNewItemViewModel(); }

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
        View view = inflater.inflate(R.layout.new_item, container, false);

        // Readjusts the size of the bottom sheet dialog fragment, when text is typed
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    // define all the code that is necessary for the functions in the dialog fragment
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // define the UI elements
        editTextItemName = getView().findViewById(R.id.editTextItemName);
        editTextItemQty = getView().findViewById(R.id.editTextItemQty);
        unitSpinner = getView().findViewById(R.id.unitSpinner);
        newItemSaveButton = getView().findViewById(R.id.newItemSaveButton);

        // populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        // define the database
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        // newItemSaveButton functionality
        // Save a new item or update (rename) an existing item

        // isUpdate will check if a new list is being added or updated
        boolean isUpdate = false;

        // pass data to fragment
        final Bundle bundle = getArguments();

        if(!bundle.getString("ITEM_NAME").equals("")) {
            isUpdate = true;
            String itemName = bundle.getString("ITEM_NAME");
            editTextItemName.setText(itemName);
            String itemQty = bundle.getString("ITEM_QTY");
            editTextItemQty.setText(itemQty);
            String itemUnit = bundle.getString("ITEM_UNIT");
            unitSpinner.setSelection(getIndex(unitSpinner, itemUnit));


            // check if itemName is empty or not
            if(itemName.length() > 0) {
                newItemSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
            }
        }

        // Listener to check if the text changed in the EditText
        // Save button will be enabled only after text is entered in the EditText
        editTextItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if EditText is empty, Save button is disabled to prevent inserting empty string into db
                if(s.toString().equals("")) {
                    newItemSaveButton.setEnabled(false);
                    newItemSaveButton.setTextColor(Color.GRAY);
                }
                else {
                    newItemSaveButton.setEnabled(true);
                    newItemSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        // Listener for the SAVE button
        boolean finalIsUpdate = isUpdate;
        newItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = editTextItemName.getText().toString();
                String itemQty = editTextItemQty.getText().toString();
                String itemUnit = unitSpinner.getSelectedItem().toString();
                int itemId = bundle.getInt("ITEM_ID");

                if(!itemName.equals("") && itemQty.equals("")) {
                    if(finalIsUpdate) {
                        db.updateItem(itemId, itemName);
                    }
                    else {
                        ItemModel itemModel = new ItemModel(itemId, itemName);
                        db.addNewItem(itemModel);
                    }
                    // dismiss the bottom sheet dialog fragment
                    dismiss();
                }
                else if (!itemName.equals("") && !itemQty.equals("")){
                    if(finalIsUpdate) {
                        db.updateItem(itemId, itemName, itemQty, itemUnit);
                    }
                    else {
                        ItemModel itemModel = new ItemModel(itemId, itemName, itemQty, itemUnit);
                        db.addNewItem(itemModel);
                    }
                    // dismiss the bottom sheet dialog fragment
                    dismiss();
                }
            }
        });
    }

    // set the spinner to the appropriate value if you're editing the item
    private int getIndex(Spinner unitSpinner, String itemUnit) {
        for (int i = 0; i < unitSpinner.getCount(); i++) {
            if (unitSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(itemUnit)) {
                return i;
            }
        }
        return 0;
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
