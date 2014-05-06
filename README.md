Fineswap Android Utilities
==========================

A collection of reusable interfaces, classes, components and UI utilities for Android.

The complete library is being used, as-is, to power [Fineswap][Fineswap]'s Android app.

## Features
* Loosely-coupled classes and utilities
* Down-to-earth easy, functional and with a proven record
* Utilities live inside their own packages
* Full Javadoc including inline *@since* tag
* Prioritizing garbage-collection and low memory footprint

## FsIntroOverlay UI utility

This is a fully-fledged utility for presenting [Coach Marks][coachmarks] easily
in an Android app. It features an interface-based database to keep track of the
previously shown overlays, new areas in the page (identified by their view id)
which are new and require an overlay, a slide-based approach and much more.

## FsIntroOverlay sample usage

Given a page with couple of buttons, let's see how to present an intro overlay for new users:

```java
import com.fineswap.android.intro.FsIntroFactory;
import com.fineswap.android.intro.FsIntroOverlay;
import com.fineswap.android.intro.FsRingResource;
import com.fineswap.android.intro.FsSlideResource;

public class Main extends Activity {

  // Each overlay instance has an accompanying name and version, so that when a
  // new version is introduced, it will be shown automatically.
  private final static String INTRO_PAGE = "Main";
  private final static int INTRO_VERSION = 1;

  // Each slide (page) in the overlay also has a name and version. In case one
  // of the slides is new, it will be shown first.
  private final static String SLIDE_FRONT = "Front";
  private final static int SLIDE_FRONT_VER = 102;

  // Each area on the screen (called a ring) has also a name and version. When
  // that particular area changes, one can easily change its version and the
  // relevant slide will be presented.
  private final static String RING_LOGIN = "Login";
  private final static String RING_CONTACT = "Contact";
  private final static String RING_SHARE = "Share";
  private final static int ALL_RINGS_VER = 200;

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    // Set content view.
    setContentView(R.layout.main);
  }

  @Override
  public void onResume() {
    // Instantiate a new slide (page) to be added to the overlay.
    // Each slide has an accompanying layout which will be inflated dynamically.
    // The contents of this layout is entirely up to the developer/designer.
    FsSlideResource slide = new FsSlideResource(
      new FsMetaVersion(
        R.layout.overlay_front_page,  // Layout to be inflated for this slide
        SLIDE_FRONT,                  // Unique name for this slide
        SLIDE_FRONT_VER               // The slide's version (bump up to show again)
      )
    );

    // An area on the page that is associated with a slide is called a ring.
    // Each ring may maintain its own Look-And-Feel, but for practicality reasons,
    // we'll use just one object for all rings.
    FsRingResource.LookAndFeel laf = new FsRingResource.LookAndFeel(
      getDimension(R.dimen.front_page_ring_padding),
      getColor(R.color.front_page_ring_color),
      getDimension(R.dimen.front_page_ring_thickness)
    );

    // The slide might contain static and dynamic contents. The dynamic content
    // is shown/hidden based on previous history of the slide, and whether the
    // page contains rings that are associated with the content.
    slide.addRingResource(new FsRingResource(
      new FsMetaVersion(laf, RING_LOGIN, ALL_RINGS_VER),  // A new ring with its own LAF
      R.id.front_page_btn_login,                          // A button in the page
      R.id.overlay_front_page_login                       // An area in the slide
    ));
    slide.addRingResource(new FsRingResource(
      new FsMetaVersion(laf, "camera", ALL_RINGS_VER),
      R.id.front_page_btn_contact,
      R.id.overlay_front_page_contact
    ));
    slide.addRingResource(new FsRingResource(
      new FsMetaVersion(laf, "refresh", ALL_RINGS_VER),
      R.id.front_page_btn_share,
      R.id.overlay_front_page_share
    ));

    // Set click handler for buttons that may exist in the slide's inflated layout.
    slide.setOnClickListener(
      R.id.overlay_front_page_btn_ok,         // Button id in the inflated layout
      new FsSlideResource.OnClickHandler() {  // Click handler
        @Override
        public void onClick(final FsSlideResource slide, final View view) {
          // Dismiss the overlay and garbage-collect any retained resources.
          slide.getIntroOverlay().destroy();
        }
      }
    );

    // Instantiate a new instance.
    FsIntroOverlay intro = FsIntroFactory.getInstance(
      new FsMetaVersion(
        this,           // Running Activity
        INTRO_PAGE,     // Unique name for this instance
        INTRO_VERSION   // Version of this instance
      )
    );

    // Attach this slide to the overlay instance.
    intro.attach(slide);

    // Show this slide, but if it didn't show, just wrap things up.
    if(!intro.show(slide)) {
      intro.destroy();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // Get this activity's content view.
    View contentView = FsView.getRootContentView(this);

    // Clean up the content view completely.
    FsView.empty(contentView);
  }

  private int getDimension(int dimenId) {
    // Get the actual dimention of the passed resource, and return it.
  }

  private int getColor(int colorId) {
    // Get the actual color of the passed resource, and return it.
  }

}
```

That was simple, wasn't it?

## FsView sample usage

Take the following for an easy-to-use cleaning of an Activity's content view when *onDestroy()* is called:

```java
import com.fineswap.android.utils.FsView;

public class Main extends Activity {

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    // Set content view.
    setContentView(R.layout.main);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // Get this activity's content view.
    View contentView = FsView.getRootContentView(this);

    // Clean up the content view completely.
    FsView.empty(contentView);
  }

}
```

## FsException, FsLayout sample usage

A couple of LayoutParams are already there for use in apps and few handy exceptions, too:

```java
public class SampleActivity extends Activity
  // Auxiliary interfaces by their full package name
  implements
    com.fineswap.android.aux.FsException,
    com.fineswap.android.aux.FsLayout {

  private LayoutInflater inflater;

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    // Prepare inflater instance.
    inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Set content view.
    setContentView(R.layout.main);
  }

  /*
  .
  .
  .
  */

  private ViewGroup inflateViewGroup(int viewId) {
    // Inflate this view.
    View view = inflater.inflate(viewId, null);

    // Is it of the correct type?
    if(null == view || !(view instanceof ViewGroup)) {
      throw EXCEPTION_NULL_OR_INVALID;
    }

    // Attach default LayoutParams.
    view.setLayoutParams(VIEWGROUP_MATCH_PARENT);

    return (ViewGroup)view;
  }

  private FrameLayout inflateFrameLayout(int viewId) {
    // Inflate this view.
    View view = inflater.inflate(viewId, null);

    // Is it of the correct type?
    if(null == view || !(view instanceof FrameLayout)) {
      throw EXCEPTION_NULL_OR_INVALID;
    }

    // This is a FrameLayout view.
    FrameLayout frameLayoutView = (FrameLayout)view;

    // Attach default LayoutParams.
    frameLayoutView.setLayoutParams(FRAMELAYOUT_MATCH_PARENT);

    return frameLayoutView;
  }

}
```

## CHANGELOG

Please see the [CHANGELOG][CHANGELOG] page for updated information about this library.

Terms of Use
------------

[MIT License][mitlicense]

    Copyright (C) 2014 Fineswap Blog & App

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

[Fineswap]:           http://fineswap.com/                                      "Fineswap Blog & App"
[CHANGELOG]:          https://github.com/fineswap/android-utils/wiki            "CHANGELOG Wiki Page"
[coachmarks]:         https://www.google.com/search?q=Coach+Marks               "Coach Marks UI pattern"
[mitlicense]:         http://en.wikipedia.org/wiki/MIT_License                  "MIT License"
