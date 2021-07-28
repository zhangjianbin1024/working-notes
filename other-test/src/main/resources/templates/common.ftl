<link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css"/>


<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>


<script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>


<script>
    /**
     * 前端时间格式化
     *
     * 方法调用：
     * let date = newDate();
     * dateFormat("YYYY-mm-dd HH:MM:SS", date);
     * 结果值：2021-07-2521:45:12
     * @param fmt
     * @param date
     * @returns {*}
     */
    function dateFormat(fmt, date) {
        let ret;
        const opt = {
            "Y+": date.getFullYear().toString(),        // 年
            "m+": (date.getMonth() + 1).toString(),     // 月
            "d+": date.getDate().toString(),            // 日
            "H+": date.getHours().toString(),           // 时
            "M+": date.getMinutes().toString(),         // 分
            "S+": date.getSeconds().toString()          // 秒
            // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (let k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
            }
            ;
        }
        ;
        return fmt;
    }
</script>