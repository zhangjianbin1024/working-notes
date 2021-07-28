package com.myke.other.controller.excel.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.myke.other.easyexcel.EasyExcelListener;
import com.myke.other.easyexcel.EasyExcelUtils;
import com.myke.other.easyexcel.ExcelCheckErrDto;
import com.myke.other.easyexcel.dto.UserExcelDto;
import com.myke.other.easyexcel.dto.UserExcelErrDto;
import com.myke.other.service.EasyExcelUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangjianbin
 * @date 2021年07月27日14:15
 */
@Slf4j
@RestController
@RequestMapping("/easyExcel")
public class EasyExcelController {

    @Autowired
    private EasyExcelUserService easyExcelUserService;

    /**
     * 导出测试
     *
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping(value = "/exportExcel", method = {RequestMethod.POST, RequestMethod.GET})
    public void exportExcel(HttpServletResponse response) throws IOException {

        // 查询数据
        List<UserExcelDto> userList = new ArrayList<>();
        UserExcelDto user1 = new UserExcelDto();
        user1.setName("张三");
        user1.setAge("10");
        user1.setBirthday("2021-07-27 13:44:50");
        user1.setSex("男");
        userList.add(user1);

        // 转成 excel 实体对象，导出
        //List<UserExcelDto> userExcelDtos = JSON.parseArray(JSON.toJSONString(userList), UserExcelDto.class);
        EasyExcelUtils.webWriteExcel(response, userList, UserExcelDto.class, "用户基本信息");
    }

    /**
     * 导入测试
     *
     * @param response
     * @param file
     *
     * @return
     *
     * @throws IOException
     */
    @PostMapping("/importExcel")
    public void importExcel(HttpServletResponse response, @RequestParam MultipartFile file) throws IOException {

        EasyExcelListener easyExcelListener = new EasyExcelListener(easyExcelUserService, UserExcelDto.class);

        EasyExcelFactory.read(file.getInputStream(), UserExcelDto.class, easyExcelListener).sheet().doRead();

        List successList = easyExcelListener.getSuccessList();
        List<ExcelCheckErrDto<UserExcelDto>> errList = easyExcelListener.getErrList();
        if (!errList.isEmpty()) {
            //如果包含错误信息就导出错误信息
            List<UserExcelErrDto> excelErrDtos = errList.stream().map(excelCheckErrDto -> {
                // excel 实体转为 excel 异常实体
                UserExcelErrDto userExcelErrDto = JSON.parseObject(JSON.toJSONString(excelCheckErrDto.getT()), UserExcelErrDto.class);
                userExcelErrDto.setErrMsg(excelCheckErrDto.getErrMsg());
                return userExcelErrDto;
            }).collect(Collectors.toList());

            // 返回错误的excel文件
            EasyExcelUtils.webWriteExcel(response, excelErrDtos, UserExcelErrDto.class, "用户导入错误信息");
        }
        //return CommonResult.success(successList.size(), "成功条数");
    }


    @GetMapping("/batch-excel")
    public void batchExcel(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = null;
        try {
            String sheetName = "测试-batch-excel-demo";

            /// 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 查询数据
            List<UserExcelDto> userList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                UserExcelDto user1 = new UserExcelDto();
                user1.setName("张三" + i);
                user1.setAge("10" + i);
                user1.setBirthday("2021-07-27 13:44:50");
                user1.setSex("男" + i);
                userList.add(user1);
            }

            excelWriter = EasyExcel.write(response.getOutputStream(), UserExcelDto.class)
                    .build();


            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

            for (int i = 0; i < 2; i++) {
                // 相当于多线程写
                excelWriter.write(userList, writeSheet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            excelWriter.finish();
        }

    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UserExcelDto}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserExcelDto.class).sheet("模板").doWrite(data());
    }


    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            int i = 1 / 0;

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), UserExcelDto.class)
                    .autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data());
        } catch (Exception e) {
            log.error("下载exce失败", e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private List<UserExcelDto> data() {
        List<UserExcelDto> list = new ArrayList<UserExcelDto>();
        for (int i = 0; i < 10; i++) {
            UserExcelDto data = new UserExcelDto();
            data.setAge("字符串" + 0);
            data.setName("zhang" + 0);
            data.setSex("6");
            list.add(data);
        }
        return list;
    }
}