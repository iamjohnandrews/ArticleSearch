package codepath.articlesearch;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;

import Model.SearchCriteria;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedSearchFragment extends DialogFragment {

    public SearchCriteria advancedSearchCriteria;
    CheckBox checkbox_oldest;
    CheckBox checkbox_newest;
    Spinner spinnerCategory;
    DatePicker datePicker;

    public AdvancedSearchFragment() {
        // Required empty public constructor
    }

    public interface AdvancedSearchListener {
        void onCompletedUserInput(SearchCriteria criteria);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        advancedSearchCriteria = new SearchCriteria();

        return inflater.inflate(R.layout.fragment_advanced_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accessViews(view);
        setCurrentDateOnDatePicker();
        setupCheckBoxListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        advancedSearchCriteria.category = spinnerCategory.getSelectedItem().toString();
        AdvancedSearchListener listener = (AdvancedSearchListener) getActivity();
        listener.onCompletedUserInput(advancedSearchCriteria);
    }

    private void accessViews(View view) {
        checkbox_oldest = (CheckBox) view.findViewById(R.id.checkbox_oldest);
        checkbox_newest = (CheckBox) view.findViewById(R.id.checkbox_newest);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
    }

    private void setCurrentDateOnDatePicker() {
        final Calendar today = Calendar.getInstance();
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                advancedSearchCriteria.beginDate = Integer.toString(i) + formatDateNYTimesStyle(i1+1) + formatDateNYTimesStyle(i2);
            }
        });
    }

    private String formatDateNYTimesStyle (int i) {
        String dateString;
        if (i < 10) {
            dateString = "0" + Integer.toString(i);
        } else  {
            dateString = Integer.toString(i);
        }
        return dateString;
    }

    private void setupCheckBoxListener() {
        CheckBox.OnCheckedChangeListener checkedChangeListener = new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.checkbox_oldest:
                        if (b) {
                            advancedSearchCriteria.sort = "oldest";
                            checkbox_newest.setChecked(false);
                        }
                        break;
                    case R.id.checkbox_newest:
                        if (b) {
                            advancedSearchCriteria.sort = "newest";
                            checkbox_oldest.setChecked(false);
                        }
                        break;
                }
            }
        };
        checkbox_oldest.setOnCheckedChangeListener(checkedChangeListener);
        checkbox_newest.setOnCheckedChangeListener(checkedChangeListener);
    }

}
