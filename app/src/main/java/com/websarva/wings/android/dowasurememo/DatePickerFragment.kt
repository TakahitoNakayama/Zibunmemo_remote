package com.websarva.wings.android.dowasurememo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

/**
 * カレンダーを使って選択された日付をEdittextにセットするクラス
 *
 * @author nakayama
 * @version 1.0
 */
class DatePickerFragment(val etYear: EditText, val etMonth: EditText, val etDay: EditText) : DialogFragment(), OnDateSetListener {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        etYear.setText("${year}")
        etMonth.setText("${month + 1}")
        etDay.setText("${day}")
    }
}

/**
 * カレンダーアイコンのImageButtonがクリックされたときに呼ばれるクラス
 *
 * @author nakayama
 * @version 1.0
 */
internal class DatePickerListener(val context: Context, val manager: FragmentManager, val table: String) : View.OnClickListener {

    private lateinit var etYear: EditText
    private lateinit var etMonth: EditText
    private lateinit var etDay: EditText

    override fun onClick(v: View) {
        val parentView = v.parent as LinearLayout
        if (table === "date1") {
            etYear = parentView.findViewById(R.id.et_date_year)
            etMonth = parentView.findViewById(R.id.et_date_month)
            etDay = parentView.findViewById(R.id.et_date_day)
        } else if (table === "update1") {
            etYear = parentView.findViewById(R.id.et_update_year)
            etMonth = parentView.findViewById(R.id.et_update_month)
            etDay = parentView.findViewById(R.id.et_update_day)
        }
        Toast.makeText(context,
                "西暦の変更は左上に薄く表示されている西暦の箇所をタップしてください",
                Toast.LENGTH_LONG).show()
        val datePicker = DatePickerFragment(etYear, etMonth, etDay)
        datePicker.show(manager, "datePicker")
    }
}