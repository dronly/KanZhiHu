package com.dronly.kanzhihu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dronly.kanzhihu.R;
import com.dronly.kanzhihu.base.EasyRecyclerViewAdapter;
import com.dronly.kanzhihu.callBack.LoadResultCallBack;
import com.dronly.kanzhihu.model.Answer;
import com.dronly.kanzhihu.model.Constants;
import com.dronly.kanzhihu.net.ParseError4String;
import com.dronly.kanzhihu.net.Request4Answers;
import com.dronly.kanzhihu.net.RequestManager;
import com.dronly.kanzhihu.ui.AnswerActivity;
import com.dronly.kanzhihu.ui.MainActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gejiahui on 2016/3/12.
 */
public class AnswersListAdapter extends EasyRecyclerViewAdapter<Answer> {
    private static String GET_ANSWER_CONTENT_URL = "http://api.kanzhihu.com/getpostanswers/";
    private static String YESTERDAY = "/yesterday";
    private static String RECENT = "/recent";
    private static String ARCHIVE = "/archive";
    private Activity mActivity;
    private Request4Answers request4Answers;
    private LoadResultCallBack mLoadResultCallBack;

    public AnswersListAdapter(Activity activity, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        mLoadResultCallBack = loadResultCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBind(final RecyclerView.ViewHolder viewHolder, int RealPosition, final Answer data) {
        ((AnswerViewHolder) viewHolder).title.setText(data.getTitle());
        ((AnswerViewHolder) viewHolder).body.setText(data.getSummary());
        ((AnswerViewHolder) viewHolder).vote.setText(data.getVote());
        ((AnswerViewHolder) viewHolder).avatar.setImageURI(Uri.parse(data.getAvatarUrl()));
        ((AnswerViewHolder) viewHolder).answerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("answerUrl", data.getAnswerUrl());
                intent.putExtra("questionUrl", data.getQuestionUrl());
                intent.putExtra("userUrl", data.getUserUrl());
                intent.putExtra("title", data.getTitle());
                intent.putExtra("vote", data.getVote());
                mActivity.startActivity(intent);
            }
        });
    }

    class AnswerViewHolder extends EasyRecyclerViewAdapter.EasyViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.body)
        TextView body;
        @Bind(R.id.vote)
        TextView vote;
        @Bind(R.id.avatar)
        SimpleDraweeView avatar;
        @Bind(R.id.answer_card)
        CardView answerCard;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    public void loadDatas(String time, int type) {
        request4Answers = new Request4Answers(getContentURL(time, type),
                new Response.Listener<ArrayList<Answer>>() {
                    @Override
                    public void onResponse(ArrayList<Answer> response) {
                        mLoadResultCallBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ParseError4String) {
                    mLoadResultCallBack.onFail(((ParseError4String) error).getErrorReason());
                } else {
                    mLoadResultCallBack.onFail("on fail");
                }

            }
        });
        RequestManager.addQueue(request4Answers, MainActivity.class);
    }


    private String getContentURL(String time, int type) {
        switch (type) {
            case Constants.YESTERDAY_ANSWERS:
                return GET_ANSWER_CONTENT_URL + time + YESTERDAY;

            case Constants.RECENT_ANSWERS:
                Logger.d(GET_ANSWER_CONTENT_URL + time + RECENT);
                return GET_ANSWER_CONTENT_URL + time + RECENT;

            case Constants.ARCHIVE_ANSWERS:
                Logger.d(GET_ANSWER_CONTENT_URL + time + ARCHIVE);
                return GET_ANSWER_CONTENT_URL + time + ARCHIVE;

            default:
                return GET_ANSWER_CONTENT_URL + time + YESTERDAY;
        }

    }
}
