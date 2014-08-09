/**
 * Exception Auxiliary Interface - Part of Fineswap Android Utilities.
 * Copyright (C) 2014 Fineswap Blog & App. All rights reserved.
 * http://fineswap.com/
 *
 * Released under the MIT license
 * http://en.wikipedia.org/wiki/MIT_License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.fineswap.lib.android.aux;

/**
 * @author Noor Dawod
 * @since 1.0
 */
public interface FsException {

  /**
   * Handy exception to throw when a parameter's value is either null or empty.
   *
   * @since 1.0
   */
  public final static RuntimeException EXCEPTION_NULL_VALUE =
    new IllegalStateException("The value cannot be null.");

  /**
   * Handy exception to throw when a parameter's value is either null or empty.
   *
   * @since 1.0
   */
  public final static RuntimeException EXCEPTION_NULL_OR_EMPTY =
    new IllegalStateException("The value is either null or empty.");

  /**
   * Handy exception to throw when a parameter's value is either null or invalid.
   *
   * @since 1.0
   */
  public final static RuntimeException EXCEPTION_NULL_OR_INVALID =
    new IllegalStateException("The value is either null or invalid.");

  /**
   * Handy exception to throw when a content view is either null or undefined.
   *
   * @since 1.0
   */
  public final static RuntimeException EXCEPTION_CONTENT_VIEW_NULL_OR_EMPTY =
    new IllegalStateException("Content view is either null or not defined.");

}
