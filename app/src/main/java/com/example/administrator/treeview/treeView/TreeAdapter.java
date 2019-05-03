package com.example.administrator.treeview.treeView;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.treeview.R;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends BaseAdapter {
	private Context con;
	private LayoutInflater lif;
	public List<Node> all = new ArrayList<Node>();//展示
	private List<Node> cache = new ArrayList<Node>();//缓存,记录点状态
	private TreeAdapter tree = this;
	boolean hasCheckBox;
	private int expandIcon = -1;//展开图标
	private int collapseIcon = -1;//收缩图标
	ViewItem vi = null;

//	//存储checkbox选中的集合
//	private List<>

	/**
	 * 构造方法
	 */
	public TreeAdapter(Context context, List<Node> rootNodes){
		this.con = context;
		this.lif = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i=0;i<rootNodes.size();i++){
			addNode(rootNodes.get(i));
		}
	}
	/**
	 * 把一个节点上的所有的内容都挂上去
	 * @param node
	 */
	public void addNode(Node node){
		all.add(node);
		cache.add(node);
		if(node.isLeaf())return;
		for(int i = 0;i<node.getChildrens().size();i++){
			addNode(node.getChildrens().get(i));
		}
	}
	/**
	 * 设置展开收缩图标
	 * @param expandIcon
	 * @param collapseIcon
	 */
	public void setCollapseAndExpandIcon(int expandIcon,int collapseIcon){
		this.collapseIcon = collapseIcon;
		this.expandIcon = expandIcon;
	}
	/**
	 * 一次性对某节点的所有节点进行选中or取消操作
	 */
	public void checkNode(Node n,boolean isChecked){
		n.setChecked(isChecked);
		checkChildren(n,isChecked);
//		有一个子节点选中，则父节点选中
		if (n.getParent()!=null)
			checkParent(n,isChecked);
//		有一个子节点未选中，则父节点未选中
//		unCheckNode(n, isChecked);
	}

	/**
	 * 对父节点操作时，同步操作子节点
	 */
	public void checkChildren(Node n,boolean isChecked){
		for(int i =0 ;i<n.getChildrens().size();i++){
			n.getChildrens().get(i).setChecked(isChecked);
			checkChildren(n.getChildrens().get(i),isChecked);
		}
	}
	/**
	 *  有一个子节点选中，则父节点选中
	 */
	public void checkParent(Node n,boolean isChecked){
//		有一个子节点选中，则父节点选中
		if (n.getParent()!=null&&isChecked){
			n.getParent().setChecked(isChecked);
			checkParent(n.getParent(),isChecked);
		}
//		全部子节点取消选中，则父节点取消选中
		if (n.getParent()!=null &&!isChecked){
			for (int i = 0; i < n.getParent().getChildrens().size(); i++) {
				if (n.getParent().getChildrens().get(i).isChecked()) {
					checkParent(n.getParent(),!isChecked);
					return ;
				}
			}
			n.getParent().setChecked(isChecked);
			checkParent(n.getParent(),isChecked);
		}
	}

	/**
	 *  有一个子节点未选中，则父节点未选中
	 */
	public void unCheckNode(Node n, boolean isChecked){
		boolean flag = false;
		n.setChecked(isChecked);
		if(n.getParent() != null ){
			Log.d("parentSize", n.getParent().getChildrens().get(0).isChecked() + "");
			for (int i = 0; i < n.getParent().getChildrens().size(); i++) {
				if((n.getParent().getChildrens().get(i)) != n && (n.getParent().getChildrens().get(i).isChecked() != true)){
					flag = true;
					break;
				}
			}
			if(!flag) {
				unCheckNode(n.getParent(), isChecked);
			}
		}
	}

	/**
	 * 获取所有选中节点
	 * @return
	 *
	 */
	public List<Node> getSelectedNode(){
		Log.d("getSelectedNode", "我被执行了！");
		List<Node> checks =new ArrayList<Node>()	;
		for(int i = 0;i<cache.size();i++){
			Node n =(Node)cache.get(i);
			if(n.isChecked())
				checks.add(n);
		}
		return checks;
	}

	public void setSelectedNode(List<String> selectedNode){
		for (int i=0;i<cache.size();i++) {
			if(selectedNode.contains(cache.get(i).getCurId())) {
				cache.get(i).setChecked(true);
				cache.get(i).getParent().setChecked(true);
			}
		}
	}
	/**
	 * 设置是否有复选框
	 * @param hasCheckBox
	 *
	 */
	public void setCheckBox(boolean hasCheckBox){
		this.hasCheckBox = hasCheckBox;
	}
	/**
	 * 控制展开缩放某节点
	 * @param location
	 *
	 */
	public void ExpandOrCollapse(int location){
		Node n = all.get(location);//获得当前视图需要处理的节点 
		if(n!=null)//排除传入参数错误异常
		{
			if(!n.isLeaf()){
				n.setExplaned(!n.isExplaned());// 由于该方法是用来控制展开和收缩的，所以取反即可
				filterNode();//遍历一下，将所有上级节点展开的节点重新挂上去
				this.notifyDataSetChanged();//刷新视图
			}
		}
	}

	/**
	 * 设置展开等级
	 * @param level
	 *
	 */
	public void setExpandLevel(int level){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level)
					n.setExplaned(true);
				else
					n.setExplaned(false);
				all.add(n);
			}
		}

	}
	/* 清理all,从缓存中将所有父节点不为收缩状态的都挂上去*/
	public void filterNode(){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(!n.isParentCollapsed()||n.isRoot())//凡是父节点不收缩或者不是根节点的都挂上去
				all.add(n);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all.size();
	}


	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return all.get(location);
	}


	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}


	@Override
	public View getView(final int location, View view, ViewGroup viewgroup) {

		final Node n = all.get(location);

		//ViewItem vi = null;
		if(view == null){
			view = lif.inflate(R.layout.member_item, null);
			vi = new ViewItem();
			vi.cb = (CheckBox)view.findViewById(R.id.checkBox);
			vi.flagIcon = (ImageView)view.findViewById(R.id.disclosureImg);
			vi.tv = (TextView)view.findViewById(R.id.contentText);
			vi.cb.setOnClickListener(new OnClickListener() {
				private Node mCheckBoxN;
				@Override
				public void onClick(View v) {
					mCheckBoxN = (Node) v.getTag();
					checkNode(mCheckBoxN, ((CheckBox) v).isChecked());
					//unCheckNode(n, ((CheckBox) v).isChecked());
					tree.notifyDataSetChanged();        //只有点击部门后刷新页面，不然刷新频繁导致卡顿

				}
			});
			view.setTag(vi);
		}
		else{
			vi = (ViewItem)view.getTag();
		}
		if(n!=null){
			if(vi==null||vi.cb==null)
				System.out.println();
			vi.cb.setTag(n);
			vi.cb.setChecked(n.isChecked());
			//叶节点不显示展开收缩图标
				if(n.isExplaned()){
					if(expandIcon!=-1){
						vi.flagIcon.setImageResource(expandIcon);
					}
				}
				else{
					if(collapseIcon!=-1){
						vi.flagIcon.setImageResource(collapseIcon);
					}
				}
			//显示文本
			vi.tv.setText(n.getValue());
			// 控制缩进
			vi.flagIcon.setPadding(100*n.getLevel(), 3,3, 3);
				if(n.isLeaf()){
					vi.flagIcon.setVisibility(View.INVISIBLE);
				}
				else{
					vi.flagIcon.setVisibility(View.VISIBLE);
			}
			//设置是否显示复选框
			if(n.hasCheckBox()){
				vi.cb.setVisibility(View.VISIBLE);
			}
			else{
				vi.cb.setVisibility(View.GONE);
			}
		}
		return view;
	}


	public class ViewItem{
		private CheckBox cb;
		private ImageView flagIcon;
		private TextView tv;
	}
}
