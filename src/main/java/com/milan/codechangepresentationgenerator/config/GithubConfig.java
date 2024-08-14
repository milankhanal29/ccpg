package com.milan.codechangepresentationgenerator.config;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubConfig {
    @Value("${github.token}")
    private String githubToken;
    @Bean
    public GitHub gitHub() {
        try {
            return GitHub.connectUsingOAuth(githubToken);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to GitHub", e);
        }
    }
}
