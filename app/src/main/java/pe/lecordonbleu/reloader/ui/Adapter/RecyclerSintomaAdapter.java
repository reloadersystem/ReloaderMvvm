package pe.lecordonbleu.reloader.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.entidades.RegisterSintoma;

public class RecyclerSintomaAdapter extends RecyclerView.Adapter<RecyclerSintomaAdapter.MyViewHolder> {
    @NonNull

    private Context mContext;
    private List<RegisterSintoma> mRegisterSintom;

    public RecyclerSintomaAdapter(@NonNull Context mContext, List<RegisterSintoma> mRegisterSintom) {
        this.mContext = mContext;
        this.mRegisterSintom = mRegisterSintom;
    }

    @Override
    public RecyclerSintomaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_sintoma_items, parent, false);
        return new RecyclerSintomaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSintomaAdapter.MyViewHolder holder, int position) {

        holder.txt_detalle.setText(mRegisterSintom.get(position).getDescripcion());
       // holder.circleImageView.setBackground(mRegisterSintom.set(position, R.drawable.circle));

        if(mRegisterSintom.get(position).getImgstado()==1){
            Glide.with(mContext)
                    .load(R.drawable.circle_on)
                    .circleCrop()
                    .into(holder.circleImageView);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.circle_off)
                    .circleCrop()
                    .into(holder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return mRegisterSintom.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_detalle;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_detalle = itemView.findViewById(R.id.txt_detalle);
            circleImageView = itemView.findViewById(R.id.img_estadosin);


        }
    }
}
