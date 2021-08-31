package com.shred.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = {CommonConstants.CONSUMER,CommonConstants.PROVIDER})
public class DubboInvokeFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long currentTimeMillis = System.currentTimeMillis();

        try {
            return invoker.invoke(invocation);
        } finally {
            System.out.println("耗时："+ (System.currentTimeMillis() -  currentTimeMillis)+" 毫秒" );
        }

    }
}
