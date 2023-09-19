package com.damu.entity;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
@Data
public class RpcRequest implements Serializable {

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     *  方法名
     */
    private String methodName;

    /**
     *   参数
     */
    private Object[] parameters;

    /**
     *   参数类型
     */
    private Class<?>[] paramTypes;
}
