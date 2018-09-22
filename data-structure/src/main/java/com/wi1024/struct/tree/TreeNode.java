package com.wi1024.struct.tree;

import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *      普通树
 * <p>
 *
 * @author mrfsong@gmail.com
 * @create 2018/8/9 21:43
 */
@ToString
public class TreeNode<T> {

    T value;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public void addChild(TreeNode<T> node) {
        node.setParent(this);
        children.add(node);
    }

    public void addChild(Collection<TreeNode<T>> nodes){
        for(TreeNode<T> node : nodes){
            node.setParent(this);
        }
        children.addAll(nodes);
    }

    public Boolean isRoot(){
        if(parent == null){
            return true;
        }
        return false;
    }

    public Boolean isLeaf(){
        if(children == null || children.size() == 0){
            return true;
        }
        return false;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }
}
