package com.std_data_mgmt.app.rbac;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.std_data_mgmt.app.entity.Role;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {
    Role[] value();
    /**
     * @return 
     * <ul>
     *   <li><code>true</code>: the user must have all roles specified</li>
     *   <li><code>false</code>: the user must have at least one role specified</li>
     * </ul>
    */
    boolean requireAll() default false; 
}