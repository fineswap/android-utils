/**
 * Layout Auxiliary Interface - Part of Fineswap Android Utilities.
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

import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author Noor Dawod
 * @since 1.0
 */
public interface FsLayout {

  /**
   * Reusable, global layout parameters for ViewGroup instances.
   *
   * @since 1.0
   */
  public final static ViewGroup.LayoutParams VIEWGROUP_WRAP_CONTENT =
    new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    );

  /**
   * Reusable, global layout parameters for ViewGroup instances.
   *
   * @since 1.0
   */
  public final static ViewGroup.LayoutParams VIEWGROUP_MATCH_PARENT =
    new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    );

  /**
   * Reusable, global layout parameters for FrameLayout instances.
   *
   * @since 1.0
   */
  public final static FrameLayout.LayoutParams FRAMELAYOUT_WRAP_CONTENT =
    new FrameLayout.LayoutParams(
      FrameLayout.LayoutParams.WRAP_CONTENT,
      FrameLayout.LayoutParams.WRAP_CONTENT
    );

  /**
   * Reusable, global layout parameters for FrameLayout instances.
   *
   * @since 1.0
   */
  public final static FrameLayout.LayoutParams FRAMELAYOUT_MATCH_PARENT =
    new FrameLayout.LayoutParams(
      FrameLayout.LayoutParams.MATCH_PARENT,
      FrameLayout.LayoutParams.MATCH_PARENT
    );

}
