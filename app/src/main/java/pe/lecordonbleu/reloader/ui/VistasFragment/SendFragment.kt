package pe.lecordonbleu.reloader.ui.VistasFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import pe.lecordonbleu.reloader.R
import pe.lecordonbleu.reloader.data.DataSource
import pe.lecordonbleu.reloader.domain.RepoImpl
import pe.lecordonbleu.reloader.entidades.RegisterFicha
import pe.lecordonbleu.reloader.ui.viewmodel.FichaViewModel
import pe.lecordonbleu.reloader.ui.viewmodel.VMFactory
import pe.lecordonbleu.reloader.vo.Resource
import pe.lecordonbleu.reloader.vo.RetrofitClient


class SendFragment : Fragment() {

    lateinit var rootview: View
    private lateinit var viewModel: FichaViewModel
    private lateinit var viewModelFactory: VMFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_send, container, false)
        // ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("ASISTENCIA");
        val txtTitle = activity!!.findViewById<TextView>(R.id.txt_titletoolbar)
        txtTitle.text = "Ficha"
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postParameters = RegisterFicha(
                "35634", 2)

        val dataRepo = RepoImpl(DataSource(RetrofitClient.webService), postParameters)
        viewModelFactory = VMFactory(dataRepo)
        ViewModelProvider(this, viewModelFactory).get(FichaViewModel::class.java)

        viewModel.fetchFichaList.observe(viewLifecycleOwner, Observer { result ->

            when (result) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    Log.v("SuccesResponse", " ${result.data}")
                }
                is Resource.Failure -> {

                    Log.v("ErrorResponse", " ${result.exception}")
                }
            }

        })
    }
}// Required empty public constructor