package balikbayan.box.sulatan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomArrayAdapter extends ArrayAdapter<CustomElement> {

    private Context context;
    private int resource;

    public CustomArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView textView1, textView2, textView3;
        String str1, str2, str3;

        // kunin ang data na nasa adapter
        CustomElement element = getItem(position);
        str1 = element.getStr1();
        str2 = element.getStr2();
        str3 = element.getStr3();

        // kunin ang reference ng mga textview na nasa custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, parent, false);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        textView1.setText(str1);
        textView2.setText(str2);
        textView3.setText(str3);

        return view;
    }
}
