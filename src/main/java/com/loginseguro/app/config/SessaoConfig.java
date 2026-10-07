package com.loginseguro.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

@Configuration
@EnableMongoHttpSession(collectionName = "sessoes")
public class SessaoConfig {
}
