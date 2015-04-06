package net.as93.homesafe.menu;

import net.as93.homesafe.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuModel> {

    private final Context context;
    private final ArrayList<MenuModel> modelsArrayList;

    public MenuAdapter(Context context, ArrayList<MenuModel> modelsArrayList) {

        super(context, R.layout.side_menu_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        rowView = inflater.inflate(R.layout.side_menu_item, parent, false);
        ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
        TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
        imgView.setImageResource(modelsArrayList.get(position).getIcon());
        titleView.setText(modelsArrayList.get(position).getTitle());
        return rowView;
    }
}