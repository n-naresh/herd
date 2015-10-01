/*
* Copyright 2015 herd contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.finra.dm.service.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.finra.dm.core.StopWatchAdvice;

/**
 * Base Service AOP Spring module configuration. This class defines specific configuration related to aspects.
 */
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class ServiceBasicAopSpringModuleConfig extends ServiceAopPointcuts
{
    /**
     * Around advice that logs methods times for all service methods.
     *
     * @param pjp the proceeding join point.
     *
     * @return the return value of the method we are advising.
     * @throws Throwable if there were any problems executing the method.
     */
    @Around("serviceMethods()")
    public Object logMethodTime(ProceedingJoinPoint pjp) throws Throwable
    {
        return StopWatchAdvice.logMethodTime(pjp);
    }
}
