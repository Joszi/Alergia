package projekt.inzynierski;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class StacjaBazowa {

	private String mobileCountryCode;
	private String mobileNetworkCode;
	private String cellID;
	private String locationAreaCode;

	private boolean error;

	private String urlSent;
	private String fullResult;
	private String latitude;
	private String longitude;

	public void setMobileCountryCode(String value) {
		mobileCountryCode = value;
	}

	public void setMobileNetworkCode(String value) {
		mobileNetworkCode = value;
	}

	public void setCellID(int value) {
		cellID = String.valueOf(value);
	}

	public void setLocationAreaCode(int value) {
		locationAreaCode = String.valueOf(value);
	}

	public boolean isError() {
		return error;
	}

	public void setUrlSent() {
		urlSent = "http://www.opencellid.org/cell/get?mcc=" + mobileCountryCode
				+ "&mnc=" + mobileNetworkCode + "&cellid=" + cellID + "&lac="
				+ locationAreaCode + "&fmt=txt";
	}

	public String getUrlSent() {
		return urlSent;
	}

	public String getFullResult() {
		return fullResult;
	}

	public void getOpenCellID() {
		setUrlSent();

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlSent);
		HttpResponse response = null;

		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fullResult = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		spliteResult();
	}

	private void spliteResult() {
		if (fullResult.equalsIgnoreCase("err")) {
			error = true;
		} else {
			error = false;

			String[] result = fullResult.split(",");
			latitude = result[0];
			longitude = result[1];
		}
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
}
