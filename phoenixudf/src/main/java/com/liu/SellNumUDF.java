
package com.liu;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.phoenix.expression.Expression;
import org.apache.phoenix.expression.function.ScalarFunction;
import org.apache.phoenix.parse.FunctionParseNode;
import org.apache.phoenix.schema.tuple.Tuple;
import org.apache.phoenix.schema.types.PBinary;
import org.apache.phoenix.schema.types.PDataType;
import org.apache.phoenix.schema.types.PInteger;
import org.apache.phoenix.schema.types.PVarchar;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * 功能： TODO(用一句话描述类的功能)
 *
 * ──────────────────────────────────────────
 *   version  变更日期    修改人    修改说明
 * ------------------------------------------
 *   V1.0.0   2020/7/9    Liush     初版
 * ──────────────────────────────────────────
 */
//指定函数名和参数
@FunctionParseNode.BuiltInFunction(
        name ="F_SELLNUM" ,
        args = {
                @FunctionParseNode.Argument(allowedTypes = {PVarchar.class}),
        }
)
public class SellNumUDF extends ScalarFunction {
    @Override
    public String getName() {
        return  "F_SELLNUM";
    }


    public SellNumUDF(List<Expression> children) {
        super(children);
    }



    @Override
    public boolean evaluate(Tuple tuple, ImmutableBytesWritable immutableBytesWritable) {
        //获取到参数
        Expression arg1 =this.getChildren().get(0);
        //从表达式中或获取参数
        if(arg1.evaluate(tuple,immutableBytesWritable)){
            if(immutableBytesWritable.getLength()>0){
                //得到参数字节码
                byte[] valueByte=immutableBytesWritable.copyBytes();
                try {
                    String sellNum=new String(valueByte,"UTF-8");
                    if(sellNum.contains("万笔")){
                        String num=sellNum.substring(0,sellNum.indexOf("万笔"));
                        int numInt=(int)(Float.valueOf(num)*10000);
                        //将处理后的数据返回，如果执行的是类似sum运算的话返回的必须是数字类型
                        immutableBytesWritable.set(PInteger.INSTANCE.toBytes(numInt));
                        return true;
                    }
                    if(sellNum.contains("笔")){
                        String num=sellNum.substring(0,sellNum.indexOf("笔"));
                        int numInt=Integer.valueOf(num);
                        immutableBytesWritable.set(PInteger.INSTANCE.toBytes(numInt));
                        return  true;
                    }



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        }
        return false;
    }

    @Override
    public PDataType getDataType() {
        return PInteger.INSTANCE;
    }
}
