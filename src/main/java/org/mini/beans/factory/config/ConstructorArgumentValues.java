package org.mini.beans.factory.config;

import java.util.*;

/**
 * 解释：ArgumentValues类是一个简单的数据结构，用于保存bean构造函数的参数值。
 */
public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<ConstructorArgumentValue>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.constructorArgumentValueList.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        ConstructorArgumentValue constructorArgumentValue = this.constructorArgumentValueList.get(index);
        return constructorArgumentValue;
    }

    public int getArgumentCount() {
        return (this.constructorArgumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.constructorArgumentValueList.isEmpty());
    }
}