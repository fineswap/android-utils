/**
 * ScrollView Widget - Part of Fineswap Android Utilities.
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

package com.fineswap.lib.android.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import java.util.ArrayList;

/**
 * On certain versions of Android (noticeably 4.1.x branch), nested ScrollView's
 * just don't work for the outermost one intercepts the touch events and blocks
 * them from reaching the nested ScrollView's.
 *
 * This widget will properly fix this. To enable it, define it in XML as follows:
 *
 * <pre>
 * &lt;com.fineswap.android.widgets.FsScrollView
 *   <strong style="color:#AA0000">android:tag="nested-parent"</strong>
 * &gt;
 *   &lt;LinearLayout&gt;
 *     &lt;!-- Content goes here --&gt;
 *     &lt;!-- Content goes here --&gt;
 *     &lt;!-- Content goes here --&gt;
 *
 *     &lt;!-- Nested ScrollView will be properly handled --&gt;
 *     <strong>&lt;ScrollView&gt;
 *
 *     &lt;/ScrollView&gt;</strong>
 *
 *     &lt;!-- Content goes here --&gt;
 *     &lt; Content goes here --&gt;
 *     &lt;!-- Content goes here --&gt;
 *   &lt;/LinearLayout&gt;
 * &lt;/com.fineswap.android.widgets.FsScrollView&gt;
 * </pre>
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsScrollView extends ScrollView {

  private final static String TAG_MAGIC_WORD = "nested-parent";
  private ArrayList<ScrollView> nestedScrollViews;

  /**
   * Constructor.
   *
   * @param ctx App context
   * @since 1.0
   */
  public FsScrollView(Context ctx) {
    super(ctx);
  }

  /**
   * Constructor.
   *
   * @param ctx App context
   * @param attrs Passed attributes
   * @since 1.0
   */
  public FsScrollView(Context ctx, AttributeSet attrs) {
    super(ctx, attrs);
  }

  /**
   * Constructor.
   *
   * @param ctx App context
   * @param attrs Passed attributes
   * @param defStyle Default style
   * @since 1.0
   */
  public FsScrollView(Context ctx, AttributeSet attrs, int defStyle) {
    super(ctx, attrs, defStyle);
  }

  /**
   * Invoked by Android to test whether this ScrollView should intercept the
   * touch event. Now it's time to check whether the touch happened in the
   * nested (children) ScrollView's, and if so, then the touch event should not
   * be intercepted.
   *
   * @param ev Touch event details
   * @return True if the touch event should be intercepted, false otherwise
   * @since 1.0
   */
  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if(null != nestedScrollViews && 0 < nestedScrollViews.size()) {
      int touchX = (int)ev.getX(), touchY = (int)ev.getY();
      for(ScrollView nestedScrollView : nestedScrollViews) {
        int top = nestedScrollView.getTop();
        int bottom = nestedScrollView.getBottom();
        int left = nestedScrollView.getLeft();
        int right = nestedScrollView.getRight();

        // Check if this falls within the nested scroll view's area.
        if(touchX <= right && touchX >= left && touchY <= bottom && touchY >= top) {
          return false;
        }
      }
    }
    return super.onInterceptTouchEvent(ev);
  }

  /**
   * Invoked by Android when this ScrollView is attached to the window. When
   * that happens, it's good time to scan for nested ScrollView's.
   *
   * @since 1.0
   */
  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Object tag = getTag();
    if(null != tag && TAG_MAGIC_WORD.equals(tag)) {
      nestedScrollViews = new ArrayList();
      populateNestedScrollView(this);
    }
  }

  /**
   * Invoked by Android when this ScrollView is detached from the window. When
   * that happens, it's good time to release retained resources.
   *
   * @since 1.0
   */
  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if(null != nestedScrollViews) {
      nestedScrollViews.clear();
      nestedScrollViews = null;
    }
  }

  private void populateNestedScrollView(ViewGroup container) {
    int childrenCount = container.getChildCount(), child = -1;
    while(childrenCount > ++child) {
      View view = container.getChildAt(child);
      if(view instanceof ScrollView) {
        nestedScrollViews.add((ScrollView)view);
      } else if(view instanceof ViewGroup) {
        populateNestedScrollView((ViewGroup)view);
      }
    }
  }

}
