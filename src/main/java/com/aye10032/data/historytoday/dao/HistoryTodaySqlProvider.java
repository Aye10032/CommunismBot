package com.aye10032.data.historytoday.dao;

import com.aye10032.data.historytoday.pojo.HistoryToday;
import com.aye10032.data.historytoday.pojo.HistoryTodayExample;
import com.aye10032.data.historytoday.pojo.HistoryTodayExample.Criteria;
import com.aye10032.data.historytoday.pojo.HistoryTodayExample.Criterion;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class HistoryTodaySqlProvider {
    public String countByExample(HistoryTodayExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("history_today");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(HistoryTodayExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("history_today");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(HistoryToday record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("history_today");
        
        if (record.getHistory() != null) {
            sql.VALUES("history", "#{history,jdbcType=VARCHAR}");
        }
        
        if (record.getYear() != null) {
            sql.VALUES("year", "#{year,jdbcType=VARCHAR}");
        }
        
        if (record.getEventDate() != null) {
            sql.VALUES("event_date", "#{eventDate,jdbcType=VARCHAR}");
        }
        
        if (record.getEventType() != null) {
            sql.VALUES("event_type", "#{eventType,jdbcType=INTEGER}");
        }
        
        if (record.getFromGroup() != null) {
            sql.VALUES("from_group", "#{fromGroup,jdbcType=FLOAT}");
        }
        
        return sql.toString();
    }

    public String selectByExample(HistoryTodayExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("history");
        sql.SELECT("year");
        sql.SELECT("event_date");
        sql.SELECT("event_type");
        sql.SELECT("from_group");
        sql.FROM("history_today");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        HistoryToday record = (HistoryToday) parameter.get("record");
        HistoryTodayExample example = (HistoryTodayExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("history_today");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=INTEGER}");
        }
        
        if (record.getHistory() != null) {
            sql.SET("history = #{record.history,jdbcType=VARCHAR}");
        }
        
        if (record.getYear() != null) {
            sql.SET("year = #{record.year,jdbcType=VARCHAR}");
        }
        
        if (record.getEventDate() != null) {
            sql.SET("event_date = #{record.eventDate,jdbcType=VARCHAR}");
        }
        
        if (record.getEventType() != null) {
            sql.SET("event_type = #{record.eventType,jdbcType=INTEGER}");
        }
        
        if (record.getFromGroup() != null) {
            sql.SET("from_group = #{record.fromGroup,jdbcType=FLOAT}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("history_today");
        
        sql.SET("id = #{record.id,jdbcType=INTEGER}");
        sql.SET("history = #{record.history,jdbcType=VARCHAR}");
        sql.SET("year = #{record.year,jdbcType=VARCHAR}");
        sql.SET("event_date = #{record.eventDate,jdbcType=VARCHAR}");
        sql.SET("event_type = #{record.eventType,jdbcType=INTEGER}");
        sql.SET("from_group = #{record.fromGroup,jdbcType=FLOAT}");
        
        HistoryTodayExample example = (HistoryTodayExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(HistoryToday record) {
        SQL sql = new SQL();
        sql.UPDATE("history_today");
        
        if (record.getHistory() != null) {
            sql.SET("history = #{history,jdbcType=VARCHAR}");
        }
        
        if (record.getYear() != null) {
            sql.SET("year = #{year,jdbcType=VARCHAR}");
        }
        
        if (record.getEventDate() != null) {
            sql.SET("event_date = #{eventDate,jdbcType=VARCHAR}");
        }
        
        if (record.getEventType() != null) {
            sql.SET("event_type = #{eventType,jdbcType=INTEGER}");
        }
        
        if (record.getFromGroup() != null) {
            sql.SET("from_group = #{fromGroup,jdbcType=FLOAT}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, HistoryTodayExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}