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

import org.aspectj.lang.annotation.Pointcut;

/**
 * Pointcuts that are used in service aspects.
 */
public class ServiceAopPointcuts
{
    /**
     * A pointcut for all DM service methods.
     */
    @Pointcut("execution(* org.finra.dm.service.*Service.*(..))")
    protected void serviceMethods()
    {
        // Pointcut methods are defined by their annotation and don't have an implementation.
    }
}
