/**
 * Graphics-related Utils - Part of Fineswap Android Utilities.
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;

/**
 * A collection of helpful, static functions for dealing with graphics subsystem.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsGraphics {

  /**
   * Clears a bitmap from a drawable by recycling it.
   *
   * @param drawable Target drawable object to clear
   * @since 1.0
   */
  public static void clearDrawableBitmap(Drawable drawable) {
    if(drawable instanceof BitmapDrawable) {
      Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
      if(null != bitmap) {
        bitmap.recycle();
        bitmap = null;
      }
    }
  }

  /**
   * Decode the alpha, red, green and blue channels of a color.
   *
   * @param color A color in the format 0xAARRGGBB to decode
   * @return A 4-integer array to hold the decoded values
   * @since 1.0
   */
  public static int[] decodeColorChannels(int color) {
    int[] channels = new int[4];
    channels[0] = Color.alpha(color);
    channels[1] = Color.red(color);
    channels[2] = Color.green(color);
    channels[3] = Color.blue(color);
    return channels;
  }

  /**
   * Get an Activity's top-level content view. If none has been specified, or if
   * an error occurred during detection, null will returned.
   *
   * @param activity Target Activity to probe
   * @return Activity's top-level content view
   * @since 1.0
   */
  public static ViewGroup getRootContentView(Activity activity) {
    try {
      // Activity's root content view.
      ViewGroup contentView = (ViewGroup)((ViewGroup)activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);

      // Just to force a NullPointerException if activityContent is null.
      contentView.getId();

      return contentView;
    } catch(ClassCastException e) {
      e.printStackTrace();
    } catch(NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the height, in pixels, of the activity's status bar.
   * If the activity has no status bar, zero will be returned.
   * If an error occurs in detection, -1 will be returned.
   *
   * @param activity The activity on which to perform the detection
   * @return Status bar height in pixels
   * @since 1.0
   */
  public static int getStatusBarHeight(Activity activity) {
    try {
      // Retrieve the overall visible display size of the content view.
      Rect rect = new Rect();
      activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

      // Top position of the content view rests just below the status bar.
      return rect.top;
    } catch(ClassCastException e) {
      e.printStackTrace();
    } catch(NullPointerException e) {
      e.printStackTrace();
    }

    return -1;
  }
}
