<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>export excel</title>
    <#--  公用部分的引用  -->
    <#include "common.ftl">
</head>
<body>
<button type="button" onclick="exportExcelGet0()" class="btn btn-primary">
    <span class="glyphicon glyphicon-circle-arrow-down"></span>测试导出GetExcel-0
</button>

<button type="button" onclick="exportExcelPost1()" class="btn btn-primary">
    <span class="glyphicon glyphicon-circle-arrow-down"></span>测试Poi-Excel导出Postexcel-1
</button>

<button type="button" onclick="exportExcelPost2()" class="btn btn-primary">
    <span class="glyphicon glyphicon-circle-arrow-down"></span>测试easyExcel导出PostExcel-2
</button>

<br/>
<p><input id="file" type="file" name="file"></p>
<p>
    <input type="button" onclick="importExcel()" id="submit" value="测试easyExcel导入校验Postexcel-1"/>
</p>

</body>

<script type="text/javascript">
    // axios 文档 https://www.kancloud.cn/yunye/axios/234845

    $(function () {
        //加载完页面后，执行
    });


    function exportExcelGet0() {
        window.location.href = "/poi/download-excel";
    }

    function exportExcelPost1() {
        let config = {
            //添加请求头
            responseType: 'blob'
        };

        axios.post('/poi/download-excel', {}, config)
            .then(function (response) {
                if (response.status === 200) {
                    var fileName = "测试Excel导出Postexcel-" + new Date().getTime() + '.xlsx'
                    var blob = new Blob([response.data],
                        {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'})
                    if (window.navigator.msSaveOrOpenBlob) {
                        //兼容ie
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        var downloadElement = document.createElement('a');
                        var href = window.URL.createObjectURL(blob); //创建下载的链接
                        downloadElement.href = href;
                        downloadElement.download = fileName; //下载后文件名
                        document.body.appendChild(downloadElement);
                        downloadElement.click(); //点击下载
                        document.body.removeChild(downloadElement); //下载完成移除元素
                        window.URL.revokeObjectURL(href); //释放掉blob对象
                    }
                }
            }).catch(function (error) {
            console.log(error);
        });
    }

    function exportExcelPost2() {
        let config = {
            //添加请求头
            responseType: 'blob'
        };

        axios.post('/easyExcel/exportExcel', {}, config)
            .then(function (response) {
                if (response.status === 200) {
                    var fileName = "EasyExcel测试excel导出-" + new Date().getTime() + '.xlsx'
                    var blob = new Blob([response.data],
                        {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'})
                    if (window.navigator.msSaveOrOpenBlob) {
                        //兼容ie
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        var downloadElement = document.createElement('a');
                        var href = window.URL.createObjectURL(blob); //创建下载的链接
                        downloadElement.href = href;
                        downloadElement.download = fileName; //下载后文件名
                        document.body.appendChild(downloadElement);
                        downloadElement.click(); //点击下载
                        document.body.removeChild(downloadElement); //下载完成移除元素
                        window.URL.revokeObjectURL(href); //释放掉blob对象
                    }
                }
            }).catch(function (error) {
            console.log(error);
        });
    }

    function importExcel() {
        var formData = new FormData();
        formData.append('file', $('#file')[0].files[0]);

        let config = {
            //添加请求头
            headers: {'Content-Type': 'multipart/form-data'},
            responseType: 'blob'
        };

        axios.post('/easyExcel/importExcel', formData, config)
            .then(function (response) {
                if (response.status === 200) {
                    var fileName = "测试导入校验excel-" + new Date().getTime() + '.xlsx'
                    var blob = new Blob([response.data],
                        {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'})
                    if (window.navigator.msSaveOrOpenBlob) {
                        //兼容ie
                        navigator.msSaveBlob(blob, fileName);
                    } else {
                        var downloadElement = document.createElement('a');
                        var href = window.URL.createObjectURL(blob); //创建下载的链接
                        downloadElement.href = href;
                        downloadElement.download = fileName; //下载后文件名
                        document.body.appendChild(downloadElement);
                        downloadElement.click(); //点击下载
                        document.body.removeChild(downloadElement); //下载完成移除元素
                        window.URL.revokeObjectURL(href); //释放掉blob对象
                    }
                }
            }).catch(function (error) {
            console.log(error);
        });
    }
</script>
</html>