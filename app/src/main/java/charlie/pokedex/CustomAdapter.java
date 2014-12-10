package charlie.pokedex;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by charlie on 12/9/14.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private PokedexListItem tempListItem = null;

    public CustomAdapter(Activity activity, ArrayList data, Resources res) {
        this.activity = activity;
        this.data = data;
        this.res = res;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data.size() <= 0) {
            return 1;
        } else {
            return data.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView sprite;
        public TextView pokemonName;
        public TextView pokedexID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.pokedex_list_item, null);

            holder = new ViewHolder();
            holder.sprite = (ImageView)view.findViewById(R.id.sprite);
            holder.pokemonName = (TextView)view.findViewById(R.id.pokemonName);
            holder.pokedexID = (TextView)view.findViewById(R.id.pokedexID);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        if (data.size() <= 0) {
            holder.pokemonName.setText("No Data");
        } else {
            tempListItem = null;
            tempListItem = (PokedexListItem)data.get(position);

            holder.pokemonName.setText(tempListItem.getPokemonName());
            holder.pokedexID.setText(tempListItem.getPokedexLabel());
            holder.sprite.setImageResource(res.getIdentifier("small_" + tempListItem.getPokedexID(), "drawable", "charlie.pokedex"));

            view.setOnClickListener(new OnItemClickListener(position));
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private class OnItemClickListener implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            MainActivity ma = (MainActivity)activity;
            //ma.onItemClick(mPosition);
        }
    }
}
