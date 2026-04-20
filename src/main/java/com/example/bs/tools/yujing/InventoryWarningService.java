package com.example.bs.tools.yujing;

import com.example.bs.entity.ChukuDailyDemand;
import com.example.bs.entity.InboundLeadTime;
import com.example.bs.entity.Kucun;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.mapper.ChukuMapper;
import com.example.bs.mapper.KucunMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InventoryWarningService {
    private static final int WINDOW_DAYS = 30;
    private static final double MIN_LT = 1.0;
    private static final double DEFAULT_Z = 1.65;

    @Autowired
    private KucunMapper kucunMapper;
    @Autowired
    private ChukuMapper chukuMapper;
    @Autowired
    private AuditMapper auditMapper;

    private volatile double z = DEFAULT_Z;
    private final Map<String, Integer> safeStockMap = new ConcurrentHashMap<>();
    private volatile LocalDate lastCalcDate = null;

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledRecompute() {
        recomputeSafeStock();
    }

    public synchronized void recomputeSafeStock() {
        List<ChukuDailyDemand> dailyDemand = chukuMapper.selDailyDemand30();
        List<InboundLeadTime> leadTimes = auditMapper.selInboundLeadTime();

        Map<String, double[]> demandSeriesByName = buildDemandSeries(dailyDemand);
        Map<String, Double> ltByName = new HashMap<>();
        for (InboundLeadTime lt : leadTimes) {
            if (lt == null || lt.getName() == null) continue;
            ltByName.put(lt.getName(), sanitizeLt(lt.getLt()));
        }

        List<Kucun> inventory = kucunMapper.selkucun();
        Map<String, Integer> nextSafeMap = new HashMap<>();
        for (Kucun item : inventory) {
            if (item == null || item.getName() == null) continue;
            String name = item.getName();
            double[] series = demandSeriesByName.getOrDefault(name, emptySeries());
            double sigma = stdDev(series);
            double lt = ltByName.getOrDefault(name, MIN_LT);
            int safe = (int) Math.ceil(z * sigma * Math.sqrt(lt));
            if (safe < 0) safe = 0;
            nextSafeMap.put(name, safe);
        }

        safeStockMap.clear();
        safeStockMap.putAll(nextSafeMap);
        lastCalcDate = LocalDate.now();
        log.info("dynamic warning recomputed, itemCount={}, z={}", safeStockMap.size(), z);
    }

    public List<Kucun> enrichWithWarning(List<Kucun> list) {
        if (lastCalcDate == null) {
            recomputeSafeStock();
        }
        List<Kucun> result = new ArrayList<>();
        for (Kucun item : list) {
            if (item == null) continue;
            int safe = safeStockMap.getOrDefault(item.getName(), 0);
            item.setSafe(safe);
            int qty = item.getQuantity() == null ? 0 : item.getQuantity();
            item.setWarning(qty <= safe);
            result.add(item);
        }
        return result;
    }

    public double getZ() {
        return z;
    }

    public synchronized void setZ(double z) {
        if (!Double.isFinite(z) || z <= 0) return;
        this.z = z;
    }

    public LocalDate getLastCalcDate() {
        return lastCalcDate;
    }

    private Map<String, double[]> buildDemandSeries(List<ChukuDailyDemand> points) {
        Map<String, Map<LocalDate, Integer>> temp = new HashMap<>();
        for (ChukuDailyDemand p : points) {
            if (p == null || p.getName() == null || p.getDay() == null) continue;
            Map<LocalDate, Integer> byDay = temp.computeIfAbsent(p.getName(), k -> new HashMap<>());
            int qty = p.getQuantity() == null ? 0 : p.getQuantity();
            byDay.put(p.getDay(), byDay.getOrDefault(p.getDay(), 0) + qty);
        }

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(WINDOW_DAYS - 1L);
        Map<String, double[]> series = new HashMap<>();
        for (Map.Entry<String, Map<LocalDate, Integer>> e : temp.entrySet()) {
            double[] arr = new double[WINDOW_DAYS];
            Map<LocalDate, Integer> byDay = e.getValue();
            for (int i = 0; i < WINDOW_DAYS; i += 1) {
                LocalDate d = start.plusDays(i);
                arr[i] = byDay.getOrDefault(d, 0);
            }
            series.put(e.getKey(), arr);
        }
        return series;
    }

    private double[] emptySeries() {
        return new double[WINDOW_DAYS];
    }

    private double sanitizeLt(Double lt) {
        if (lt == null || !Double.isFinite(lt) || lt < MIN_LT) return MIN_LT;
        return lt;
    }

    private double stdDev(double[] values) {
        if (values == null || values.length == 0) return 0;
        double sum = 0;
        for (double v : values) sum += v;
        double mean = sum / values.length;
        double sq = 0;
        for (double v : values) {
            double d = v - mean;
            sq += d * d;
        }
        return Math.sqrt(sq / values.length);
    }
}

