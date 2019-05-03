package com.example.administrator.treeview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.example.administrator.treeview.treeView.Node;
import com.example.administrator.treeview.treeView.TreeListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Node> list = new ArrayList<Node>();
    private TreeListView listView;
    private RelativeLayout relativeLayout, rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_relative_layout);
        Context context=MainActivity.this;
        rl = new RelativeLayout(context);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        listView = new TreeListView(context, initNodeTree());
        listView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        relativeLayout.addView(listView);
    }
    public List<Node> initNodeTree() {

        List<Node> member_list =new ArrayList<Node>();
//        -1表示为根节点,id的作用为
        member_list.add(new Node("" + -1, "1" , "111"));
        member_list.add(new Node(""+1 , "2" , "222"));
        member_list.add(new Node("" + -1, "3" , "333"));
        member_list.add(new Node("" + 1, "4" , "444"));
        member_list.add(new Node("" + 4, "5" , "555"));
        member_list.add(new Node("" + 4, "6" , "666"));
        member_list.add(new Node("" + 4, "7" , "777"));
        member_list.add(new Node("" + 7, "8" , "888"));
        member_list.add(new Node("" + 8, "9" , "999"));
        member_list.add(new Node("" + 8, "10" , "101010"));
        list.addAll(member_list);
        return list;
    }
}
