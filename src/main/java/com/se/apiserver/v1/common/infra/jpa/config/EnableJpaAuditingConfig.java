package com.se.apiserver.v1.common.infra.jpa.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnableJpaAuditingConfig {
}
