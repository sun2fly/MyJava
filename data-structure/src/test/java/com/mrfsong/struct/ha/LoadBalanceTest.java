package com.mrfsong.struct.ha;

import com.github.jsonzou.jmockdata.MockConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.jsonzou.jmockdata.JMockData.mock;


/**
 *
 * 负载均衡算法
 */
@Slf4j
@RunWith(Parameterized.class)
public class LoadBalanceTest {



    MockConfig mockConfig;

    private int maxNode = 128;
    private int clusterSize = 10;
    private int keySize = 100;



    HashFunction function = Hashing.murmur3_32();

    List<String> mockKeyList = new ArrayList<>(keySize);

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0]; // repeat count which you want
    }


    @Before
    public void prepare() {
        mockConfig = new MockConfig()
                .doubleRange(1.0d,9999.99999d)
                .floatRange(1.11111f,9999.99999f)
                .decimalScale(3) // 设置小数位数为3，默认是2
                .dateRange("2020-06-01 00:00:00","2020-07-01 00:00:00")
                .intRange(0,255)
                .globalConfig();



        //mock数据
        for(int i = 0;i<keySize;i++){
            mockKeyList.add(mock(String.class,mockConfig));
        }



    }


    @Test
    public void testHash() throws Exception {

        Map<String,Integer> keyIndexMap = Maps.newHashMap();

        //集群节点数量：10
        for(String key : mockKeyList){
            HashCode hashCode = function.hashString(key, Charset.defaultCharset());
            int keyHash = Math.abs(hashCode.asInt());
            int nodeIndex = keyHash % clusterSize;
            keyIndexMap.put(key,nodeIndex);
        }

        print(keyIndexMap);

        //集群节点数量：20
        keyIndexMap = Maps.newHashMap();
        for(String key : mockKeyList){
            HashCode hashCode = function.hashString(key, Charset.defaultCharset());
            int keyHash = Math.abs(hashCode.asInt());
            int nodeIndex = keyHash % (clusterSize * 2);
            keyIndexMap.put(key,nodeIndex);
        }

       print(keyIndexMap);

    }


    @Test
    public void testRangedHash() throws Exception {

        Map<String,Integer> keyIndexMap = Maps.newHashMap();

        //集群节点数量：10
        for(String key : mockKeyList){
            HashCode hashCode = function.hashString(key, Charset.defaultCharset());
            int keyHash = Math.abs(hashCode.asInt());
            int kGroupIndex = keyHash % maxNode;
            int nodeIndex =  kGroupIndex * clusterSize / maxNode;
            keyIndexMap.put(key,nodeIndex);

        }

        print(keyIndexMap);

        //集群节点数量：20
        keyIndexMap = Maps.newHashMap();
        for(String key : mockKeyList){
            HashCode hashCode = function.hashString(key, Charset.defaultCharset());
            int keyHash = Math.abs(hashCode.asInt());
            int kGroupIndex = keyHash % maxNode;
            int nodeIndex =  kGroupIndex * clusterSize * 2 / maxNode;
            keyIndexMap.put(key,nodeIndex);
        }

        print(keyIndexMap);

    }


    /**
     * 测试分片区间
     * @throws Exception
     */
    @Test
    public void testRange() throws Exception {

        int hostNum = 32;        //主机数量
        int maxPartition = 256 ;//最大数据分片数量
        int hashCode = mock(Integer.class,mockConfig);     //分片字段hash值



        //1. 确定数据分片大小
        int partitionSize = (maxPartition % hostNum == 0) ? (maxPartition / hostNum) : (maxPartition / hostNum) + 1;


        //2. 划分数据key区间范围
        Map<Integer, Range<Integer>> keyGroupMap = Maps.newHashMap();
        for(int i=0;i<hostNum;i++){
            keyGroupMap.put(i , Range.closedOpen(i * partitionSize, (i + 1) * partitionSize));
        }

        //3. 确定分片下标号
        int keyIndex = hashCode % maxPartition;

//        log.info("zero hash : {}" , 255 % maxPartition);


        //4. 定位数据KEY存储目标主机
        int hostIndex = 0 ;
        for(Map.Entry<Integer,Range<Integer>> entry : keyGroupMap.entrySet()){
            Range<Integer> range = entry.getValue();
            if(range != null && (range.contains(keyIndex))){
                hostIndex = entry.getKey();
                break;
            }
        }

        log.info("1# host num : {} , partitionSize:{}, hashCode : {} , target host : {}" ,hostNum ,partitionSize,  hashCode , hostIndex);


        //KEY: partitionNo VALUE: hostId
        Map<Integer,Integer> partitionHostMap = Maps.newHashMap();
        for(int i=0;i < maxPartition;i++){
            partitionHostMap.put(i, i / partitionSize);
        }
        hostIndex = partitionHostMap.get(keyIndex);
        log.info("2# host num : {} , partitionSize:{}, hashCode : {} , target host : {}" ,hostNum ,partitionSize,  hashCode , hostIndex);


    }


    private String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }


    private void print(Map<String,Integer> keyIndexMap){

        keyIndexMap.entrySet().stream().forEach(entry -> log.info("Key : {}, node : {}" , entry.getKey() , entry.getValue()));
        //节点键值统计

        keyIndexMap.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getValue(),Collectors.counting())).forEach((node , count) -> {
            log.info("node: {} , count: {}" , node ,count);
        });

    }



}
