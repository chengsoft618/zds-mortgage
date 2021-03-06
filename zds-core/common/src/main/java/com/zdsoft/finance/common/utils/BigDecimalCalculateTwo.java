package com.zdsoft.finance.common.utils;

import java.math.BigDecimal;

public class BigDecimalCalculateTwo {
 // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 12;

    // 这个类不能实例化
    private BigDecimalCalculateTwo() {}

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * 多个乘数相乘
     * @return
     */
    public static BigDecimal mulList(BigDecimal ... param){
        BigDecimal value =  BigDecimal.valueOf(0d);
        if(param != null){
            value = BigDecimal.valueOf(1d);
            for (BigDecimal d : param) {
                value = BigDecimalCalculateTwo.mul(value, d);
            }
        }
        return value;
    }
    
    /**
     * 多个加数相加
     * @return
     */
    public static BigDecimal addList(BigDecimal ... param){
        BigDecimal value = BigDecimal.valueOf(0d);
        if(param != null){
            for (BigDecimal d : param) {
                value = BigDecimalCalculateTwo.add(value, d);
            }
        }
        return value;
    }
    
    /**
     * 减去多个被减数
     * @param reduction  减数
     * @param param 被减数
     * @return
     */
    public static BigDecimal subList(BigDecimal reduction, BigDecimal ... param){
        BigDecimal value = reduction;
        if( param != null){
            for (BigDecimal d : param) {
                value = BigDecimalCalculateTwo.sub(value, d);
            }
        }
        return value;
    }
    
    /**
     * 减去多个被减数
     * @param divisor  减数
     * @param param 被减数
     * @return
     */
    public static BigDecimal divList(BigDecimal divisor, BigDecimal ... param){
        BigDecimal value = divisor;
        if( param != null){
            for (BigDecimal d : param) {
                value = BigDecimalCalculateTwo.div(value, d);
            }
        }
        return value;
    }
    
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度必须为整数或者0!");
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度必须为整数或者0!");
        }
        BigDecimal one = new BigDecimal("1");
        return v.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }
}
