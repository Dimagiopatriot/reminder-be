package com.reminder.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore


@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey("as466gf")
        return converter
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Throws(Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer) {

        configurer
            .inMemory()
            .withClient(CLIENT_ID)
            .secret(CLIENT_SECRET)
            .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
            .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
            .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
            .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS)
    }

    companion object {

        internal val CLIENT_ID = "reminder-be-client"
        internal val CLIENT_SECRET = "reminder-be-secret"
        internal val GRANT_TYPE_PASSWORD = "password"
        internal val AUTHORIZATION_CODE = "authorization_code"
        internal val REFRESH_TOKEN = "refresh_token"
        internal val IMPLICIT = "implicit"
        internal val SCOPE_READ = "read"
        internal val SCOPE_WRITE = "write"
        internal val TRUST = "trust"
        internal val ACCESS_TOKEN_VALIDITY_SECONDS = 2190 // 3 month
        internal val FREFRESH_TOKEN_VALIDITY_SECONDS = 2190 // 3 month
    }
}