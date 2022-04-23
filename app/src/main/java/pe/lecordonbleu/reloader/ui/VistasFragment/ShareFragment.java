package pe.lecordonbleu.reloader.ui.VistasFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import pe.lecordonbleu.reloader.R;

public class ShareFragment extends Fragment {

    View rootview;

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_share, container, false);
       // ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("HORARIO");
        TextView txtTitle = getActivity().findViewById(R.id.txt_titletoolbar);
        txtTitle.setText("Horario");
        return rootview;
    }

}