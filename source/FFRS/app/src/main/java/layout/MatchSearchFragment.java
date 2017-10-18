package layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.capstone.ffrs.MatchMapActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.utils.TimePickerListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MatchSearchFragment extends Fragment {

    private String displayFormat = "dd/MM/yyyy";
    private Spinner spinner;

    public MatchSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_search, container, false);

        final EditText date = (EditText) view.findViewById(R.id.input_date);
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

        long currentMillis = System.currentTimeMillis();
        String strCurrentDate = sdf.format(currentMillis);
        date.setText(strCurrentDate);

        final Calendar dateSelected = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateSelected.set(Calendar.YEAR, year);
                dateSelected.set(Calendar.MONTH, monthOfYear);
                dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);

                date.setText(sdf.format(dateSelected.getTime()));

            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        final EditText from = (EditText) view.findViewById(R.id.input_start_time);
        from.setOnClickListener(new TimePickerListener(view.getContext(), from));

        final EditText to = (EditText) view.findViewById(R.id.input_end_time);
        to.setOnClickListener(new TimePickerListener(view.getContext(), to));

        Button btLocation = (Button) view.findViewById(R.id.btChooseLocation);
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getActivity().getIntent().getExtras();
                Intent intent = new Intent(v.getContext(), MatchMapActivity.class);
                intent.putExtra("field_type_id", (spinner.getSelectedItemPosition() + 1));
                intent.putExtra("field_date", date.getText().toString());
                intent.putExtra("field_start_time", from.getText().toString());
                intent.putExtra("field_end_time", to.getText().toString());
                intent.putExtra("user_id", b.getInt("user_id"));
                startActivity(intent);
            }
        });
        addSpinner(view);
        return view;
    }

    public void addSpinner(View view) {
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.field_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}