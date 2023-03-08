package org.shiloh.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shiloh
 * @date 2023/3/8 17:55
 */
@Configuration
@ComponentScan(basePackages = {"org.shiloh.web.*"})
public class ComponentScanConfig {
}
