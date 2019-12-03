package com.pact.healthapp.components.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.event.EventBean;
import com.pact.healthapp.components.event.EventDetailActivity;
import com.pact.healthapp.components.topiclist.TopiclistActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityListFramgent extends BaseFragment implements
        OnCheckedChangeListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.community_listView)
    private ListView community_listView;
    private ArrayList<CommunityBean> communitylist = new ArrayList<CommunityBean>();
    private CommunityAdapter4ListView communityAdapter;
    private EventListAdapter4ListView eventAdapter;
    @ViewInject(R.id.community_group_group)
    private RadioGroup community_group_group;
    private ArrayList<EventBean> events = new ArrayList<EventBean>();
    boolean isTopic;
    boolean resume = true;

    public CommunityListFramgent() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.community_community_layout, null);
        ViewUtils.inject(this, view);

        isTopic = true;
        getGroupInfo();

        community_group_group.setOnCheckedChangeListener(this);
        community_listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (!isTopic) {// 不是帖子,是活动
                    Intent intent = new Intent(getActivity(),
                            EventDetailActivity.class);
                    intent.putExtra("eventId", events.get(position).getId());
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(getActivity(), TopiclistActivity.class)
                        .putExtra("TITLE",
                                communitylist.get(position).getBoardTitle())
                        .putExtra("subBoards",
                                communitylist.get(position).getSubBoards() + "")
                        .putExtra("BOARDID",
                                communitylist.get(position).getId())
                        .putExtra("FAVORITED",
                                communitylist.get(position).getFavorite() + ""));
            }
        });

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        // 默认清空数据
        communitylist.clear();
        events.clear();
        switch (checkedId) {
            case R.id.community_group:// 圈子
                if (eventAdapter != null)
                    eventAdapter.notifyDataSetChanged();
                isTopic = true;
                if (communitylist.size() > 0) {// 加载逻辑

                }
                getGroupInfo();
                break;
            case R.id.community_active:// 活动
                isTopic = false;
                if (communityAdapter != null)
                    communityAdapter.notifyDataSetChanged();
                if (events.size() > 0) {// 加载逻辑

                }
                getActives("old");
                break;
            default:
                break;
        }
    }

    /**
     * 获取圈子信息
     */
    private void getGroupInfo() {
        // TODO Auto-generated constructor stub
        String bizId = "000";
        String serviceName = "queryBoard";
        // 拼接参数
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        String servicePara = JSON.toJSONString(map);
        // 发送请求，获取板块
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0);
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {// 获取验证码成功
                                String boards = obj.getString("boards");
                                if (boards == null || boards.length() == 0) {
                                    return;
                                }
                                JSONArray jsonArray = JSON.parseArray(boards);
                                communitylist.clear();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject object = (JSONObject) jsonArray
                                            .get(i);
                                    CommunityBean communityBean = JSON
                                            .toJavaObject(object,
                                                    CommunityBean.class);
                                    communitylist.add(communityBean);
                                }
                                communityAdapter = new CommunityAdapter4ListView(
                                        context, communitylist);
                                community_listView.setAdapter(communityAdapter);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            // CommonToast.makeText(context, "获取板块失败");
                        }
                    }
                });
    }

    /**
     * 获取活动信息
     */
    private void getActives(String queryType) {
        String bizId, serviceName, servicePara;
        bizId = "000";
        serviceName = "queryEventList";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("startTime", System.currentTimeMillis() + "");
        map.put("queryType", queryType);
        map.put("pageSize", "30");
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        super.onSuccess(arg0);
                        String result = "";
                        result = Des3.decode(arg0.result.toString());
                        JSONObject obj = JSON.parseObject(result);
                        JSONArray array = JSON.parseArray(obj
                                .getString("events"));
                        EventBean eventBean = new EventBean();
                        events.clear();
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject obj1 = (JSONObject) array.get(i);
                            eventBean = JSON
                                    .toJavaObject(obj1, EventBean.class);
                            events.add(eventBean);
                        }
                        eventAdapter = new EventListAdapter4ListView(context,
                                events);
                        community_listView.setAdapter(eventAdapter);
                    }
                });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (resume) { // 第一次不执行
            resume = false;
        } else {
            if (isTopic) {
                getGroupInfo(); // 圈子
            } else {
                getActives("old"); // 活动
            }
        }
    }

}
