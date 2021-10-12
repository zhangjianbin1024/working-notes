package com.myke.other.service;


import com.myke.other.entity.UserDO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为 https://blog.csdn.net/soonfly/article/details/70305683
 * <p>
 * <p>
 * Spring事务传播行为测试
 * <p>
 * PROPAGATION_REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
 * PROPAGATION_SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。
 * PROPAGATION_MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。
 * PROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。
 * PROPAGATION_NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
 * PROPAGATION_NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。
 * Propagation.NESTED-如果当前调用者没有事务，则开启一个新的事务,此时NESTED效果和REQUIRED一样。
 * 如果当前调用者有事务，会开启一个当前事务的嵌套子事务。嵌套子事务有自己独立的savepoint，
 * 所以它回滚不影响外围事务的正常提交，但是外围事务回滚会带着嵌套子事务一起回滚
 *
 * @author zhangjianbin
 * @date 2021年07月28日15:52
 */
@Slf4j
@Service
public class TransactionUserService4 {


    @Autowired
    private TransactionUserService2 userService2;


    @Autowired
    private TransactionUserService3 userService3;

    @Autowired
    private TransactionUserService5 userService5;

    /************************* REQUIRED **********************************/

    /**
     * 外围方法没有开启事务
     * userService2.addRequired REQUIRED
     * userService3.addRequired REQUIRED
     * 此时两个service的事务方法都会开启一个新的事务独立运行，最后抛异常的时候两个事务都已经提交了，
     * 成功插入两条数据
     */
    public void test1() {
        UserDO userDO1 = new UserDO("123");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("124");
        userService3.addRequired(userDO2);

        throw new RuntimeException("事务传播行为REQUIRED");
    }

    /**
     * 外围方法没有开启事务
     * userService2.addRequired          REQUIRED
     * userService3.addRequiredException REQUIRED
     * 此时两个service的事务方法都会开启一个新的事务独立运行，所以125插入成功，126会回滚
     */
    public void test2() {
        UserDO userDO1 = new UserDO("125");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("126");
        userService3.addRequiredException(userDO2);
    }

    /**
     * 外围方法开启事务
     * userService2.addRequired REQUIRED
     * userService3.addRequired REQUIRED
     * 此时两个service的事务方法都会加入外围方法的事务，外围方法回滚，数据都回滚了
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test3() {
        UserDO userDO1 = new UserDO("127");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("128");
        userService3.addRequired(userDO2);

        throw new RuntimeException();
    }

    /**
     * 外围方法开启事务
     * userService2.addRequired          REQUIRED
     * userService3.addRequiredException REQUIRED
     * 此时两个service的事务方法都会加入外围方法的事务，外围方法回滚，数据都回滚了
     * <p>
     * test4和test3的区别在于，test3两个事务方法都会加入到外围方法的事务中执行
     * 而test4的userService3.addRequiredException方法加入外围事务会失败，然后外围事务回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test4() {
        UserDO userDO1 = new UserDO("130");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("131");
        userService3.addRequiredException(userDO2);
    }

    /**
     * 外围方法开启事务
     * userService2.addRequired          REQUIRED
     * userService3.addRequiredException REQUIRED
     * <p>
     * 和test4类似，userService3.addRequiredException方法加入外围事务会失败，外围事务回滚
     * <p>
     * test5可能会有点出乎意料，
     * userService3.addRequiredException的异常明明已经捕获了，为什么还会回滚？
     * <p>
     * 通过debug可以看到，userService3.addRequiredException方法其实已经被代理了，抛出异常之后，
     * 会由Spring事务相关的类先捕获到这个异常,然后把 Global transaction 标记为回滚，即使这个异常后面被捕获了
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test5() {
        UserDO userDO1 = new UserDO("132");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("133");
        try {
            userService3.addRequiredException(userDO2);
            //子方法有异常，spring将该事务标志为rollback only
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("捕获异常,也能回滚");
        }

        // test5 事务就会在整个方法执行完后就会提交，
        // 这时就会造成Transaction rolled back because it has been marked as rollback-only的异常。
    }

    /**
     * 外围方法开启事务
     * userService2.addRequired          REQUIRED
     * userService3.addRequiredException REQUIRED
     * <p>
     * userService3.addRequired2 方法加入外围事务,发生异常时也能回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test6() {

        UserDO userDO1 = new UserDO("133");
        userService2.addRequired(userDO1);

        UserDO userDO2 = new UserDO("134");
        try {
            userService3.addRequired2(userDO2);
            int i = 1 / 0;// 发生异常
        } catch (Exception e) {
            log.error("addRequired2 提交后异常了,则执行 addRequired2 事务回滚", e);

            //事务回滚方式一：设置手动回滚
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            //事务回滚方式二：向上抛异常
            throw new RuntimeException(e);

            // 没有方式一、方式二配置时，则事务 addRequired 、addRequired2 不会回滚，都能正常提交事务
        }

        // addRequired 、addRequired2 事务都会回滚
    }


    /************************* REQUIRES_NEW **********************************/
    /**
     * 外围方法没有开启事务
     * userService2.addRequiresNew          REQUIRES_NEW
     * userService3.addRequiresNew          REQUIRES_NEW
     * <p>
     * 此时两个service都会新启动一个事务，数据都成功插入
     */
    public void test7() {
        UserDO userDO1 = new UserDO("135");
        userService2.addRequiresNew(userDO1);

        UserDO userDO2 = new UserDO("136");
        userService3.addRequiresNew(userDO2);

        throw new RuntimeException();
    }

    /**
     * 外围方法没有开启事务
     * userService2.addRequiresNew                   REQUIRES_NEW
     * userService3.addRequiresNewException          REQUIRES_NEW
     * <p>
     * 此时两个service都会新启动一个事务，service1插入成功，service2插入失败
     */
    public void test8() {
        UserDO userDO1 = new UserDO("137");
        userService2.addRequiresNew(userDO1);

        UserDO userDO2 = new UserDO("138");
        userService3.addRequiresNewException(userDO2);
    }

    /**
     * 先开启一个新事务
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test9() {
        UserDO userDO1 = new UserDO("140");
        // 加入外围事务，回滚
        userService2.addRequired(userDO1);

        // 挂起当前事务(即外围事务)，新开启一个事务，插入成功
        UserDO userDO2 = new UserDO("141");
        userService2.addRequiresNew(userDO2);
        // 结束后提交事务，恢复挂起事务

        // 挂起当前事务(即外围事务)，新开启一个事务, 插入成功
        UserDO userDO3 = new UserDO("143");
        userService2.addRequiresNew(userDO3);

        // 结束后提交事务，恢复挂起事务,并回滚挂起的事务
        throw new RuntimeException();
    }

    /**
     * 先开启一个新事务
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test10() {
        UserDO userDO1 = new UserDO("145");
        // 加入外围事务，回滚
        userService2.addRequired(userDO1);

        // 新开启事务，插入成功
        UserDO userDO2 = new UserDO("146");
        userService2.addRequiresNew(userDO2);

        // 新开启事务，回滚
        UserDO userDO3 = new UserDO("147");
        userService3.addRequiresNewException(userDO3);
    }

    /***
     * 先开启一个新事务
     *
     * 实际上152的事务是先于151提交的，
     * 但是151事务仍然是先执行的，
     * 反映在数据库中还是151先插入，152后插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void test11() {
        UserDO userDO1 = new UserDO("151");
        // 加入外围事务,插入成功
        userService2.addRequired(userDO1);

        // 挂起当前事务，新开启事务,插入成功
        UserDO userDO2 = new UserDO("152");
        userService2.addRequiresNew(userDO2);
        // 结束后提交事务，恢复挂起事务

        try {
            // 挂起当前事务，新开启一个事务，事务回滚
            UserDO userDO3 = new UserDO("153");
            userService3.addRequiresNewException(userDO3);
        } catch (Exception e) {
            log.error("捕获异常，也能回滚");
        }

        // 恢复挂起事务，并提交挂起事务
    }


    /**
     * 外围方法没有开启事务的情况下，内部事务NESTED和REQUIRED效果相同
     */
    public void test12() {
        // 开启一个新事务,插入成功
        UserDO userDO1 = new UserDO("154");
        userService2.addNested(userDO1);

        // 开启一个新事务,插入成功
        UserDO userDO2 = new UserDO("155");
        userService2.addNested(userDO2);

        throw new RuntimeException();
    }

    public void test13() {
        // 开启一个新事务，插入成功
        UserDO userDO1 = new UserDO("156");
        userService2.addNested(userDO1);

        // 开启一个新事务，回滚
        UserDO userDO2 = new UserDO("157");
        userService3.addNestedException(userDO2);
    }

    /**
     * 开启一个新事务
     * <p>
     * transactionManager 事务名：DataSourceTransactionManagerConfiguration#transactionManager
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void test14() {
        UserDO userDO1 = new UserDO("161");
        // 开启一个嵌套事务，设置事务保存点
        userService2.addNested(userDO1);
        // 释放事务保存点

        UserDO userDO2 = new UserDO("162");
        // 开启一个嵌套事务，设置事务保存点
        userService2.addNested(userDO2);
        // 释放事务保存点

        // 内部事务成为外围事务的嵌套子事务，外围事务回滚，嵌套子事务也要回滚，没有数据插入成功
        throw new RuntimeException();
    }


    /**
     * 开启一个新事务
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void test15() {
        UserDO userDO1 = new UserDO("163");
        // 开启一个嵌套事务，设置事务保存点
        userService2.addNested(userDO1);
        // 释放事务保存点

        UserDO userDO2 = new UserDO("164");
        // 开启一个嵌套事务，设置事务保存点
        userService3.addNestedException(userDO2);
        // 回滚到事务保存点

        // 由于有异常抛出，回滚外围事务，嵌套子事务也要回滚，没有数据插入成功
    }

    /**
     * 开启一个新事务
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void test16() {
        UserDO userDO1 = new UserDO("165");
        // 开启一个嵌套事务，设置事务保存点,插入成功
        userService2.addNested(userDO1);
        // 释放事务保存点

        UserDO userDO2 = new UserDO("166");
        try {
            // 开启一个嵌套事务，设置事务保存点
            userService3.addNestedException(userDO2);
        } catch (Exception e) {
            System.out.println("service2回滚");
        }
        // 回滚到保存点
        // 嵌套事务异常被捕获，外围事务提交，165插入成功
    }

    /****************************************************************/


    /**
     * 异常：事务超时
     * MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction
     */
    @SneakyThrows
    public void test17() {
        userService5.test_thread2();
    }

    /**
     * 异常：
     * TransactionTimedOutException: Transaction timed out: deadline was Thu Jul 29 13:19:10 CST 2021
     */
    public void test18() {
        UserDO userDO = new UserDO("jdbcTemplate");
        userService2.addUserDoAnnotationTransactionalTimeOutWithJdbcTemplate(userDO);
    }

    /**
     * 异常：
     * TransactionTimedOutException: Transaction timed out: deadline was Thu Jul 29 13:40:56 CST 2021
     */
    public void test19() {
        Thread t1 = new Thread(() -> {
            UserDO userDO = new UserDO("mybatis");
            userService2.addUserDoAnnotationTransactionalTimeOutWithMybatis(userDO);

        });

        Thread t2 = new Thread(() -> {
            UserDO userDO = new UserDO("mybatis");
            userService2.addUserDoAnnotationTransactionalTimeOutWithMybatis(userDO);
        });
        t1.start();
        t2.start();

        // 为了查看子线程执行情况,所以这里使用while-true
        while (true) {
        }

    }
}