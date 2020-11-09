package com.mrfsong.struct.pojo;

public class BinaryTree {

    public TreeNode root ;

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    public BinaryTree(int[] arr) {
        this.root = createBinaryTree(arr,0);
    }

    /**
     * 构造完全二叉树
     * @param array
     * @param index
     * @return
     */
    private TreeNode createBinaryTree(int[] array, int index) {

        TreeNode treeNode = null;
        if (index < array.length) {
            treeNode = new TreeNode(array[index]);
            // 对于顺序存储的完全二叉树，如果某个节点的索引为index，其对应的左子树的索引为2*index+1，右子树为2*index+1
            treeNode.left = createBinaryTree(array, 2 * index + 1);
            treeNode.right = createBinaryTree(array, 2 * index + 2);
        }
        return treeNode;
    }


}
