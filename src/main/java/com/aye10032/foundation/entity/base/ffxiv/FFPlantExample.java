package com.aye10032.foundation.entity.base.ffxiv;

import java.util.ArrayList;
import java.util.List;

public class FFPlantExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FFPlantExample() {
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
     * @date 2022-09-27
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

        public Criteria andItemRankIsNull() {
            addCriterion("item_rank is null");
            return (Criteria) this;
        }

        public Criteria andItemRankIsNotNull() {
            addCriterion("item_rank is not null");
            return (Criteria) this;
        }

        public Criteria andItemRankEqualTo(Integer value) {
            addCriterion("item_rank =", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankNotEqualTo(Integer value) {
            addCriterion("item_rank <>", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankGreaterThan(Integer value) {
            addCriterion("item_rank >", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankGreaterThanOrEqualTo(Integer value) {
            addCriterion("item_rank >=", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankLessThan(Integer value) {
            addCriterion("item_rank <", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankLessThanOrEqualTo(Integer value) {
            addCriterion("item_rank <=", value, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankIn(List<Integer> values) {
            addCriterion("item_rank in", values, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankNotIn(List<Integer> values) {
            addCriterion("item_rank not in", values, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankBetween(Integer value1, Integer value2) {
            addCriterion("item_rank between", value1, value2, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemRankNotBetween(Integer value1, Integer value2) {
            addCriterion("item_rank not between", value1, value2, "itemRank");
            return (Criteria) this;
        }

        public Criteria andItemNameIsNull() {
            addCriterion("item_name is null");
            return (Criteria) this;
        }

        public Criteria andItemNameIsNotNull() {
            addCriterion("item_name is not null");
            return (Criteria) this;
        }

        public Criteria andItemNameEqualTo(String value) {
            addCriterion("item_name =", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameNotEqualTo(String value) {
            addCriterion("item_name <>", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameGreaterThan(String value) {
            addCriterion("item_name >", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameGreaterThanOrEqualTo(String value) {
            addCriterion("item_name >=", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameLessThan(String value) {
            addCriterion("item_name <", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameLessThanOrEqualTo(String value) {
            addCriterion("item_name <=", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameLike(String value) {
            addCriterion("item_name like", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameNotLike(String value) {
            addCriterion("item_name not like", value, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameIn(List<String> values) {
            addCriterion("item_name in", values, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameNotIn(List<String> values) {
            addCriterion("item_name not in", values, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameBetween(String value1, String value2) {
            addCriterion("item_name between", value1, value2, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemNameNotBetween(String value1, String value2) {
            addCriterion("item_name not between", value1, value2, "itemName");
            return (Criteria) this;
        }

        public Criteria andItemCountIsNull() {
            addCriterion("item_count is null");
            return (Criteria) this;
        }

        public Criteria andItemCountIsNotNull() {
            addCriterion("item_count is not null");
            return (Criteria) this;
        }

        public Criteria andItemCountEqualTo(String value) {
            addCriterion("item_count =", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountNotEqualTo(String value) {
            addCriterion("item_count <>", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountGreaterThan(String value) {
            addCriterion("item_count >", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountGreaterThanOrEqualTo(String value) {
            addCriterion("item_count >=", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountLessThan(String value) {
            addCriterion("item_count <", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountLessThanOrEqualTo(String value) {
            addCriterion("item_count <=", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountLike(String value) {
            addCriterion("item_count like", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountNotLike(String value) {
            addCriterion("item_count not like", value, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountIn(List<String> values) {
            addCriterion("item_count in", values, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountNotIn(List<String> values) {
            addCriterion("item_count not in", values, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountBetween(String value1, String value2) {
            addCriterion("item_count between", value1, value2, "itemCount");
            return (Criteria) this;
        }

        public Criteria andItemCountNotBetween(String value1, String value2) {
            addCriterion("item_count not between", value1, value2, "itemCount");
            return (Criteria) this;
        }

        public Criteria andValueRequiredIsNull() {
            addCriterion("value_required is null");
            return (Criteria) this;
        }

        public Criteria andValueRequiredIsNotNull() {
            addCriterion("value_required is not null");
            return (Criteria) this;
        }

        public Criteria andValueRequiredEqualTo(String value) {
            addCriterion("value_required =", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredNotEqualTo(String value) {
            addCriterion("value_required <>", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredGreaterThan(String value) {
            addCriterion("value_required >", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredGreaterThanOrEqualTo(String value) {
            addCriterion("value_required >=", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredLessThan(String value) {
            addCriterion("value_required <", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredLessThanOrEqualTo(String value) {
            addCriterion("value_required <=", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredLike(String value) {
            addCriterion("value_required like", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredNotLike(String value) {
            addCriterion("value_required not like", value, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredIn(List<String> values) {
            addCriterion("value_required in", values, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredNotIn(List<String> values) {
            addCriterion("value_required not in", values, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredBetween(String value1, String value2) {
            addCriterion("value_required between", value1, value2, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andValueRequiredNotBetween(String value1, String value2) {
            addCriterion("value_required not between", value1, value2, "valueRequired");
            return (Criteria) this;
        }

        public Criteria andGainExpIsNull() {
            addCriterion("gain_exp is null");
            return (Criteria) this;
        }

        public Criteria andGainExpIsNotNull() {
            addCriterion("gain_exp is not null");
            return (Criteria) this;
        }

        public Criteria andGainExpEqualTo(String value) {
            addCriterion("gain_exp =", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpNotEqualTo(String value) {
            addCriterion("gain_exp <>", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpGreaterThan(String value) {
            addCriterion("gain_exp >", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpGreaterThanOrEqualTo(String value) {
            addCriterion("gain_exp >=", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpLessThan(String value) {
            addCriterion("gain_exp <", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpLessThanOrEqualTo(String value) {
            addCriterion("gain_exp <=", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpLike(String value) {
            addCriterion("gain_exp like", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpNotLike(String value) {
            addCriterion("gain_exp not like", value, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpIn(List<String> values) {
            addCriterion("gain_exp in", values, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpNotIn(List<String> values) {
            addCriterion("gain_exp not in", values, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpBetween(String value1, String value2) {
            addCriterion("gain_exp between", value1, value2, "gainExp");
            return (Criteria) this;
        }

        public Criteria andGainExpNotBetween(String value1, String value2) {
            addCriterion("gain_exp not between", value1, value2, "gainExp");
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
     * @date 2022-09-27
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