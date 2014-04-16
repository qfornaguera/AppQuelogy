package com.example.appqueology;

import java.util.ArrayList;

import android.graphics.PointF;
import android.view.View;
import android.widget.RelativeLayout;

public class Utility {
	
	static float nodeCounter,posX;
	
	public static void beautifygraph(RelativeLayout Rel){
		nodeCounter = 0;
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					Artifact node = (Artifact)Rel.getChildAt(i);
					if(node.getFather() == null)
						nodeList.add(node);
				}
			}	
		}
		posX = 0;
		for(int j=0;j<nodeList.size();j++){
			Artifact node = nodeList.get(j);
			setNodePosition(node,j,1);
			posX = posX + 1000;
		}
		recalculateLines(Rel);
		
	}
	
	public static void setNodePosition(Artifact node,int treeNum,int level){		
		ArrayList<Artifact> sons = node.getSons();
		for(int i=0;i<sons.size();i++){
			setNodePosition(sons.get(i),treeNum,level+1);
			if((sons.size()-1)/2 == i){
				if(sons.size()%2 != 0){
					if(sons.size() == 1){
						node.setX(sons.get(i).getX()-(node.width-sons.get(i).width)/2);//align in case of just one son (this puts father and son at the same center X)
					}else{
						node.setX(sons.get(i).getX());
					}
				}else{
					posX = posX + 200;
					node.setX(posX);
				}
			}
		}
		if(sons.size() == 0){
			posX = posX + 200;
			node.setX(posX);
		}
		node.setY(level*400);

	}
	
	public static void recalculateLines(RelativeLayout Rel){
		
		while(Rel.findViewWithTag("line") != null){
			Rel.removeView(Rel.findViewWithTag("line"));
		}
		
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					nodeList.add((Artifact)Rel.getChildAt(i));
					//nodeList.get(nodeList.size()-1).seekFather(Rel);
				}
			}
			
		}
		
		for(int j=0;j<nodeList.size();j++){
				Artifact node = nodeList.get(j);
				if(node.getFather() != null){
					Line line = new Line(Rel.getContext(),new PointF(node.getCenterX(),node.getCenterY()),new PointF(node.getFather().getCenterX(),node.getFather().getCenterY()));
					line.setTag("line");
					Rel.addView(line);
					node.bringToFront();
					node.getFather().bringToFront();
					View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
				}
		}
		
	}

}
