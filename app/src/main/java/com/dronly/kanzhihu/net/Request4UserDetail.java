package com.dronly.kanzhihu.net;

import com.alibaba.fastjson.JSON;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dronly.kanzhihu.model.UserDetail;

import java.io.UnsupportedEncodingException;

/**
 * Created by gejiahui on 2016/3/15.
 */
public class Request4UserDetail extends Request<UserDetail> {
    private Response.Listener<UserDetail> listener;

    public Request4UserDetail(String url, Response.Listener<UserDetail> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;

    }


    @Override
    protected Response<UserDetail> parseNetworkResponse(NetworkResponse response) {
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            UserDetail res = JSON.parseObject(parsed, UserDetail.class);

            if (res.getError().equals("")) {
                return Response.success(res,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {  //error不为空说明出错
                return Response.error(new ParseError4String(res.getError()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError4String(e));
        }

    }

    @Override
    protected void deliverResponse(UserDetail response) {
        listener.onResponse(response);
    }
}
