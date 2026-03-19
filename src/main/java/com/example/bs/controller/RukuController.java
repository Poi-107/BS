package com.example.bs.controller;

import com.example.bs.aop.AopAnnotation;
import com.example.bs.entity.Result;
import com.example.bs.entity.Ruku;
import com.example.bs.service.RukuService;
import com.example.bs.tools.UserContext;
import com.example.bs.tools.interceptor.Per;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bs")
@Slf4j
public class RukuController {
    @Autowired
    private RukuService rukuService;

    //    查询入库单
    @Per(1)
    @GetMapping("/selruku")
    public Result ruku(){
        log.info("请求查询入库单");
        List<Ruku> ruku=rukuService.selruku();
        return Result.success(ruku);
    }
//    根据user查询入库单
    @GetMapping("/selruku2")
    public Result selruku2(@RequestParam String user){
        log.info("根据username查询入库单");
        List<Ruku> ruku=rukuService.selruku2(user);
        return Result.success(ruku);
    }
//    分类查询
    @GetMapping("/selruku1")
    public Result selruku1(@RequestParam String leibie){
        log.info("请求分类查询入库单");
        List<Ruku> ruku=rukuService.selruku1(leibie);
        return Result.success(ruku);
    }
//    获取所有类别
    @GetMapping("/selleibie3")
    public Result selleibie3() {
        log.info("请求获取所有类别");
        List<String> auditList = rukuService.selleibie();
        return Result.success(auditList);
    }
//    添加入库
    @AopAnnotation(target = "入库",action = "进行入库")
    @PostMapping("/addruku")
    public Result addruku(@RequestBody Ruku ruku){
        log.info("请求添加入库单");
        String userName = UserContext.getCurrentUserName();
        ruku.setUser(userName);
        ruku.setRktime(LocalDateTime.now());
        rukuService.addruku(ruku);
        return Result.success("添加成功！");
    }
//    按照物品名称查询
    @GetMapping("/selruku3")
    public Result selruku3(@RequestBody Ruku r){
        log.info("按物品名称查询");
        List<Ruku> ruku=rukuService.selruku3(r.getName());
        return Result.success(ruku);
    }
//    按照供应商名称查询
    @GetMapping("/selruku4")
    public Result selruku4(@RequestBody Ruku r){
        log.info("按供应商名称查询");
        List<Ruku> ruku=rukuService.selruku4(r.getSupplier());
        return Result.success(ruku);
    }
//    按照操作人查询
    @Per(1)
    @GetMapping("/selruku5")
    public Result selruku5(@RequestBody Ruku r){
        log.info("按操作人查询");
        List<Ruku> ruku=rukuService.selruku5(r.getUser());
        return Result.success(ruku);
    }

//    excel导入
    @PostMapping(value = "/ruku/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result importRuku(@RequestPart("file") MultipartFile file) {
        log.info("请求导入入库 Excel");
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要导入的 Excel 文件");
        }
        final String name = (file.getOriginalFilename() == null ? "" : file.getOriginalFilename()).toLowerCase();
        if (!name.endsWith(".xlsx")) {
            return Result.error("仅支持 .xlsx 格式");
        }

        String userName = UserContext.getCurrentUserName();
        if (userName == null || userName.isBlank()) {
            return Result.error("未登录或无法获取用户信息");
        }

        int success = 0;
        int failed = 0;
        List<Map<String, Object>> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream(); XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null) {
                return Result.error("Excel 内容为空");
            }

            int lastRow = sheet.getLastRowNum();
            if (lastRow < 1) {
                return Result.error("Excel 至少需要 1 行数据（除表头外）");
            }

            for (int i = 1; i <= lastRow; i += 1) { // 0 为表头，从 1 开始
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 列顺序：类别、物品名称、供应商、单价、数量、金额
                String leibie = readText(row.getCell(0));
                String material = readText(row.getCell(1));
                String supplier = readText(row.getCell(2));
                Integer price = readInt(row.getCell(3));
                Integer quantity = readInt(row.getCell(4));
                Integer moneyCell = readInt(row.getCell(5));

                // 空行跳过（前 3 列都为空 & 数量/单价为空）
                boolean empty = (leibie == null || leibie.isBlank())
                        && (material == null || material.isBlank())
                        && (supplier == null || supplier.isBlank())
                        && quantity == null
                        && price == null
                        && moneyCell == null;
                if (empty) continue;

                String msg = validateRow(material, supplier, leibie, quantity, price, moneyCell);
                if (msg != null && !msg.isBlank()) {
                    failed += 1;
                    Map<String, Object> err = new HashMap<>();
                    err.put("row", i + 1); // Excel 行号（从 1 开始）
                    err.put("message", msg);
                    errors.add(err);
                    continue;
                }

                int expectedMoney = quantity * price;
                if (moneyCell != null && moneyCell != expectedMoney) {
                    failed += 1;
                    Map<String, Object> err = new HashMap<>();
                    err.put("row", i + 1);
                    err.put("message", "金额不一致：填写金额=" + moneyCell + "，应为 单价×数量=" + expectedMoney);
                    err.put("givenMoney", moneyCell);
                    err.put("expectedMoney", expectedMoney);
                    errors.add(err);
                    continue;
                }

                int money = (moneyCell != null) ? moneyCell : expectedMoney;

                Ruku ruku = new Ruku();
                ruku.setName(material.trim());
                ruku.setSupplier(supplier.trim());
                ruku.setLeibie(leibie.trim());
                ruku.setQuantity(quantity);
                ruku.setPrice(price);
                ruku.setMoney(money);
                ruku.setUser(userName);
                ruku.setRktime(LocalDateTime.now());
                rukuService.addruku(ruku);
                success += 1;
            }
        } catch (Exception ex) {
            log.error("导入入库 Excel 失败", ex);
            return Result.error("导入失败：" + ex.getMessage());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("success", success);
        data.put("failed", failed);
        data.put("errors", errors);
        return Result.success(data);
    }

    private static String validateRow(String material, String supplier, String leibie, Integer quantity, Integer price, Integer money) {
        if (leibie == null || leibie.trim().isEmpty()) return "类别不能为空（第1列）";
        if (material == null || material.trim().isEmpty()) return "物品名称不能为空（第2列）";
        if (supplier == null || supplier.trim().isEmpty()) return "供应商不能为空（第3列）";
        if (price == null || price < 0) return "单价必须 >= 0（第4列）";
        if (quantity == null || quantity <= 0) return "数量必须 > 0（第5列）";
        if (money != null && money < 0) return "金额必须 >= 0（第6列）";
        return "";
    }

    private static String readText(Cell cell) {
        if (cell == null) return "";
        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> {
                    double v = cell.getNumericCellValue();
                    long l = (long) v;
                    yield (Math.abs(v - l) < 1e-9) ? String.valueOf(l) : String.valueOf(v);
                }
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                case FORMULA -> cell.getCellFormula();
                default -> "";
            };
        } catch (Exception ignored) {
            return "";
        }
    }

    private static Integer readInt(Cell cell) {
        if (cell == null) return null;
        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> (int) Math.round(cell.getNumericCellValue());
                case STRING -> {
                    String s = cell.getStringCellValue();
                    if (s == null) yield null;
                    String t = s.trim();
                    if (t.isEmpty()) yield null;
                    yield Integer.parseInt(t);
                }
                default -> null;
            };
        } catch (Exception ignored) {
            return null;
        }
    }
}
