package com.myke.other.dto.validation;

import com.myke.other.validator.annotation.CollectionRange;
import com.myke.other.validator.annotation.Gender;
import com.myke.other.validator.group.StudentGroupSequenceProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/**
 * @author zhangjianbin
 * @date 2021年07月23日14:03
 */
@GroupSequenceProvider(StudentGroupSequenceProvider.class)
@Getter
@Setter
@ToString
public class StudentDTO {

    @NotNull(message = "更新时id不能为空", groups = {Update.class})
    @Null(message = "新增时id必须为空", groups = {Save.class})
    private Integer id;

    @NotNull
    private String name;

    /**
     * 自定义提示异常消息
     */
    @Min(value = 2, message = "{com.customs.min.message}")
    @NotNull
    @Positive
    private Integer age;

    @Future
    private Date start;

    /**
     * 根据 age 值的不同，自选择不同的分组进行校验
     */
    @Size(min = 1, max = 2, message = "集合大小必须在1到2之间", groups = WhenAge20And30Group.class)
    @Size(min = 3, max = 5, message = "集合大小必须在3到5之间", groups = WhenAge30And40Group.class)
    @NotNull
    private List<String> hobbies;

    /**
     * 对 list 中的每个值进行校验
     */
    @NotEmpty
    private List<@Email String> emails;

    @Valid // 让它校验List里面所有的属性
    private List<InnerChild> childList;

    @Getter
    @Setter
    @ToString
    public static class InnerChild {
        @NotNull
        private String name;
        @NotNull
        private Integer age;
    }

    /********** 自定义注解 *****************/
    @CollectionRange(min = 5, max = 10, groups = CustomsAnnotation.class)//
    private List<Integer> numbers;

    @Gender(gender = 0, groups = CustomsAnnotation.class)
    private Integer personGender;
    /********** 自定义注解 *****************/


    /**
     * 不继承 extends Default 会导致没有添加分组的注解不进行校验
     * <p>
     * 保存的时候校验分组
     */
    public interface Save extends Default {
    }

    /**
     * 更新的时候校验分组
     */
    public interface Update extends Default {
    }


    /**
     * 定义校验顺序，如果Default组失败，则Group1组不会再校验
     *
     * @GroupSequence用来控制组执行顺序
     */
    //@GroupSequence({Default.class, Group1.class})
    @GroupSequence({NotNull.class, Positive.class, Min.class})
    public interface Group {
    }

    public interface CustomsAnnotation extends Default {

    }


    /**
     * 定义专属的业务逻辑分组
     * <p>
     * age 字段值不同，添加不同的分组进行校验
     */
    public interface WhenAge20And30Group {
    }

    public interface WhenAge30And40Group {
    }
}