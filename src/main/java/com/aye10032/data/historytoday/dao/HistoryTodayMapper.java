package com.aye10032.data.historytoday.dao;

import com.aye10032.data.historytoday.pojo.HistoryToday;
import com.aye10032.data.historytoday.pojo.HistoryTodayExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface HistoryTodayMapper {
    @SelectProvider(type=HistoryTodaySqlProvider.class, method="countByExample")
    long countByExample(HistoryTodayExample example);

    @DeleteProvider(type=HistoryTodaySqlProvider.class, method="deleteByExample")
    int deleteByExample(HistoryTodayExample example);

    @Delete({
        "delete from history_today",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into history_today (history, year, ",
        "date)",
        "values (#{history,jdbcType=VARCHAR}, #{year,jdbcType=VARCHAR}, ",
        "#{date,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="select last_insert_rowid()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(HistoryToday record);

    @InsertProvider(type=HistoryTodaySqlProvider.class, method="insertSelective")
    @SelectKey(statement="select last_insert_rowid()", keyProperty="id", before=false, resultType=Integer.class)
    int insertSelective(HistoryToday record);

    @SelectProvider(type=HistoryTodaySqlProvider.class, method="selectByExample")
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="history", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="year", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="date", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<HistoryToday> selectByExample(HistoryTodayExample example);

    @Select({
        "select",
        "id, history, year, date",
        "from history_today",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ConstructorArgs({
        @Arg(column="id", javaType=Integer.class, jdbcType=JdbcType.INTEGER, id=true),
        @Arg(column="history", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="year", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="date", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    HistoryToday selectByPrimaryKey(Integer id);

    @UpdateProvider(type=HistoryTodaySqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") HistoryToday record, @Param("example") HistoryTodayExample example);

    @UpdateProvider(type=HistoryTodaySqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") HistoryToday record, @Param("example") HistoryTodayExample example);

    @UpdateProvider(type=HistoryTodaySqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(HistoryToday record);

    @Update({
        "update history_today",
        "set history = #{history,jdbcType=VARCHAR},",
          "year = #{year,jdbcType=VARCHAR},",
          "date = #{date,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(HistoryToday record);
}