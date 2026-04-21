package com.example.bs.concurrency;

import com.example.bs.entity.Kucun;
import com.example.bs.mapper.KucunMapper;
import com.example.bs.service.KucunService;
import com.example.bs.tools.yujing.InventoryWarningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KucunServiceConcurrencyTest {

    @InjectMocks
    private KucunService kucunService;

    @Mock
    private KucunMapper kucunMapper;

    @Mock
    private InventoryWarningService inventoryWarningService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private List<Kucun> dbResult;

    @BeforeEach
    void setUp() {
        Kucun item = new Kucun();
        item.setId(1);
        item.setName("苹果");
        item.setQuantity(100);

        dbResult = new ArrayList<>();
        dbResult.add(item);
    }

    @Test
    void shouldFallbackToDatabaseWhenRedisUnavailable() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("cache:kucun:all")).thenThrow(new RuntimeException("redis down"));
        when(kucunMapper.selkucun()).thenReturn(dbResult);
        when(inventoryWarningService.enrichWithWarning(dbResult)).thenReturn(dbResult);

        List<Kucun> result = kucunService.selkucun();

        assertEquals(1, result.size());
        assertEquals("苹果", result.get(0).getName());
        verify(kucunMapper, atLeastOnce()).selkucun();
    }

    @Test
    void shouldHandleConcurrentReadRequestsWithoutException() throws InterruptedException, ExecutionException {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("cache:kucun:all")).thenThrow(new RuntimeException("redis down"));
        when(kucunMapper.selkucun()).thenReturn(dbResult);
        when(inventoryWarningService.enrichWithWarning(dbResult)).thenReturn(dbResult);

        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Callable<List<Kucun>>> tasks = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            tasks.add(() -> kucunService.selkucun());
        }

        List<Future<List<Kucun>>> futures = pool.invokeAll(tasks);
        pool.shutdown();
        boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);

        for (Future<List<Kucun>> future : futures) {
            List<Kucun> one = future.get();
            assertEquals(1, one.size());
        }
        assertTrue(terminated);
        verify(kucunMapper, atLeastOnce()).selkucun();
    }
}
