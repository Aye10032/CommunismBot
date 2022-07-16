package com.aye10032.entity;

import java.util.ArrayList;
import java.util.List;

public class SubTaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SubTaskExample() {
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
     * @author dazo66
     * @date 2022-07-17
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andReciverTypeIsNull() {
            addCriterion("reciver_type is null");
            return (Criteria) this;
        }

        public Criteria andReciverTypeIsNotNull() {
            addCriterion("reciver_type is not null");
            return (Criteria) this;
        }

        public Criteria andReciverTypeEqualTo(Integer value) {
            addCriterion("reciver_type =", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeNotEqualTo(Integer value) {
            addCriterion("reciver_type <>", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeGreaterThan(Integer value) {
            addCriterion("reciver_type >", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("reciver_type >=", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeLessThan(Integer value) {
            addCriterion("reciver_type <", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeLessThanOrEqualTo(Integer value) {
            addCriterion("reciver_type <=", value, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeIn(List<Integer> values) {
            addCriterion("reciver_type in", values, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeNotIn(List<Integer> values) {
            addCriterion("reciver_type not in", values, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeBetween(Integer value1, Integer value2) {
            addCriterion("reciver_type between", value1, value2, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("reciver_type not between", value1, value2, "reciverType");
            return (Criteria) this;
        }

        public Criteria andReciverIdIsNull() {
            addCriterion("reciver_id is null");
            return (Criteria) this;
        }

        public Criteria andReciverIdIsNotNull() {
            addCriterion("reciver_id is not null");
            return (Criteria) this;
        }

        public Criteria andReciverIdEqualTo(Long value) {
            addCriterion("reciver_id =", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdNotEqualTo(Long value) {
            addCriterion("reciver_id <>", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdGreaterThan(Long value) {
            addCriterion("reciver_id >", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdGreaterThanOrEqualTo(Long value) {
            addCriterion("reciver_id >=", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdLessThan(Long value) {
            addCriterion("reciver_id <", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdLessThanOrEqualTo(Long value) {
            addCriterion("reciver_id <=", value, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdIn(List<Long> values) {
            addCriterion("reciver_id in", values, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdNotIn(List<Long> values) {
            addCriterion("reciver_id not in", values, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdBetween(Long value1, Long value2) {
            addCriterion("reciver_id between", value1, value2, "reciverId");
            return (Criteria) this;
        }

        public Criteria andReciverIdNotBetween(Long value1, Long value2) {
            addCriterion("reciver_id not between", value1, value2, "reciverId");
            return (Criteria) this;
        }

        public Criteria andArgsIsNull() {
            addCriterion("args is null");
            return (Criteria) this;
        }

        public Criteria andArgsIsNotNull() {
            addCriterion("args is not null");
            return (Criteria) this;
        }

        public Criteria andArgsEqualTo(String value) {
            addCriterion("args =", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsNotEqualTo(String value) {
            addCriterion("args <>", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsGreaterThan(String value) {
            addCriterion("args >", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsGreaterThanOrEqualTo(String value) {
            addCriterion("args >=", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsLessThan(String value) {
            addCriterion("args <", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsLessThanOrEqualTo(String value) {
            addCriterion("args <=", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsLike(String value) {
            addCriterion("args like", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsNotLike(String value) {
            addCriterion("args not like", value, "args");
            return (Criteria) this;
        }

        public Criteria andArgsIn(List<String> values) {
            addCriterion("args in", values, "args");
            return (Criteria) this;
        }

        public Criteria andArgsNotIn(List<String> values) {
            addCriterion("args not in", values, "args");
            return (Criteria) this;
        }

        public Criteria andArgsBetween(String value1, String value2) {
            addCriterion("args between", value1, value2, "args");
            return (Criteria) this;
        }

        public Criteria andArgsNotBetween(String value1, String value2) {
            addCriterion("args not between", value1, value2, "args");
            return (Criteria) this;
        }

        public Criteria andSubNameIsNull() {
            addCriterion("sub_name is null");
            return (Criteria) this;
        }

        public Criteria andSubNameIsNotNull() {
            addCriterion("sub_name is not null");
            return (Criteria) this;
        }

        public Criteria andSubNameEqualTo(String value) {
            addCriterion("sub_name =", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameNotEqualTo(String value) {
            addCriterion("sub_name <>", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameGreaterThan(String value) {
            addCriterion("sub_name >", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameGreaterThanOrEqualTo(String value) {
            addCriterion("sub_name >=", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameLessThan(String value) {
            addCriterion("sub_name <", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameLessThanOrEqualTo(String value) {
            addCriterion("sub_name <=", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameLike(String value) {
            addCriterion("sub_name like", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameNotLike(String value) {
            addCriterion("sub_name not like", value, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameIn(List<String> values) {
            addCriterion("sub_name in", values, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameNotIn(List<String> values) {
            addCriterion("sub_name not in", values, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameBetween(String value1, String value2) {
            addCriterion("sub_name between", value1, value2, "subName");
            return (Criteria) this;
        }

        public Criteria andSubNameNotBetween(String value1, String value2) {
            addCriterion("sub_name not between", value1, value2, "subName");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * @author dazo66
     * @date 2022-07-17
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