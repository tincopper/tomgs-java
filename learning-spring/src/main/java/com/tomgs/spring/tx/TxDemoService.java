package com.tomgs.spring.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务demo
 *
 * @author tangzy
 */
@Service
public class TxDemoService {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PlatformTransactionManager transactionManager;

  /**
   * 编程式事务 方式1
   */
  public void testMethod() {

    Boolean execute = transactionTemplate.execute(status -> {
      String sql = "UPDATE user SET age = '30' WHERE (id = '1')";
      int update = jdbcTemplate.update(sql);
      // 做其余的事情  可能抛出异常
      System.out.println(1 / 0);
      return update > 0;
    });

    System.out.println("更新状态：" + execute);
  }

  /**
   * 编程式事务 方式2
   */
  public void testTxWithTxm() {
    // 事务定义
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    definition.setReadOnly(false);
    definition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    // 获取事务定义获取一个事务实例
    TransactionStatus transaction = transactionManager.getTransaction(definition);

    try {
      // doSomething
      String sql = "UPDATE user SET age = '40' WHERE (id = '1')";
      int update = jdbcTemplate.update(sql);
      // 做其余的事情  可能抛出异常
      System.out.println(1 / 0);
      System.out.println("更新状态：" + (update > 0));

      // 提交事务
      transactionManager.commit(transaction);
    } catch (Exception e) {
      // 回滚事务
      transactionManager.rollback(transaction);
      throw e;
    }

  }

  /**
   * 声明式事务
   */
  @Transactional(rollbackFor = Exception.class)
  public void testTxWithAnnotation() {
    String sql = "UPDATE user SET age = '40' WHERE (id = '1')";
    int update = jdbcTemplate.update(sql);
    // 做其余的事情  可能抛出异常
    System.out.println(1 / 0);

    System.out.println("更新状态：" + (update > 0));
  }

}
