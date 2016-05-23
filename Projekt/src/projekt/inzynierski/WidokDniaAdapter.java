package projekt.inzynierski;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WidokDniaAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;

	public WidokDniaAdapter(Context context, String[] values) {
		super(context, R.layout.rowlayout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, null);

		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

		String s = values[position];
		if (s.contains("0")) {
			imageView.setImageResource(R.drawable.poziom_0);
		} else if (s.contains("1")) {
			imageView.setImageResource(R.drawable.poziom_1);
		} else if (s.contains("2")) {
			imageView.setImageResource(R.drawable.poziom_2);
		} else if (s.contains("3")) {
			imageView.setImageResource(R.drawable.poziom_3);
		} else if (s.contains("4")) {
			imageView.setImageResource(R.drawable.poziom_4);
		} else {
			imageView.setImageResource(R.drawable.ikona);
		}

		String wynik;
		if (values[position].endsWith("0")) {
			wynik = values[position]
					.substring(0, values[position].length() - 1);
			textView.setText(wynik);
			return rowView;
		} else if (values[position].endsWith("1")) {
			wynik = values[position]
					.substring(0, values[position].length() - 1);
			textView.setText(wynik);
			return rowView;
		} else if (values[position].endsWith("2")) {
			wynik = values[position]
					.substring(0, values[position].length() - 1);
			textView.setText(wynik);
			return rowView;
		} else if (values[position].endsWith("3")) {
			wynik = values[position]
					.substring(0, values[position].length() - 1);
			textView.setText(wynik);
			return rowView;
		} else if (values[position].endsWith("4")) {
			wynik = values[position]
					.substring(0, values[position].length() - 1);
			textView.setText(wynik);
			return rowView;
		}
		textView.setText(values[position]);
		return rowView;
	}
}
