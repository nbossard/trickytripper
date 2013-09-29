package de.koelle.christian.trickytripper.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.actionbarsherlock.app.SherlockDialogFragment;
import de.koelle.christian.common.utils.Assert;
import de.koelle.christian.trickytripper.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialogFragment extends SherlockDialogFragment {

    private Calendar initialDate = new GregorianCalendar();
    private Calendar resultDate;
    private Date datePickerInitialDate;
    private Date resultDateTime;


    public interface DatePickerDialogCallback {
        Date getDatePickerInitialDate();
        int getDatePickerStringIdForTitle();
        void deliverDatePickerResult(Date pickedDate);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date datePickerInitialDate = getCallBack().getDatePickerInitialDate();
        if (datePickerInitialDate == null) {
            datePickerInitialDate = new Date();
        }

        initialDate = Calendar.getInstance();
        initialDate.setTime(datePickerInitialDate);

        resultDate = Calendar.getInstance();
        resultDate.setTime(datePickerInitialDate);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.date_picker_dialog_view, null);
        bindWidgets(view);
        builder
                .setTitle(getResources().getString(getCallBack().getDatePickerStringIdForTitle()))
                .setView(view)
                .setCancelable(true)
                .setNegativeButton(R.string.common_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatePickerDialogFragment.this.dismiss();
                    }
                })
                .setPositiveButton(R.string.common_button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        view.findViewById(R.id.datePicker_datePicker);
                        resultDateTime = (resultDate.getTimeInMillis() == initialDate.getTimeInMillis()) ? null : resultDate.getTime();
                        getCallBack().deliverDatePickerResult(resultDateTime);
                        DatePickerDialogFragment.this.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void bindWidgets(View view) {
        DatePicker pickerDate = (DatePicker) view.findViewById(R.id.datePicker_datePicker);
        pickerDate.init(
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {

                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        resultDate.set(Calendar.YEAR, year);
                        resultDate.set(Calendar.MONTH, monthOfYear);
                        resultDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                });
        TimePicker pickerTime = (TimePicker) view.findViewById(R.id.datePicker_timePicker);
        pickerTime.setIs24HourView(true);
        pickerTime.setCurrentHour(initialDate.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(initialDate.get(Calendar.MINUTE));
        pickerTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                resultDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                resultDate.set(Calendar.MINUTE, minute);
            }
        });
    }

    private DatePickerDialogCallback getCallBack() {
        DatePickerDialogCallback result;
        if (getTargetFragment() != null) {
            try {
                result = (DatePickerDialogCallback) getTargetFragment();
            } catch (ClassCastException e) {
                throw new ClassCastException("The targetFragment had been set but did not implement DatePickerDialogCallback. Was: " + getTargetFragment().getClass());
            }
        } else {
            try {
                result = (DatePickerDialogCallback) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException("At least " + getActivity() + " must implement DatePickerDialogCallback");
            }
        }
        Assert.notNull(result);
        return result;
    }
}
