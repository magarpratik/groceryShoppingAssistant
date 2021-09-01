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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewSavedItemViewModel extends BottomSheetDialogFragment {
    public static final String TAG = "AddSavedItemBottomDialog";

    private EditText itemName;
    private EditText itemQty;
    private EditText itemPrice;
    private Spinner spinner;
    private Button saveButton;

    private DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // Initialise the view for the new list
        View view = inflater.inflate(R.layout.new_saved_item, container, false);

        // Readjusts the size of the bottom sheet dialog fragment, when text is typed
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    // define all the code that is necessary for the functions in the dialog fragment
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemName = getView().findViewById(R.id.savedItemNameEditText);
        itemQty = getView().findViewById(R.id.savedItemQtyEditText);
        itemPrice = getView().findViewById(R.id.savedItemPriceEditText);
        saveButton = getView().findViewById(R.id.savedItemSaveButton);
        spinner = getView().findViewById(R.id.savedUnitSpinner);

        // populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // database
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();

        if(!bundle.getString("ITEM_NAME").equals("")) {
            isUpdate = true;
            String name = bundle.getString("ITEM_NAME");
            itemName.setText(name);
            String qty = bundle.getString("ITEM_QTY");
            itemQty.setText(qty);
            String unit = bundle.getString("ITEM_UNIT");
            spinner.setSelection(getIndex(spinner, unit));
            String price = bundle.getString("ITEM_PRICE");
            itemPrice.setText(price);

            // check if itemName is empty or not
            if(name.length() > 0) {
                saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
            }
        }

        // Listener to check if the text changed in the EditText
        // Save button will be enabled only after text is entered in the EditText
        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if EditText is empty, Save button is disabled to prevent inserting empty string into db
                if(s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                }
                else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary3));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        // Listener for the SAVE button
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemName.getText().toString();
                String qty = itemQty.getText().toString();
                String unit = spinner.getSelectedItem().toString();
                String price = itemPrice.getText().toString();
                int itemId = bundle.getInt("ITEM_ID");
                int listId = bundle.getInt("LIST_ID");
                int storeId = bundle.getInt("STORE_ID");

                if(!itemName.equals("")) {
                    if(finalIsUpdate) {
                        db.updateSavedItem(itemId, listId, storeId,
                                name, qty, unit, price);
                    }
                    else {
                        ItemModel itemModel = new ItemModel(listId, name);
                        itemModel.setPrice(price);
                        itemModel.setId(itemId);
                        itemModel.setStoreId(storeId);
                        itemModel.setWeight(qty + unit);
                        db.addNewSavedItem(itemModel);
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
