/*
 * Copyright 2017 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.objectfactory;

import com.google.inject.Provider;
import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentContext;
import com.navercorp.pinpoint.bootstrap.plugin.monitor.DataSourceMonitorRegistry;
import com.navercorp.pinpoint.profiler.interceptor.factory.AnnotatedInterceptorFactory;
import com.navercorp.pinpoint.profiler.metadata.ApiMetaDataService;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ObjectBinderFactory {
    private final ProfilerConfig profilerConfig;
    private final Provider<TraceContext> traceContextProvider;
    private final DataSourceMonitorRegistry dataSourceMonitorRegistry;
    private final Provider<ApiMetaDataService> apiMetaDataService;

    public ObjectBinderFactory(ProfilerConfig profilerConfig, Provider<TraceContext> traceContextProvider, DataSourceMonitorRegistry dataSourceMonitorRegistry, Provider<ApiMetaDataService> apiMetaDataService) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (traceContextProvider == null) {
            throw new NullPointerException("traceContextProvider must not be null");
        }
        if (dataSourceMonitorRegistry == null) {
            throw new NullPointerException("dataSourceMonitorRegistry must not be null");
        }
        if (apiMetaDataService == null) {
            throw new NullPointerException("apiMetaDataService must not be null");
        }

        this.profilerConfig = profilerConfig;
        this.traceContextProvider = traceContextProvider;
        this.dataSourceMonitorRegistry = dataSourceMonitorRegistry;
        this.apiMetaDataService = apiMetaDataService;
    }

    public AutoBindingObjectFactory newAutoBindingObjectFactory(InstrumentContext pluginContext, ClassLoader classLoader, ArgumentProvider... argumentProviders) {
        final TraceContext traceContext = this.traceContextProvider.get();
        return new AutoBindingObjectFactory(profilerConfig, traceContext, pluginContext, classLoader, argumentProviders);
    }


    public InterceptorArgumentProvider newInterceptorArgumentProvider(InstrumentClass instrumentClass) {
        ApiMetaDataService apiMetaDataService = this.apiMetaDataService.get();
        return new InterceptorArgumentProvider(dataSourceMonitorRegistry, apiMetaDataService, instrumentClass);
    }

    public AnnotatedInterceptorFactory newAnnotatedInterceptorFactory(InstrumentContext pluginContext, boolean exceptionHandle) {
        final TraceContext traceContext = this.traceContextProvider.get();
        ApiMetaDataService apiMetaDataService = this.apiMetaDataService.get();
        return new AnnotatedInterceptorFactory(profilerConfig, traceContext, dataSourceMonitorRegistry, apiMetaDataService, pluginContext, exceptionHandle);
    }
}
