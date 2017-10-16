package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.ffrs.FieldSuggestActivity;
import com.capstone.ffrs.PayPalActivity;
import com.capstone.ffrs.R;

public class ReserveFailFragment extends Fragment {

    private Button btHome, btRetry;

    public ReserveFailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reserve_fail, container, false);
        btHome = (Button) view.findViewById(R.id.btHome);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getActivity().getIntent().getExtras();

                Intent intent = new Intent(getContext(), FieldSuggestActivity.class);
                intent.putExtra("user_id", b.getInt("user_id"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btRetry = (Button) view.findViewById(R.id.btRetry);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getActivity().getIntent().getExtras();

                Intent intent = new Intent(getContext(), PayPalActivity.class);
                intent.putExtra("time_slot_id", b.getInt("time_slot_id"));
                intent.putExtra("user_id", b.getInt("user_id"));
                intent.putExtra("field_id", b.getInt("field_id"));
                intent.putExtra("field_name", b.getString("field_name"));
                intent.putExtra("field_address", b.getString("field_address"));
                intent.putExtra("image_url", b.getString("image_url"));
                intent.putExtra("price", b.getInt("price"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
}
