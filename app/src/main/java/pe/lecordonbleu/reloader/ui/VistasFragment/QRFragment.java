package pe.lecordonbleu.reloader.ui.VistasFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.Servicios.Constantes;
import pe.lecordonbleu.reloader.Servicios.EndPoint;
import pe.lecordonbleu.reloader.Servicios.MethodWs;
import pe.lecordonbleu.reloader.entidades.RegisterQR;
import pe.lecordonbleu.reloader.entidades.RegisterSintoma;
import pe.lecordonbleu.reloader.ui.Adapter.RecyclerSintomaAdapter;
import pe.lecordonbleu.reloader.utils.Codificador;
import pe.lecordonbleu.reloader.utils.MisPreferencias;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRFragment extends Fragment {

    private ImageView imageView;
    private int id_estud;
    private String codigo_qr;
    private String dataCarrera;
    private String dataMenu;
    private String dataPerfil;
    private String mensaje;
    private String email;
    private TextView txtTitletoolbar;

    RecyclerView recyclerView;
    RecyclerSintomaAdapter recyclerSintomaAdapter;

    ArrayList<RegisterSintoma> sintomaArrayList;


    public QRFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = root.findViewById(R.id.img_qr);
        recyclerView = root.findViewById(R.id.recycler_estadosint);

        sintomaArrayList = new ArrayList<>();
        sintomaArrayList.add(new RegisterSintoma("Certificado de Vacunación", 1));
        sintomaArrayList.add(new RegisterSintoma("Declaración jurada - Ficha sintomatológica", 1));
        sintomaArrayList.add(new RegisterSintoma("Declaración jurada - Persona de riesgo", 2));
        sintomaArrayList.add(new RegisterSintoma("Examen de descarte covid", 1));
        sintomaArrayList.add(new RegisterSintoma("VIGENCIA", 1));


        // ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("Mi QR");

        TextView txtTitle = getActivity().findViewById(R.id.txt_titletoolbar);
        txtTitle.setText("Mi QR");


        id_estud = Integer.parseInt(MisPreferencias.obtenerValor(getContext(), "id_estud"));
        codigo_qr = MisPreferencias.obtenerValor(getContext(), "codigo_qr");


//        if (codigo_qr != null) {
//
//            try {
//                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                Bitmap bitmap = barcodeEncoder.encodeBitmap(codigo_qr, BarcodeFormat.QR_CODE, 850, 850);
//                imageView.setImageBitmap(bitmap);
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//
//        } else {
        RegisterQR registerQR = new RegisterQR(id_estud);

        EndPoint endPoint = MethodWs.getConfiguration().create(EndPoint.class);
        Call<ResponseBody> responseBodyQR = endPoint.postVerUsuarioQR(registerQR);
        responseBodyQR.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constantes.INSTANCE.getSUCCESS()) {
                    try {

                        ResponseBody responsedata = response.body();
                        String dataPerfil = responsedata.string();

                        JSONObject obj = new JSONObject(dataPerfil);
                        String codigoQr = obj.getString("codigo_qr");
                        String mensaje = obj.getString("mensaje");
                        int flag = obj.getInt("flag");

//                        Log.v("codigoQr", codigoQr);
//                        Log.v("mensajeQr", mensaje);

                        if (flag == 0) {
                            Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                        } else if (flag == 1) {

                            Codificador codificador = new Codificador();
                            String codigo = codificador.encode(codigoQr);

                            // Log.v("codificador", codigo);

                            try {
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.encodeBitmap(codigo, BarcodeFormat.QR_CODE, 850, 850);
                                imageView.setImageBitmap(bitmap);
                                // MisPreferencias.guardarValor(getContext(), "codigo_qr", codigoQr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        //   }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSintomaAdapter = new RecyclerSintomaAdapter(getContext(), sintomaArrayList);
        recyclerView.setAdapter(recyclerSintomaAdapter);

        return root;
    }
}