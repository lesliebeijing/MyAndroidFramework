package com.leslie.androidframework.net;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class MyJsonArrayRequest extends JsonArrayRequest {
	protected static final String TYPE_UTF8_CHARSET = "charset=UTF-8";

	public MyJsonArrayRequest(String url, Listener<JSONArray> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String type = response.headers.get(HTTP.CONTENT_TYPE);

			if (type == null) {
				type = TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			} else if (!type.contains("UTF-8")) {
				type += ";" + TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			}
		} catch (Exception e) {
		}

		return super.parseNetworkResponse(response);
	}
}
