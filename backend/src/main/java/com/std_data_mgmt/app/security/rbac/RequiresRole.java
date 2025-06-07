package com.std_data_mgmt.app.security.rbac;

import com.std_data_mgmt.app.enums.Role;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method or class requires specific roles for access.
 * This annotation can be applied to a method to restrict its execution or to a
 * class to apply the role requirement to all its methods.
 *
 * <p>When applied to a class, all methods within that class will inherit
 * the specified role requirements unless individually overridden.
 *
 * <p>Usage example:
 * <pre>{@code
 * @RequiresRole(Role.SUPERVISOR)
 * public void createResource() {
 * // Only users with SUPERVISOR role can access
 * }
 *
 * @RequiresRole(value = {Role.SUPERVISOR, Role.HOD}, requireAll = false)
 * public void viewContent() {
 * // Users with either SUPERVISOR or HOD role can access
 * }
 *
 * @RequiresRole(value = {Role.SUPERVISOR, Role.HOD}, requireAll = true)
 * public void modifyContent() {
 * // Users with SUPERVISOR and HOD role can access
 * }
 * }</pre>
 *
 * @see com.std_data_mgmt.app.enums.Role
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {
    Role[] value();

    boolean requireAll() default false;
}