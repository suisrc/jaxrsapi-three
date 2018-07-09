package com.suisrc.weixin.core.check;

/**
 * 类型assert工具
 * 全部通过，不进行限定
 * @author Y13
 *
 */
public class TypeTrueAssert implements TypeAssert {
    
    /**
     * 执行assert
     * @param type
     * @return
     */
    public boolean apply(String type, String value) {
        return true;
    }

}
