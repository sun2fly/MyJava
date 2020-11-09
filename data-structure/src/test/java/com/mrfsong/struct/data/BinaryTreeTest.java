package com.mrfsong.struct.data;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.mrfsong.struct.pojo.BinaryTree;
import com.mrfsong.struct.tree.TreeHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@Slf4j
@RunWith(Parameterized.class)
public class BinaryTreeTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1][0];
    }


    @Test
    public void createTest() {
        MockConfig mockConfig = new MockConfig();
        mockConfig.intRange(0,100);
        int[] nums = JMockData.mock(new TypeReference<int[]>(){} , mockConfig);
        BinaryTree binaryTree = new BinaryTree(nums);
        TreeHelper.show(binaryTree.root);
        log.info("Tree depth : {}" , TreeHelper.getTreeDepth(binaryTree.root) );
    }




}
