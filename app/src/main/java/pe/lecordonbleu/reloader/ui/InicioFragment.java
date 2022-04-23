package pe.lecordonbleu.reloader.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.entidades.RegisterMenu;
import pe.lecordonbleu.reloader.ui.Adapter.RecyclerInicioAdapter;
import pe.lecordonbleu.reloader.ui.VistasFragment.QRFragment;
import pe.lecordonbleu.reloader.utils.DeviceDetector;
import pe.lecordonbleu.reloader.utils.GridSpacingItemDecoration;
import pe.lecordonbleu.reloader.utils.MisPreferencias;

import static android.content.Context.MODE_PRIVATE;

public class InicioFragment extends Fragment {

    private String dataCarrera;
    private String dataMenu;
    private String dataPerfil;
    private String mensaje;
    private String id_estud;
    private String email;
    ArrayList<RegisterMenu> menuArrayList;
    RecyclerInicioAdapter recyclerInicioAdapter;
    RecyclerView recyclerView;
    List<String> colors;

    int spanCount;
    int spacing;

    BottomNavigationView menuPrincipal;

    View root;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_principal, container, false);

        // ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("INTRANET ESTUDIANTE");

        TextView txtTitle = getActivity().findViewById(R.id.txt_titletoolbar);
        txtTitle.setText("Intranet Estudiante");

        recyclerView = root.findViewById(R.id.recycler_inicio);
        menuArrayList = new ArrayList<>();

//        ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("INTRANET");

        SharedPreferences sharpref = root.getContext().getSharedPreferences("SharePreferenceICBL", MODE_PRIVATE);

        if (sharpref.contains("id_estud")) {

            id_estud = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "id_estud");
            dataCarrera = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "dataCarrera");
            dataMenu = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "dataMenu");
            dataPerfil = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "dataPerfil");
            mensaje = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "mensaje");
            email = MisPreferencias.obtenerValor(root.getContext().getApplicationContext(), "email");


            try {
                JSONArray jsonArrayCarrera = new JSONArray(dataCarrera);
                //Log.v("dataCarrera", String.valueOf(jsonArrayCarrera));

                JSONArray jsonArrayMenu = new JSONArray(dataMenu);
                Log.v("dataMenu", String.valueOf(jsonArrayMenu));

                for (int idx = 0; idx < jsonArrayMenu.length(); idx++) {
                    JSONObject arrayItem = jsonArrayMenu.getJSONObject(idx);

                    String icono = arrayItem.getString("icono");
                    String textoMenuAbrev = arrayItem.getString("textoMenuAbrev");
                    String textoMenu = arrayItem.getString("textoMenu");
                    int idMenu = arrayItem.getInt("idMenu");
                    int orden = arrayItem.getInt("orden");
                    int nivel = arrayItem.getInt("nivel");
                    int idMenuPadre = arrayItem.getInt("idMenuPadre");


                    if (nivel == 1) {
                        RegisterMenu registerMenu = new RegisterMenu();
                        registerMenu.setIcono(icono);
                        registerMenu.setTextoMenuAbrev(textoMenuAbrev);
                        registerMenu.setTextoMenu(textoMenu);
                        registerMenu.setIdMenu(idMenu);
                        registerMenu.setOrden(orden);
                        registerMenu.setNivel(nivel);
                        registerMenu.setIdMenuPadre(idMenuPadre);

                        menuArrayList.add(registerMenu);
                    }
                }

                GridLayoutManager layoutManager
                        = new GridLayoutManager(getContext(), 4);

                if (DeviceDetector.isDeviceTablet(getContext())) {
                    spanCount = 4; // 3 columns
                    spacing = 180; // 50px
                } else {
                    spanCount = 4; // 3 columns
                    spacing = 20; // 50px
                }


                boolean includeEdge = false;
                recyclerView.setLayoutManager(layoutManager);
                recyclerInicioAdapter = new RecyclerInicioAdapter(getContext(), menuArrayList);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                recyclerView.setAdapter(recyclerInicioAdapter);

                recyclerInicioAdapter.setOnSelectedOption(position -> {

                    String nombre = menuArrayList.get(position).getTextoMenu();


                    if (nombre.equalsIgnoreCase("MI Qr")) {
                        Fragment fragmentPrincipal = new QRFragment();
                        FragmentManager fmanager = getActivity().getSupportFragmentManager();
                        if (fmanager != null) {
                            FragmentTransaction ftransaction = fmanager.beginTransaction();
                            if (ftransaction != null) {
                                ftransaction.replace(R.id.contentPrincipalMenu, fragmentPrincipal);
                                ftransaction.commit();
                            }
                        }

                        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_menuprincipal);
                        navBar.setSelectedItemId(R.id.navigation_item2);


                    }
                });


                JSONObject jsonObjectPerfil = new JSONObject(dataPerfil);
                int id_estud = jsonObjectPerfil.getInt("id_estud");
                Log.v("dataPerfil", String.valueOf(jsonObjectPerfil));

                String nombre = jsonObjectPerfil.getString("personaNombre");
                String apePaterno = jsonObjectPerfil.getString("personaPaterno");
                String apeMaterno = jsonObjectPerfil.getString("personaMaterno");


                // setTitle(String.format("%s / %s", tema + " - Page " + (currentpage + 1), contadorpag));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return root;
    }


}
