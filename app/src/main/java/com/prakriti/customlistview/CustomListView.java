package com.prakriti.customlistview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListView extends BaseAdapter {
    // it is an adapter

    private Context context;
    private LayoutInflater layoutInflater;

    private String animals[] = new String[] {"BEAR", "BIRD", "CAT", "COW", "DOLPHIN", "FISH", "FOX", "HORSE", "LION", "TIGER"};
    private int images[] = { R.drawable.bear, R.drawable.bird, R.drawable.cat, R.drawable.cow, R.drawable.dolphin, R.drawable.fish,
            R.drawable.fox, R.drawable.horse, R.drawable.lion, R.drawable.tiger };


    public CustomListView(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return animals.length; // all arrays are of same size
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) { // return pos of value inside array
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // called on every iteration of items inside listview
        convertView = layoutInflater.inflate(R.layout.custom_listview, null); // null for root

        ImageView myImage = convertView.findViewById(R.id.myImage);
        TextView myText = convertView.findViewById(R.id.myText);

//        myImage.setImageResource(images[position]); // loading multiple images may cause OutOfMemoryException & app will crash
        myText.setText(animals[position]);
            // in case of single text view, use oldValue + list[pos] value in setText
/*
        // load scaled down version of images
        // refer loading large bitmaps efficiently -> android doc
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // the lower this number, the higher the quality
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[position], options);
        myImage.setImageBitmap(bitmap);
*/
        // use android doc specified methods
        myImage.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), images[position], 80, 80));
            // lesser the required height & width here, lesser the memory the app will take, lower the quality

        // can set listener for view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, animals[position], Toast.LENGTH_SHORT).show(); // or txt.getText() value
            }
        });

        return convertView;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
