package io.seventytwo.demo.hr;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HrJooqConfiguration {

    @Bean
    Settings jooqSettings() {
        return new Settings().withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
