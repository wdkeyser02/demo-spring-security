/**
 *
 */
package com.demo.constant;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
public interface BasicStatus<T extends Enum<?>> {

	String getStatusValue();

	BasicStatus<T>[] nextStatus();

}
