package com.mrfsong.search.elastic;

import com.mrfsong.common.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TransportApiTest {

    private TransportClient transportClient;

    @Before
    public void init() {

        Settings settings = Settings.builder()
                .put("cluster.name", "es6_local").build();
        try {
            transportClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            log.error("TransportClient init fail !" , e);
        }
    }

    @After
    public void destroy() {
        if(transportClient != null){
            transportClient.close();
        }
    }


    @Test
    public void testBulk() throws Exception {

        BulkRequest bulkRequest = new BulkRequest();
        Map dataMap = new HashMap();
        dataMap.put("age",35);
        dataMap.put("address","BeiJing");
        dataMap.put("entryTime",new Date());

        String docId = KeyUtil.uuidString();

        IndexRequest indexRequest = new IndexRequest("data", "test", docId)
                .source(dataMap);

//        jsonBuilder();

        bulkRequest.add(new UpdateRequest("data", "test", docId)
                .doc(dataMap)
                .upsert(indexRequest)
                .docAsUpsert(true)
                .detectNoop(false)
                .retryOnConflict(3)
        );

        ActionFuture<BulkResponse> bulk = transportClient.bulk(bulkRequest);
        BulkResponse bulkResponse = bulk.get(5000L, TimeUnit.MILLISECONDS);
        if(bulkResponse.hasFailures()){
            BulkItemResponse[] items = bulkResponse.getItems();

        }


    }



}
