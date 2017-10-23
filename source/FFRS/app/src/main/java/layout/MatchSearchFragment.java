package layout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.capstone.ffrs.MatchActivity;
import com.capstone.ffrs.MatchMapActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.FieldTime;
import com.capstone.ffrs.utils.CustomTimePickerDialog;
import com.capstone.ffrs.utils.TimePickerListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.LocalDateTime;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchSearchFragment extends Fragment {

    private String displayFormat = "dd/MM/yyyy";
    private Spinner spinner;
    private LatLng currentPosition = null;
    private Button btFindRequest;

    public MatchSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            EditText txtAddress = (EditText) getView().findViewById(R.id.input_address);
            if (txtAddress.getText().toString().isEmpty()) {
                double latitude = intent.getDoubleExtra("latitude", -1);
                double longitude = intent.getDoubleExtra("longitude", -1);
                if (latitude != -1 && longitude != -1) {
                    currentPosition = new LatLng(latitude, longitude);
                } else {
                    currentPosition = null;
                }
                if (currentPosition != null) {
                    btFindRequest.setText("Tìm đối thủ");
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(currentPosition.latitude, currentPosition.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        txtAddress.setHint(address);
                        validate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public BroadcastReceiver timepickerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            validate();
        }
    };

    public void validate() {
        EditText from = (EditText) getActivity().findViewById(R.id.input_start_time);
        EditText to = (EditText) getActivity().findViewById(R.id.input_end_time);

        if (currentPosition == null) {
            btFindRequest.setEnabled(false);
            btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        } else if (!from.getText().toString().isEmpty() && !to.getText().toString().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            try {
                Date startTime = sdf.parse(from.getText().toString());
                Date endTime = sdf.parse(to.getText().toString());
                if (startTime.compareTo(endTime) < 0) {
                    btFindRequest.setEnabled(true);
                    btFindRequest.setBackgroundColor(Color.parseColor("#009632"));
                } else {
                    btFindRequest.setEnabled(false);
                    btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            btFindRequest.setEnabled(false);
            btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        }
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
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), R.style.DatepickerCalendarTheme, datePickerListener, dateSelected
                        .get(Calendar.YEAR), dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        final EditText from = (EditText) view.findViewById(R.id.input_start_time);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes > 0 && minutes < 30) {
            minutes = 30;
        } else if (minutes > 30 && minutes <= 59) {
            minutes = 0;
            if (hour < 23) {
                hour += 1;
            } else {
                hour = 0;
                LocalDateTime dateTime = LocalDateTime.now();
                dateTime = dateTime.plusDays(1);
                date.setText(sdf.format(dateTime.toDate()));
            }
        }

        from.setOnClickListener(new TimePickerListener(view.getContext(), from));
        from.setText(hour + ":" + (minutes == 0 ? "00" : "30"));

        final EditText to = (EditText) view.findViewById(R.id.input_end_time);
        to.setOnClickListener(new TimePickerListener(view.getContext(), to) {
            @Override
            public void onClick(View v) {

                if (!from.getText().toString().isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        setMinTime(sdf.parse(from.getText().toString()));

                        super.onClick(v);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    to.setText("");
                }
            }
        });

        EditText txtAddress = (EditText) view.findViewById(R.id.input_address);
        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        addresses = geocoder.getFromLocationName(v.getText().toString(), 1);

                        if (!addresses.isEmpty()) {
                            currentPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            Toast.makeText(getActivity(), addresses.get(0).getLatitude() + ":" + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
                        } else {
                            currentPosition = null;
                            Toast.makeText(getActivity(), "Cannot get address", Toast.LENGTH_LONG).show();
                        }

                        validate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        btFindRequest = (Button) view.findViewById(R.id.btFindRequest);
        btFindRequest.setText("Đang tìm vị trí của bạn");
        btFindRequest.setEnabled(false);
        btFindRequest.setBackgroundColor(Color.parseColor("#dbdbdb"));
        btFindRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                Intent intent = new Intent(v.getContext(), MatchActivity.class);
                intent.putExtra("field_type_id", (spinner.getSelectedItemPosition() + 1));
                intent.putExtra("field_date", date.getText().toString());
                intent.putExtra("field_start_time", from.getText().toString());
                intent.putExtra("field_end_time", to.getText().toString());
                intent.putExtra("user_id", sharedPreferences.getInt("user_id", -1));
                intent.putExtra("latitude", currentPosition.latitude);
                intent.putExtra("longitude", currentPosition.longitude);
                startActivity(intent);
            }
        });

        addSpinner(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationReceiver,
                new IntentFilter("location-message"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timepickerReceiver,
                new IntentFilter("timepicker-message"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationReceiver);

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(timepickerReceiver);
    }

    public void addSpinner(View view) {
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.field_types, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}