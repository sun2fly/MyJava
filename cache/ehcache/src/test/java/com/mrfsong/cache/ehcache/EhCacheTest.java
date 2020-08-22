package com.mrfsong.cache.ehcache;

import com.google.gson.Gson;
import com.mrfsong.cache.ehcache.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.*;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.core.statistics.DefaultStatisticsService;
import org.ehcache.core.statistics.TierStatistics;
import org.ehcache.event.*;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.impl.config.persistence.UserManagedPersistenceContext;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreConfiguration;
import org.ehcache.impl.persistence.DefaultLocalPersistenceService;
import org.ehcache.impl.serialization.CompactJavaSerializer;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * <p>
 *      ehcache用法测试
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/08/10 10:15
 */
@Slf4j
public class EhCacheTest {

    private Integer ENTRY_SIZE = 10;

    @Before
    public void init(){}

    @After
    public void destroy(){}


    /**
     *
     * @Link https://www.ehcache.org/documentation/3.8/tiering.html
     *
     * Taking the above into account, the following possibilities are valid configurations:
     *
     *  1. heap + offheap
     *  2. heap + offheap + disk
     *  3. heap + offheap + clustered
     *  4. heap + disk
     *  5. heap + clustered
     */
    @Test
    public void heapTest() throws  Exception {

        ResourcePools cachePoolConfig = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(ENTRY_SIZE, EntryUnit.ENTRIES)
//                .offheap(1,MemoryUnit.MB)
                .build();

        //TODO 此方式无法监听Expire事件
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheEventListener<String, User>() {
                    @Override
                    public void onEvent(CacheEvent<? extends String, ? extends User> event) {
                        EventType eventType = event.getType();
                        log.warn("EventType:{} , Cache {} ..." ,eventType.name(), event.getKey());
                    }
                },EventType.CREATED,EventType.EVICTED, EventType.EXPIRED)
                .unordered().asynchronous();


        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("stringCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,cachePoolConfig)
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(30L)))
                                .withService(cacheEventListenerConfiguration)
                                .withLoaderWriter(new CacheLoaderWriter<String, String>() {
                                    @Override
                                    public String load(String s) throws Exception {
                                        log.warn("load cache ...");
                                        return null;
                                    }

                                    @Override
                                    public void write(String s, String s2) throws Exception {
                                        log.warn("write cache ...");

                                    }

                                    @Override
                                    public void delete(String s) throws Exception {
                                        log.warn("delete cache ...");

                                    }
                                })

                )
                .using(new DefaultStatisticsService())
                .build(true);

        Cache<String, String> stringCache = cacheManager.getCache("stringCache", String.class, String.class);

        IntStream.range(0,ENTRY_SIZE).forEach(entry -> {
            stringCache.put(String.valueOf(entry),"" + System.currentTimeMillis());
        });

        //此处不会报错，entry数量超过最大限制、会先淘汰掉部分entry
        stringCache.put("overflow-key","" + System.currentTimeMillis());

/*        cachePoolConfig = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(ENTRY_SIZE * 2, EntryUnit.ENTRIES)
                .build();
        stringCache.getRuntimeConfiguration().updateResourcePools(cachePoolConfig);*/

        stringCache.get("1");
        stringCache.put("1","abc");

        //Sleep to wait cache expired
        Thread.sleep(1000 * 60L);
    }


    /**
     * TODO
     *  待确认点：
     *      1. TTL事件到期后，缓存数据不会立即清除，在后续get操作时候才会清除所有缓存层数据、同时并触发Expired事件
     *      2. 所有缓存层的对同一条缓存的操作、对外只会触发一次事件
     *      3. 如何自定义evict算法
     *
     * @throws Exception
     */
    @Test
    public void diskTest() throws Exception {

        //配置Event监听服务
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheEventListener<String, User>() {
                    @Override
                    public void onEvent(CacheEvent<? extends String, ? extends User> event) {
                        log.warn("EventType:{} , Cache {} ..." ,event.getType(), event.getKey());
                        //TODO 此处捕获Expire、Evict事件后，可进行后续的远端存储操作
                    }
                }, EventType.EVICTED,EventType.EXPIRED)
                .firingMode(EventFiring.ASYNCHRONOUS)
                .eventOrdering(EventOrdering.ORDERED)
        ;

        ResourcePools cachePoolConfig = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(10, EntryUnit.ENTRIES)                //Only 10 entries allowed on heap. Eviction will occur when full
                .offheap(1, MemoryUnit.MB)                  //Only 1 MB allowed. Eviction will occur when full.
                .disk(2, MemoryUnit.MB, true)
                .build();

        DefaultStatisticsService statisticsService = new DefaultStatisticsService();

        //磁盘写满后并不会报错、会使用某种清理策略，清除部分缓存数据
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(new File("/tmp/eh_data")))
                .withCache("userCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, User.class,cachePoolConfig)
                                //event事件是保存到内存Queue中，此处配置多线程处理event
                                .withDispatcherConcurrency(100)
                                .withEventListenersThreadPool("listeners-pool")
//                                .withEvictionAdvisor()
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(30L)))                      //缓存过期策略
                                .withService(new OffHeapDiskStoreConfiguration(2))                                      //磁盘segment数量
                                .withService(cacheEventListenerConfiguration)                                                       //事件监听策略
                                .withValueSerializer(new CompactJavaSerializer<>(Thread.currentThread().getContextClassLoader()))   //自定义序列化

                               /* .withLoaderWriter(new CacheLoaderWriter<String, User>() {
                                    @Override
                                    public User load(String s) throws Exception {
                                        return null;
                                    }

                                    @Override
                                    public void write(String s, User user) throws Exception {

                                    }

                                    @Override
                                    public void delete(String s) throws Exception {

                                    }
                                })*/
                                .withService(WriteBehindConfigurationBuilder.newBatchedWriteBehindConfiguration(100, TimeUnit.MICROSECONDS, 10)
                                        .queueSize(100)
                                        .concurrencyLevel(1)
                                        .enableCoalescing()
                                )
                                .withSizeOfMaxObjectSize(10,MemoryUnit.MB)      //缓存单key存储对象最大值
                )
                .using(statisticsService)
                .build(true);


        Cache<String, User> userCache = persistentCacheManager.getCache("userCache", String.class, User.class);
        int i = 0;
        while(i < ENTRY_SIZE * 2000) {
            userCache.put("" + i, new User("name-"+i , i));
            i++;
        }

        log.info("========== Cache init has finished! ==========");
        Cache.Entry<String, User> iterEntry = null;
        Iterator<Cache.Entry<String, User>> entryIterator = userCache.iterator();

        while(entryIterator.hasNext()){
            iterEntry = entryIterator.next();
            userCache.get(iterEntry.getKey());
        }

        //Sleep to wait cache expired
        Thread.sleep(1000 * 30L);



        //获取statistic信息
        CacheStatistics cacheStatistics = statisticsService.getCacheStatistics("userCache");
        log.info("Statistics Info : \n"
                + " Gets: " + cacheStatistics.getCacheGets()
                + " Hits: " + cacheStatistics.getCacheHits()
                + " Hit Percent: " + cacheStatistics.getCacheHitPercentage()
                + " Puts: " + cacheStatistics.getCachePuts()
                + " Evictions:" + cacheStatistics.getCacheEvictions()
                + " Expirations:" + cacheStatistics.getCacheExpirations()
        );
        Gson gson = new Gson();

        Map<String, TierStatistics> tierStatistics = cacheStatistics.getTierStatistics();
        tierStatistics.entrySet().stream().forEach(entry -> {
            log.info("Tier : {} , Statistics:{}" , entry.getKey() , gson.toJson(entry.getValue()));
        });

        persistentCacheManager.close();
        persistentCacheManager.destroy();



    }


    /**
     * 二阶段提交事务，仅支持Read Commit隔离级别、需要配置Copier
     */
    @Test
    public void xaCache(){
        /*BitronixTransactionManager transactionManager = TransactionManagerServices.getTransactionManager();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .using(new LookupTransactionManagerProviderConfiguration(BitronixTransactionManagerLookup.class))
                .withCache("xaCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(10))
                        .withService(new XAStoreConfiguration("xaCache"))
                        .build()
                )
                .build(true);

        Cache<Long, String> xaCache = cacheManager.getCache("xaCache", Long.class, String.class);

        transactionManager.begin();
        {
            xaCache.put(1L, "one");
        }
        transactionManager.commit();

        cacheManager.close();
        transactionManager.shutdown();*/
    }


    /**
     * 自定义跨层级缓存异步写入策略
     */
    @Test
    public void testLoaderWriter() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        Cache<Long, String> writeBehindCache = cacheManager.createCache("writeBehindCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withLoaderWriter(new CacheLoaderWriter<Long, String>() {
                            @Override
                            public String load(Long aLong) throws Exception {
                                return null;
                            }

                            @Override
                            public void write(Long aLong, String s) throws Exception {

                            }

                            @Override
                            public void delete(Long aLong) throws Exception {

                            }
                        })
                        .withService(WriteBehindConfigurationBuilder
                                .newBatchedWriteBehindConfiguration(1, TimeUnit.SECONDS, 3)
                                .queueSize(3)
                                .concurrencyLevel(1)
                                .enableCoalescing())
                        .build());

        writeBehindCache.put(42L, "one");
        writeBehindCache.put(43L, "two");
        writeBehindCache.put(42L, "This goes for the record");
        cacheManager.close();
    }


    /**
     * 高级ManagedCacheBuilder用法
     * @throws Exception
     */
    @Test
    public void testUserManagerCache() throws Exception {

        LocalPersistenceService persistenceService = new DefaultLocalPersistenceService(new DefaultPersistenceConfiguration(new File("/tmp/eh_cache/umc")));

        PersistentUserManagedCache<Long, String> cache = UserManagedCacheBuilder.newUserManagedCacheBuilder(Long.class, String.class)
                .with(new UserManagedPersistenceContext<>("cache-name", persistenceService))
                .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .heap(10L, EntryUnit.ENTRIES)
                        .disk(10L, MemoryUnit.MB, true))
                .withEventExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(5))
                .withEventListeners(CacheEventListenerConfigurationBuilder
                        .newEventListenerConfiguration(new CacheEventListener<Object, Object>() {
                            @Override
                            public void onEvent(CacheEvent<?, ?> event) {

                            }
                        }, EventType.CREATED, EventType.UPDATED)
                        .asynchronous()
                        .unordered())
                .build(true);

// Work with the cache
        cache.put(42L, "The Answer!");

        cache.close();
        cache.destroy();

        persistenceService.stop();

    }

    /**
     * 自定义缓存过期驱逐策略
     * @throws Exception
     */
    @Test
    public void testEvictionAdvisor() throws Exception{

        CacheConfiguration<Long, String> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                ResourcePoolsBuilder.heap(2L))
                .withEvictionAdvisor(new EvictionAdvisor<Long,String>(){
                    @Override
                    public boolean adviseAgainstEviction(Long key, String value) {
                        return false;
                    }
                })
                .build();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("cache", cacheConfiguration)
                .build(true);

        Cache<Long, String> cache = cacheManager.getCache("cache", Long.class, String.class);

// Work with the cache
        cache.put(42L, "The Answer!");
        cache.put(41L, "The wrong Answer!");
        cache.put(39L, "The other wrong Answer!");

        cacheManager.close();

    }




}
