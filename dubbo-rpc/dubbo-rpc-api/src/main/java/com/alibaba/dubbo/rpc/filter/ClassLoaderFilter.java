/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * ClassLoaderInvokerFilter
 * 切换当前工作线程的类加载器到接口的类加载器，以便和接口的类加载器的上下文一起工作
 */
@Activate(group = Constants.PROVIDER, order = -30000)
public class ClassLoaderFilter implements Filter {

    /**
     * 如果要实现违反双亲委派模型来查找Class,那么通常会使用上下文类加载器(ContextClassLoader)。当前框架线程的类加载器可能
     * 和Invoker接口的类加载器不是同一个，而当前框架线程中又需要获取Invoker的类加载器中的一些 Class,为 了避免出现 ClassNotFoundException,
     * 此时只需要使用 Thread. currentThread().getContextClassLoader()就可以获取Invoker的类加载器，进而获得这个类加载器中的Classo
     *
     * 常见的使用例子有DubboProtocol#optimizeSerialization方法，会根据Invoker中配置的optimizer参数获取扩展的自定义序列化处理类，这些外部引入的序列化类在框架的类加载器
     * 中肯定没有，因此需要使用Invoker的类加载器获取对应的类。
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        ClassLoader ocl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(invoker.getInterface().getClassLoader());
        try {
            return invoker.invoke(invocation);
        } finally {
            Thread.currentThread().setContextClassLoader(ocl);
        }
    }

}
