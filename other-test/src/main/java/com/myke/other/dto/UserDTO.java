package com.myke.other.dto;

import com.myke.other.validator.annotation.EncryptId;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;

/**
 * 文章:
 * Spring Validation最佳实践及其实现原理
 * - https://juejin.cn/post/6856541106626363399#heading-15
 * - https://juejin.cn/post/6844904016380461070
 * <p>
 * Lombok类中注解详解
 * https://www.cnblogs.com/pascall/p/10281169.html
 * <p>
 * 解决多字段联合逻辑校验问题
 * https://blog.csdn.net/weixin_30800807/article/details/101424420
 *
 * @author zhangjianbin
 * @date 2021年07月21日15:21
 */
@Data
public class UserDTO {

    /**
     * 主键
     */
    @NotNull(groups = Update.class)
    @Null(groups = Save.class)
    private Long id;

    /**
     * 年龄
     */
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 50, message = "年龄不能大于50")
    @NotNull(message = "age 不能为空")
    private Integer age;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 性别
     */
    @NotNull
    private Integer sex;

    /**
     * 用户名
     */
    @NotBlank
    private String username;


    /**
     * 自定义主键校验
     * <p>
     * 会抛出异常,所在方法 org.springframework.validation.beanvalidation.SpringValidatorAdapter#processConstraintViolations(java.util.Set, org.springframework.validation.Errors)
     * https://blog.csdn.net/xvhongliang/article/details/99764048
     */
    @EncryptId
    private String encryptId;

    /**
     * 嵌套校验
     * <p>
     * 此时DTO类的对应字段必须标记@Valid注解
     */
    @NotNull
    @Valid
    private Job job;

    @Data
    public static class Job {

        @Min(value = 1, groups = Save.class, message = "jobId 最小不能小于1")
        private Long jobId;

        private String jobName;

        private String position;
    }


    /**
     * 保存的时候校验分组
     */
    public interface Save extends Default {
    }

    /**
     * 更新的时候校验分组
     */
    public interface Update extends Default {
    }


}