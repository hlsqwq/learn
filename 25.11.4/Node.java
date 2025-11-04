package com.hls;

import lombok.Data;

@Data
public class Node {
    public final static int RED = 1;
    public final static int BLACK = 0;
    public final static int ERROR = -1;

    public final static int LL = 10;
    public final static int RR = 11;
    public final static int RL = 12;
    public final static int LR = 13;



    //0:black   1:red
    int color;
    Integer data;
    Node left;
    Node right;
    Node parent;

    public Node(){
        color=RED;
        data=ERROR;
        left=null;
        right=null;
        parent=null;
    }
    public Node(int val){
        color=RED;
        data=val;
        left=null;
        right=null;
        parent=null;
    }

    public boolean hasRedKid(){
        if(left!=null && left.color==RED){
            return true;
        } else if (right!=null && right.color==RED) {
            return true;
        }
        return false;
    }


    /**
     * 获取类型
     * @return
     */
    public int get_type(){
        if(parent==null || parent.parent==null){
            return ERROR;
        }
        if(parent.parent.left==parent){
            if(parent.left==this){
                return LL;
            }
            return LR;
        }else {
            if(parent.right==this){
                return RR;
            }
            return RL;
        }
    }


    //反转颜色
    public void reverse(){
        if(color==BLACK){
            color=RED;
        }else{
            color=BLACK;
        }
    }

    public int get_uncle(){
        Node grandparent = null;
        if(parent==null || parent.parent==null){
            return ERROR;
        }
        grandparent=parent.parent;
        Node uncle=grandparent.left==parent?grandparent.right:grandparent.left;
        if(uncle==null){
            return BLACK;
        }
        return uncle.color;
    }

    public String toString(){
        return String.format("节点的值：%s  颜色：%s",data,color);
    }

}
