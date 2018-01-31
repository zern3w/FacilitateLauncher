package android.facilitatelauncher.view;

import android.content.Context;
import android.facilitatelauncher.model.Contact;
import android.facilitatelauncher.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by puttipongtadang on 1/31/18.
 */

public class ContactSelectionAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> list;

    class ViewHolder {

        TextView text, number;
    }

    public ContactSelectionAdapter(Context context, int resource,
                                        List<Contact> list) {
        super(context, resource, list);

        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    public List<Contact> getList() {
        return list;
    }

    public void setList(List<Contact> list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {

            view = View.inflate(context, resource, null);
            holder = new ViewHolder();

            holder.text = view.findViewById(R.id.title);
//            holder.number = view.findViewById(R.id.number);

            view.setTag(holder);
        }

        holder.text.setText(list.get(position).getName());
        holder.number.setText(list.get(position).getNumber());

        return view;
    }
}
