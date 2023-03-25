package com.aye10032.foundation.entity.base.ban.record;

import java.util.ArrayList;
import java.util.List;

public class KillRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public KillRecordExample() {
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

        public Criteria andKilledTimesIsNull() {
            addCriterion("killed_times is null");
            return (Criteria) this;
        }

        public Criteria andKilledTimesIsNotNull() {
            addCriterion("killed_times is not null");
            return (Criteria) this;
        }

        public Criteria andKilledTimesEqualTo(Integer value) {
            addCriterion("killed_times =", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesNotEqualTo(Integer value) {
            addCriterion("killed_times <>", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesGreaterThan(Integer value) {
            addCriterion("killed_times >", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("killed_times >=", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesLessThan(Integer value) {
            addCriterion("killed_times <", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesLessThanOrEqualTo(Integer value) {
            addCriterion("killed_times <=", value, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesIn(List<Integer> values) {
            addCriterion("killed_times in", values, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesNotIn(List<Integer> values) {
            addCriterion("killed_times not in", values, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesBetween(Integer value1, Integer value2) {
            addCriterion("killed_times between", value1, value2, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKilledTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("killed_times not between", value1, value2, "killedTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesIsNull() {
            addCriterion("kill_times is null");
            return (Criteria) this;
        }

        public Criteria andKillTimesIsNotNull() {
            addCriterion("kill_times is not null");
            return (Criteria) this;
        }

        public Criteria andKillTimesEqualTo(Integer value) {
            addCriterion("kill_times =", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesNotEqualTo(Integer value) {
            addCriterion("kill_times <>", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesGreaterThan(Integer value) {
            addCriterion("kill_times >", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("kill_times >=", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesLessThan(Integer value) {
            addCriterion("kill_times <", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesLessThanOrEqualTo(Integer value) {
            addCriterion("kill_times <=", value, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesIn(List<Integer> values) {
            addCriterion("kill_times in", values, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesNotIn(List<Integer> values) {
            addCriterion("kill_times not in", values, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesBetween(Integer value1, Integer value2) {
            addCriterion("kill_times between", value1, value2, "killTimes");
            return (Criteria) this;
        }

        public Criteria andKillTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("kill_times not between", value1, value2, "killTimes");
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