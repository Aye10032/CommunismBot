package com.aye10032.foundation.entity.base.ban.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BanRecordExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * @author Aye10032
     * @date 2022-07-22
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andQqIdIsNull() {
            addCriterion("qq_id is null");
            return (Criteria) this;
        }

        public Criteria andQqIdIsNotNull() {
            addCriterion("qq_id is not null");
            return (Criteria) this;
        }

        public Criteria andQqIdEqualTo(Long value) {
            addCriterion("qq_id =", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotEqualTo(Long value) {
            addCriterion("qq_id <>", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThan(Long value) {
            addCriterion("qq_id >", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThanOrEqualTo(Long value) {
            addCriterion("qq_id >=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThan(Long value) {
            addCriterion("qq_id <", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThanOrEqualTo(Long value) {
            addCriterion("qq_id <=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdIn(List<Long> values) {
            addCriterion("qq_id in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotIn(List<Long> values) {
            addCriterion("qq_id not in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdBetween(Long value1, Long value2) {
            addCriterion("qq_id between", value1, value2, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotBetween(Long value1, Long value2) {
            addCriterion("qq_id not between", value1, value2, "qqId");
            return (Criteria) this;
        }

        public Criteria andFromGroupIsNull() {
            addCriterion("from_group is null");
            return (Criteria) this;
        }

        public Criteria andFromGroupIsNotNull() {
            addCriterion("from_group is not null");
            return (Criteria) this;
        }

        public Criteria andFromGroupEqualTo(Long value) {
            addCriterion("from_group =", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupNotEqualTo(Long value) {
            addCriterion("from_group <>", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupGreaterThan(Long value) {
            addCriterion("from_group >", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupGreaterThanOrEqualTo(Long value) {
            addCriterion("from_group >=", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupLessThan(Long value) {
            addCriterion("from_group <", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupLessThanOrEqualTo(Long value) {
            addCriterion("from_group <=", value, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupIn(List<Long> values) {
            addCriterion("from_group in", values, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupNotIn(List<Long> values) {
            addCriterion("from_group not in", values, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupBetween(Long value1, Long value2) {
            addCriterion("from_group between", value1, value2, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andFromGroupNotBetween(Long value1, Long value2) {
            addCriterion("from_group not between", value1, value2, "fromGroup");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andLastBanDateIsNull() {
            addCriterion("last_ban_date is null");
            return (Criteria) this;
        }

        public Criteria andLastBanDateIsNotNull() {
            addCriterion("last_ban_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastBanDateEqualTo(Date value) {
            addCriterion("last_ban_date =", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateNotEqualTo(Date value) {
            addCriterion("last_ban_date <>", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateGreaterThan(Date value) {
            addCriterion("last_ban_date >", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateGreaterThanOrEqualTo(Date value) {
            addCriterion("last_ban_date >=", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateLessThan(Date value) {
            addCriterion("last_ban_date <", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateLessThanOrEqualTo(Date value) {
            addCriterion("last_ban_date <=", value, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateIn(List<Date> values) {
            addCriterion("last_ban_date in", values, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateNotIn(List<Date> values) {
            addCriterion("last_ban_date not in", values, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateBetween(Date value1, Date value2) {
            addCriterion("last_ban_date between", value1, value2, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andLastBanDateNotBetween(Date value1, Date value2) {
            addCriterion("last_ban_date not between", value1, value2, "lastBanDate");
            return (Criteria) this;
        }

        public Criteria andBanTimeIsNull() {
            addCriterion("ban_time is null");
            return (Criteria) this;
        }

        public Criteria andBanTimeIsNotNull() {
            addCriterion("ban_time is not null");
            return (Criteria) this;
        }

        public Criteria andBanTimeEqualTo(Integer value) {
            addCriterion("ban_time =", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeNotEqualTo(Integer value) {
            addCriterion("ban_time <>", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeGreaterThan(Integer value) {
            addCriterion("ban_time >", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ban_time >=", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeLessThan(Integer value) {
            addCriterion("ban_time <", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeLessThanOrEqualTo(Integer value) {
            addCriterion("ban_time <=", value, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeIn(List<Integer> values) {
            addCriterion("ban_time in", values, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeNotIn(List<Integer> values) {
            addCriterion("ban_time not in", values, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeBetween(Integer value1, Integer value2) {
            addCriterion("ban_time between", value1, value2, "banTime");
            return (Criteria) this;
        }

        public Criteria andBanTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("ban_time not between", value1, value2, "banTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * @author Aye10032
     * @date 2022-07-22
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}