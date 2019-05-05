package com.reminder.infrastructure.config

import com.reminder.api.Routing
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer


//@Configuration
//@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources!!.resourceId(RESOURCE_ID).stateless(false)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.anonymous().disable()
            .authorizeRequests()
            .antMatchers(Routing.LOGIN_API).permitAll()
            .antMatchers(Routing.REGISTER_API).permitAll()
            .antMatchers("/api/**").authenticated()
            //.antMatchers("/**").access("hasRole('USER')")
            .and().exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
    }

    companion object {

        private val RESOURCE_ID = "resource_id"
    }

}