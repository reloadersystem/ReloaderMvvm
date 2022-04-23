package pe.lecordonbleu.reloader.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.entidades.RegisterMenu;

public class RecyclerInicioAdapter extends RecyclerView.Adapter<RecyclerInicioAdapter.MyViewHolder> {

    private Context mContext;
    private List<RegisterMenu> mRegisterMenu;
    private OnSelectedOption onSelectedOption;


    public void setOnSelectedOption(OnSelectedOption onSelectedOption) {
        this.onSelectedOption = onSelectedOption;
    }

    public RecyclerInicioAdapter(Context mContext, List<RegisterMenu> registerMenu) {
        this.mContext = mContext;
        this.mRegisterMenu = registerMenu;
    }

    @NonNull
    @Override
    public RecyclerInicioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_inicio_items, parent, false);
        return new RecyclerInicioAdapter.MyViewHolder(view, onSelectedOption);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerInicioAdapter.MyViewHolder holder, int position) {


        int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
        int color = androidColors[position];

//        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//
//        int previousIndex = randomAndroidColor;
//
//        if (randomAndroidColor == previousIndex) {
//            randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//        }

//numero de  items del array
//        Random r = new Random();
//        int red = r.nextInt(255 - 0 + 1) + 0;
//        int green = r.nextInt(255 - 0 + 1) + 0;
//        int blue = r.nextInt(255 - 0 + 1) + 0;

        //int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));


//        GradientDrawable draw = new GradientDrawable();
//        draw.setShape(GradientDrawable.RECTANGLE);
//        draw.setColor(Color.rgb(red, green, blue));

        holder.txt_descripcion.setText(mRegisterMenu.get(position).getTextoMenu());
//        holder.cardView.setBackgroundColor(Color.rgb(red, green, blue));
//        holder.cardView.setCardBackgroundColor(Color.rgb(red, green, blue));
        holder.cardView.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return mRegisterMenu.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_descripcion;
        ImageView img_iconos;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView, OnSelectedOption onSelectedOption) {
            super(itemView);
            txt_descripcion = itemView.findViewById(R.id.txt_descripcion);
            //img_iconos = itemView.findViewById(R.id.img_iconos);
            cardView = itemView.findViewById(R.id.card_fondo);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int posicion = getAdapterPosition();
                    onSelectedOption.onClickedItem(posicion);
                }
            });
        }
    }
}
