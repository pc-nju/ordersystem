package com.imooc.interceptor;

import com.imooc.bean.BaseBean;
import com.imooc.constant.FinalName;
import com.imooc.entity.Page;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Mybatis分页查询过滤器（Interceptor是mybatis提供的拦截器，不是Spring和JDK提供的）
 * @author 潘畅
 * @date 2018/5/25 19:59
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //获取配置文件中sql语句的id
        String id = mappedStatement.getId();
        //拦截以“ByPage”结尾的id,只要符合该条件的“StatementHandler”才能继续执行（正则表达式）
        if (id.matches(FinalName.REGEX_SELECT_BY_PAGE)){
            //获取sql语句
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();

            //获取参数（参数对象是各种Bean，但是所有的bean都继承自BaseBean，都封装了Page对象）
            BaseBean baseBean = (BaseBean) boundSql.getParameterObject();
            Page page = baseBean.getPage();
            /**
             *     page对象还缺个totalNumber(总条数)，来完善page对象。而我们获得的“sql”就是要查
             * 询出的目标集合，所以我们只要以“sql”为子查询，即可计算出目标总条数。
             */
            String totalNumberSql = "select count(*) from ("+ sql +")a";

            //因为Page对象要保证再放回Mybatis框架中就已经完整，所以这里使用JDK原生查询
            Connection connection = (Connection) invocation.getArgs()[0];

            /**
             *     因为从框架取出的“sql”，可能需要参数，所以我们无法直接执行“totalNumberSql”。我们需要将Mybatis掌握
             * 的参数与“？”映射关系设置到“totalNumberSql”中，就是通过“ParameterHandler.setParameters(preparedStatement)”
             */
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            PreparedStatement preparedStatement = connection.prepareStatement(totalNumberSql);
            parameterHandler.setParameters(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            //因为是查询总条数，就1条结果，所以不用遍历了
            if (resultSet.next()){
                //列下标从1开始
                page.setTotalNumber(resultSet.getInt(1));
            }

            //拼接sql语句
            String pageSql = sql + " LIMIT " + page.getDbIndex() + "," + page.getDbNumber();

            //将改造后的sql语句放回框架
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }
        /**
         *     invocation.proceed()：通过反射继续执行获取SQL语句之后的代码（即Mybatis
         * 框架代为进行的执行sql语句，返回结果。。）
         */
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
