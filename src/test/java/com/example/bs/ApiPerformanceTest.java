package com.example.bs;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Result;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.service.LogService;
import com.example.bs.tools.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogService logService;

    @Autowired
    private AuditMapper auditMapper;

    private static PrintStream out;

    private static final int TEST_DATA_SIZE = 5000;
    private static final AtomicInteger auditIdCounter = new AtomicInteger(0);

    private record PerformanceResult(int concurrentUsers, String testName, long avgRT, double tps, double errorRate, long p99RT, boolean success) {}

    @BeforeAll
    public static void setUp() {
        try {
            out = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            out = System.out;
        }
    }

//    @BeforeEach
//    public void setupTestData() {
//        // Get the current max id from audit table to avoid primary key conflicts
//        Integer maxId = auditMapper.getMaxId();
//        int startId = (maxId == null) ? 1 : maxId + 1;
//        auditIdCounter.set(startId);
//
//        List<Audit> auditsToInsert = new ArrayList<>();
//        for (int i = 0; i < TEST_DATA_SIZE; i++) {
//            Audit audit = new Audit();
//            audit.setType("OUT");
//            audit.setCode("TEST" + (startId + i));
//            audit.setLeibie("测试类别");
//            audit.setName("ceshi");
//            audit.setParther("测试客户");
//            audit.setPrice(10);
//            audit.setQuantity(1);
//            audit.setMoney(10);
//            audit.setUser("test-user");
//            audit.setCreatetime(LocalDateTime.now());
//            audit.setStatus(0); // Pending
//            auditsToInsert.add(audit);
//        }
//        if (!auditsToInsert.isEmpty()) {
//            auditMapper.batchAddAudit(auditsToInsert);
//        }
//    }

    private String getAuthToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "admin");
        claims.put("per", 2); // Super admin
        return Jwt.generateJwt(claims);
    }

    @Test
    public void runAllPerformanceTests() throws Exception {
        out.println("| Concurrent Users | API Type        | Avg RT(ms) | TPS   | Error Rate(%) | 99th Pct RT(ms) |");
        out.println("|------------------|-----------------|------------|-------|---------------|-----------------|");

        List<PerformanceResult> allResults = new ArrayList<>();
        allResults.add(runPerformanceTest(10, "Query_Inventory", "/bs/selkucun", null));
        allResults.add(runPerformanceTest(50, "Query_Inventory", "/bs/selkucun", null));
        allResults.add(runPerformanceTest(100, "Query_Inventory", "/bs/selkucun", null));
        allResults.add(runPerformanceTest(200, "Query_Inventory", "/bs/selkucun", null));

//        allResults.add(runPerformanceTest(10, "Approve_Stock_Out", "/bs/upaudit", "approve"));
//        allResults.add(runPerformanceTest(50, "Approve_Stock_Out", "/bs/upaudit", "approve"));
//        allResults.add(runPerformanceTest(100, "Approve_Stock_Out", "/bs/upaudit", "approve"));
//        allResults.add(runPerformanceTest(200, "Approve_Stock_Out", "/bs/upaudit", "approve"));

        printFinalReport(allResults);
    }

    private PerformanceResult runPerformanceTest(int concurrentUsers, String testName, String apiPath, String requestType) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrentUsers);
        List<Callable<TestResult>> tasks = new ArrayList<>();
        long durationSeconds = 10;
        String authToken = getAuthToken();

        for (int i = 0; i < concurrentUsers; i++) {
            Callable<TestResult> task = () -> {
                TestResult testResult = new TestResult();
                long testStartTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - testStartTime) < (durationSeconds * 1000)) {
                    long singleStartTime = System.currentTimeMillis();
                    try {
                        MvcResult mvcResult;
                        if ("approve".equals(requestType)) {
                            int auditId = auditIdCounter.getAndIncrement();
                            String requestBody = String.format("{\"id\": %d, \"status\": 1, \"remark\": \"并发测试通过\"}", auditId);
                            mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(apiPath)
                                            .header("token", authToken)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBody))
                                    .andExpect(status().isOk())
                                    .andReturn();
                        } else {
                            mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(apiPath)
                                            .header("token", authToken))
                                    .andExpect(status().isOk())
                                    .andReturn();
                        }
                        long singleEndTime = System.currentTimeMillis();
                        testResult.responseTimes.add(singleEndTime - singleStartTime);

                        ObjectMapper mapper = new ObjectMapper();
                        Result result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
                        if (!"1".equals(result.getCode())) {
                            testResult.errorCount++;
                        }
                    } catch (Exception e) {
                        testResult.errorCount++;
                    }
                }
                return testResult;
            };
            tasks.add(task);
        }

        List<Future<TestResult>> futures = executor.invokeAll(tasks);
        List<Long> allResponseTimes = new ArrayList<>();
        int totalErrorCount = 0;

        for (Future<TestResult> future : futures) {
            TestResult result = future.get();
            allResponseTimes.addAll(result.responseTimes);
            totalErrorCount += result.errorCount;
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return calculateMetrics(concurrentUsers, testName, allResponseTimes, totalErrorCount, durationSeconds);
    }

    private PerformanceResult calculateMetrics(int concurrentUsers, String testName, List<Long> responseTimes, int errorCount, long durationSeconds) {
        if (responseTimes.isEmpty()) {
            return new PerformanceResult(concurrentUsers, testName, 0, 0, 100.0, 0, false);
        }

        Collections.sort(responseTimes);
        long sumResponseTime = 0;
        for (long time : responseTimes) {
            sumResponseTime += time;
        }

        long avgRT = sumResponseTime / responseTimes.size();
        double tps = (double) responseTimes.size() / durationSeconds;
        double errorRate = (double) errorCount / responseTimes.size() * 100;
        long p99RT = responseTimes.get((int) (responseTimes.size() * 0.99));

        return new PerformanceResult(concurrentUsers, testName, avgRT, tps, errorRate, p99RT, true);
    }

    private void printFinalReport(List<PerformanceResult> results) {
        System.out.println("| Concurrent Users | API Type        | Avg RT(ms) | TPS   | Error Rate(%) | 99th Pct RT(ms) |");
        System.out.println("|------------------|-----------------|------------|-------|---------------|-----------------|");
        for (PerformanceResult res : results) {
            if (res.success) {
                System.out.printf("| %-16d | %-15s | %-10d | %-5.0f | %-13.1f | %-15d |\n",
                        res.concurrentUsers, res.testName, res.avgRT, res.tps, res.errorRate, res.p99RT);
            } else {
                System.out.printf("| %-16d | %-15s | %-10s | %-5s | %-13.1f | %-15s |\n",
                        res.concurrentUsers, res.testName, "N/A", "N/A", 100.0, "N/A");
            }
        }
    }

    static class TestResult {
        List<Long> responseTimes = new ArrayList<>();
        int errorCount = 0;
    }
}
