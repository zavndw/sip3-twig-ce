/*
 * Copyright 2018-2020 SIP3.IO, Inc.
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

package io.sip3.twig.ce.configuration

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import org.springframework.web.context.WebApplicationContext

@Configuration
@ComponentScan("io.sip3.twig.ee")
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var context: WebApplicationContext

    @Value("\${security.enabled}")
    private var enabled = false

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
                .authorizeRequests().anyRequest().apply {
                    if (enabled) {
                        authenticated().and()
                                .formLogin()
                                .successHandler { _, _, authentication ->
                                    logger.info { "Login attempt. User: ${authentication.principal}, State: SUCCESSFUL" }
                                }
                                .failureHandler { _, response, exception ->
                                    logger.info { "Login attempt. User: ${exception.message}, State: FAILED" }
                                    response.sendError(HttpStatus.FORBIDDEN.value())
                                }
                                .and()
                                .exceptionHandling()
                                .authenticationEntryPoint(Http403ForbiddenEntryPoint())
                    } else {
                        permitAll()
                    }
                }
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        if (enabled) {
            context.getBeansOfType(AuthenticationProvider::class.java).forEach { (name, provider) ->
                auth!!.authenticationProvider(provider)
                logger.info { "Authentication provider '$name' added." }
            }
        }
    }
}