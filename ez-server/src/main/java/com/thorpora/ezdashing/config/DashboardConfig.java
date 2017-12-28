/**
 * Created by Yannick Lacaute on 17/05/17.
 * Copyright 2015-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thorpora.ezdashing.config;

import com.thorpora.ezdashing.dashboard.model.DashboardConfiguration;
import com.thorpora.ezdashing.exception.DashboardConfigNotFound;
import com.thorpora.ezdashing.exception.MissingApplicationArgumentException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.io.File;

@Profile({"prod", "dev"})
@Configuration
public class DashboardConfig {

    public static final String FILENAME = "dashboard.json";

    @Bean
    public DashboardConfiguration dashboardConfiguration(Environment env) {
        return new DashboardConfiguration(getConfigFile(env));
    }

    /**
     * We assume the dashboard.json configuration file is always in the same directory as the
     * application.properties, even if its location is defined with "spring.config.location".
     *
     * Remember that spring.config.location is the FULL PATH, including the properties filename.
     */
    private File getConfigFile(Environment env) {
        String externalConfigLocation = env.getProperty("spring.config.location");
        if (externalConfigLocation != null && !externalConfigLocation.isEmpty()) {
            String path = externalConfigLocation
                    .substring(0, externalConfigLocation.lastIndexOf('/'))
                    .replace("file:", "");
            File file = new File(path + "/" + FILENAME);
            if (!file.exists()) {
                throw new DashboardConfigNotFound(String.format("File '%s' does not exist.", file));
            }
            return file;
        }
        throw new MissingApplicationArgumentException("No arg 'spring.config.location' defined");
    }
}
