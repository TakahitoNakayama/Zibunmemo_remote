package com.websarva.wings.android.dowasurememo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


/**
 * カレンダーを使って選択された日付をEdittextにセットするクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;

    public DatePickerFragment(EditText e1, EditText e2, EditText e3) {
        etYear = e1;
        etMonth = e2;
        etDay = e3;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        etYear.setText("" + year);
        etMonth.setText("" + (month + 1));
        etDay.setText("" + day);
    }
}

/**
 * カレンダーアイコンのImageButtonがクリックされたときに呼ばれるクラス
 *
 * @author nakayama
 * @version 1.0
 */
class DatePickerListener implements View.OnClickListener {

    private Context context;
    private androidx.fragment.app.FragmentManager manager;
    private String table;

    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;

    public DatePickerListener(Context _context, androidx.fragment.app.FragmentManager _manager, String _table) {
        context = _context;
        manager = _manager;
        table = _table;
    }

    @Override
    public void onClick(View v) {

        LinearLayout parentView = (LinearLayout) v.getParent();

        if (table == "date1") {
            etYear = parentView.findViewById(R.id.et_date_year);
            etMonth = parentView.findViewById(R.id.et_date_month);
            etDay = parentView.findViewById(R.id.et_date_day);

        } else if (table == "update1") {
            etYear = parentView.findViewById(R.id.et_update_year);
            etMonth = parentView.findViewById(R.id.et_update_month);
            etDay = parentView.findViewById(R.id.et_update_day);
        }

        Toast.makeText
                (context,
                        "西暦の変更は左上に薄く表示されている西暦の箇所をタップしてください",
                        Toast.LENGTH_LONG).show();

        DatePickerFragment datePicker =
                new DatePickerFragment(etYear, etMonth, etDay);
        datePicker.show(manager, "datePicker");

    }
}