//package com.mrfsong.search.elastic;
//
//import org.elasticsearch.action.bulk.BackoffPolicy;
//import org.elasticsearch.action.bulk.BulkProcessor;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.unit.ByteSizeUnit;
//import org.elasticsearch.common.unit.ByteSizeValue;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.Random;
//
//public class AppTest {
//
//
//
//    @Before
//    public void init(){
//
//
//    }
//
//    @After
//    public void destroy(){
//
//    }
//
//
//    @Test
//    public void testQuery() throws Exception {
//
//
//
//
//
//    }
//
//    @Test
//    public void testBulk() throws Exception {
//
//    }
//
//    /**
//     * 造数据
//     * @throws IOException
//     */
//    public static TransportClient scrollSearchPreData() throws IOException {
//        TransportClient client = TransportClientFactory.getInstance().getClient();
//        BulkProcessor bulkProcessor = BulkProcessor.builder(
//                client,
//                new BulkProcessor.Listener() {
//                    @Override
//                    public void beforeBulk(long executionId, BulkRequest request) {
//                        // bulk 执行之前
//                        System.out.println("beforeBulk-----" + request.getDescription());
//                    }
//
//                    @Override
//                    public void afterBulk(long executionId,
//                                          BulkRequest request,
//                                          BulkResponse response) {
//                        // bulk 执行之后
//                        System.out.println("afterBulk------" + request.getDescription() + ",是否有错误：" + response.hasFailures());
//                    }
//
//                    @Override
//                    public void afterBulk(long executionId,
//                                          BulkRequest request,
//                                          Throwable failure) {
//                        //bulk 失败
//                        System.out.println("报错-----" + request.getDescription() + "," + failure.getMessage());
//                    }
//                })
//                .setBulkActions(10000)  // 每10000个request，bulk一次
//                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)) // 每5M的数据刷新一次
//                .setFlushInterval(TimeValue.timeValueSeconds(5))    // 每5s刷新一次，而不管有多少数据量
//                .setConcurrentRequests(0)   // 设置并发请求的数量。值为0意味着只允许执行一个请求。值为1意味着在积累新的批量请求时允许执行1个并发请求。
//                .setBackoffPolicy(  // 设置一个自定义的重试策略，该策略最初将等待100毫秒，按指数增长，最多重试3次。当一个或多个批量项请求失败时，如果出现EsRejectedExecutionException异常，将尝试重试，该异常表明用于处理请求的计算资源太少。要禁用backoff，请传递BackoffPolicy.noBackoff()。
//                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
//                .build();
//
//
//        Random random = new Random();
//        for (int i = 1; i <= 1000; i++){
//            bulkProcessor.add(new IndexRequest("book", "elasticsearch", i+"").source(jsonBuilder()
//                    .startObject()
//                    .field("name", "book_" + i)
//                    .field("price", random.nextDouble()*1000)
//                    .field("postDate", new Date())
//                    .endObject()));
//        }
//        bulkProcessor.flush();
//        bulkProcessor.close();
//        return client;
//    }
//
//
//
//
//
//}
