package mingninja.net.kitcheninventory.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import mingninja.net.kitcheninventory.R;

public class DataItemAdapter extends ArrayAdapter<DataItem> {
    List<DataItem> mDataItems; //reference to List items
    LayoutInflater mInflater; //reads into memory

    public DataItemAdapter(Context context, List<DataItem> objects) {
        super(context, R.layout.activity_main_list_item, objects);

        mDataItems = objects;
        //mInflater = LayoutInflater.from(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_main_list_item,parent,false);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.itemNameText);
        ImageView itemImageView = (ImageView) convertView.findViewById(R.id.imageView);

        DataItem item = mDataItems.get(position);

        itemName.setText(item.getItemName()); //original DO NOT DELETE

        InputStream inputStream = null;
        try {
            String imageFileName = item.getImage();
            inputStream = getContext().getAssets().open(imageFileName);
            Drawable drawable = Drawable.createFromStream(inputStream,null);
            itemImageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* onclick */
       /* final String tempItemName = item.getItemName();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), tempItemName, Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }


}
