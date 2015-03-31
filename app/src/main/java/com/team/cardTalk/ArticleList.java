package com.team.cardTalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ArticleList extends Fragment implements AdapterView.OnItemClickListener {


    private ArrayList<Article> articleList;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_articlelist, container, false);

        ListView listView = (ListView) view.findViewById(R.id.custom_list_listView);

        Dao dao = new Dao(getActivity());
        String testJsonData = dao.getJsonTestData();
        dao.insertJsonData(testJsonData);

        articleList = dao.getArticleList();


//        ArticleListData data1 = new ArticleListData("녹색 여왕의 활기찬 정원", "3월 20일 오전 9:10", "icon1.png", "NEXT MOVIE NIGHT (feat. 빅히어로 & pizza)\n13일의 금요일 저녁을 빅히어로와 함께!\n### 무슨 영화??? 빅 히어로\n > 말랑말랑 마시멜로같은 히어로 본 적 있나요?");
//        articleListDataArray.add(data1);
//
//        ArticleListData data2 = new ArticleListData("핑크 마법사의 거품 욕조", "3월 20일 오전 10:10", "icon2.png", "삼시세끼가 끝나서 산체 보는 낙이 없네 ㅠㅠ\n내가 누군지 궁금한 사람...\n무엇이 무엇이 똑같을까\n");
//        articleListDataArray.add(data2);
//
//        ArticleListData data3 = new ArticleListData("노란 조커의 은밀한 화장실", "3월 20일 오전 11:10", "icon3.png", "형진이는 못하는게 뭐냐?!\nLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        articleListDataArray.add(data3);
//
//        ArticleListData data4 = new ArticleListData("붉은 왕의 낡은 연못", "3월 20일 오전 12:10", "icon4.png", "오늘 공지센 메뉴는 뭡니까?\n공지센으로 고고싱\n공지센은 사랑입니다.");
//        articleListDataArray.add(data4);

        CustomAdapter customAdapter= new CustomAdapter(getActivity(), R.layout.custom_article_list, articleList);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);

		return view;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("TEST", position + "번 리스트 선택됨");
    }
}
