/**
 * View-related Utils - Part of Fineswap Android Utilities.
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

package com.fineswap.android.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A collection of helpful, static functions for dealing with views.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsView {

  /**
   * Sets a view's background drawable.
   * Due to SDK changes, the function calls the correct method appropriately.
   *
   * @param view Target view to set the background on
   * @param drawable Target drawable (can be null)
   * @since 1.0
   */
  @SuppressWarnings("deprecation")
  public static void setBackgroundDrawable(View view, Drawable drawable) {
    // Test if the SDK version is less than API level 16 (Jelly Bean).
    if(android.os.Build.VERSION.SDK_INT < 16) {
      view.setBackgroundDrawable(drawable);
    } else {
      view.setBackground(drawable);
    }
  }

  /**
   * Recursively garbage-collects a view by removing its associated listeners,
   * animations, description, background drawables and tags.
   * Optionally clears the view's tag object completely by detecting it type.
   *
   * Note: Children's backgrounds will be cleared if 'removeAllViews' is true.
   *
   * @param view Target view to clear out
   * @param removeAllViews Whether to remove all of the target view's children
   * @param emptyTag Whether to empty tag objects
   * @param clearBackground Whether to clear backgrounds
   * @since 1.0
   */
  public static void empty(View view, boolean removeAllViews, boolean emptyTag, boolean clearBackground) {
    if(null == view) {
      return;
    }

    if(view instanceof ViewGroup) {
      ViewGroup container = (ViewGroup)view;
      int pos = container.getChildCount();
      while(0 < pos--) {
        empty(container.getChildAt(pos), false, emptyTag, removeAllViews || clearBackground);
      }
      if(removeAllViews) {
        try {
          container.removeAllViews();
        } catch(Throwable ignored) {
        }
      }
    } else if(view instanceof ImageView) {
      Drawable drawable = ((ImageView)view).getDrawable();
      FsGraphics.clearDrawableBitmap(drawable);
      ((ImageView)view).setImageDrawable(drawable = null);
    }

    // Remove any tags.
    Object tag = view.getTag();
    if(null != tag) {
      if(emptyTag) {
        if(tag instanceof List) {
          ((List)tag).clear();
        } else if(tag instanceof Queue) {
          ((Queue)tag).clear();
        } else if(tag instanceof Map) {
          ((Map)tag).clear();
        } else if(tag instanceof Collection) {
          ((Collection)tag).clear();
        }
      }
      view.setTag(tag = null);
    }

    // Remove any click listeners.
    try {
      if(view instanceof AdapterView) {
        ((AdapterView)view).setOnItemClickListener(null);
      } else {
        view.setOnClickListener(null);
      }
    } catch(Throwable ignored) {
    }

    // Should the background be removed.
    if(clearBackground) {
      Drawable background = view.getBackground();
      if(null != background) {
        background.setCallback(null);
        view.unscheduleDrawable(background);
        FsGraphics.clearDrawableBitmap(background);
        setBackgroundDrawable(view, background = null);
      }
    }

    // Exist before API level 8.
    view.setAnimation(null);
    view.setContentDescription(null);
    view.setOnFocusChangeListener(null);
    view.setOnKeyListener(null);
    view.setOnLongClickListener(null);
    view.setOnTouchListener(null);

    // Exist after, thus need wrapping.
    try {
      view.setOnDragListener(null);
    } catch(NoSuchMethodError ignored) {
      // Since API level 11.
    }
    try {
      view.setOnSystemUiVisibilityChangeListener(null);
    } catch(NoSuchMethodError ignored) {
      // Since API level 11.
    }
    try {
      view.setOnGenericMotionListener(null);
    } catch(NoSuchMethodError ignored) {
      // Since API level 12.
    }
    try {
      view.setOnHoverListener(null);
    } catch(NoSuchMethodError ignored) {
      // Since API level 14.
    }
  }

  /**
   * Shortcut function to clear-out the supplied view completely.
   *
   * @param view Target view to empty
   * @since 1.0
   */
  public static void empty(View view) {
    empty(view, true, true, true);
  }

}
